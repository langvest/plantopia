package by.langvest.plantopia.command.special;

import by.langvest.plantopia.command.PlantopiaMessenger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.IOWorker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * {@link PlantopiaWipeCommand} provides a mechanism to perform two critical, sequential operations:
 * <ol>
 *     <li>Force a reload of all server datapacks, including worldgen JSON files.</li>
 *     <li>Completely regenerate chunks in a given radius around a player.</li>
 * </ol>
 *
 * This serves as an essential developer tool for testing world generation changes without creating a new world.
 * To guarantee that a chunk is rebuilt from scratch, its previous state must be forcibly and completely erased.
 * The process is as follows:
 * <ol>
 *     <li><b>Disable Autosave:</b> The chunk is found in memory and marked as "not dirty" by calling <code>setUnsaved(false)</code>.
 *     This prevents the game's autosave system from writing the chunk back to disk during the operation.</li>
 *     <li><b>Forcibly Remove from Memory:</b> The chunk's {@link ChunkHolder} is directly removed from the internal
 *     {@code chunkMap.updatingChunkMap}. This prevents the {@link DistanceManager} from keeping the chunk loaded.</li>
 *     <li><b>Delete from Disk:</b> A task is queued to physically delete the chunk's data from its region file.</li>
 *     <li><b>Request Anew:</b> When the game needs the chunk again, it finds no trace of it in memory or on disk
 *     and is forced to run the entire generation process from the beginning.</li>
 * </ol>
 */
public class PlantopiaWipeCommand {
    private static final PlantopiaMessenger messenger = PlantopiaMessenger.getDefaultInstance();

    /**
     * The main method to orchestrate the regeneration process.
     * It ensures that datapacks are reloaded before chunk regeneration begins.
     */
    public static void wipeChunksAroundPlayer(@Nullable ServerPlayer player, int radius) {
        if (player == null) return;

        var server = player.getServer();
        if (server == null) return;

        long startTime = System.nanoTime();
        int chunkCount = (int) Math.pow(2 * radius + 1, 2);
        messenger.info(player, "Regenerating " + chunkCount + " chunks...");

        // Chain the asynchronous operations: first reload, then regenerate.
        // The second task is executed on the server thread to ensure thread safety.
        reloadDatapacks(server, player)
            .thenRunAsync(() -> regenerateChunksInRadius(player, radius, startTime, chunkCount), server);
    }

    /**
     * Handles the asynchronous reloading of all server datapacks.
     * This is the necessary first step to make the server aware of any changes to worldgen JSON files.
     */
    private static @NotNull CompletableFuture<Void> reloadDatapacks(@NotNull MinecraftServer server, @NotNull ServerPlayer player) {
        return server.reloadResources(server.getPackRepository().getSelectedIds());
    }

    /**
     * Orchestrates the chunk regeneration process.
     * This method's responsibility is to prepare the list of tasks and handle their collective completion.
     * The logic for each individual chunk is delegated to {@link #safelyUnloadAndQueueForDeletion}.
     */
    private static void regenerateChunksInRadius(@NotNull ServerPlayer player, int radius, long startTime, int chunkCount) {
        var chunkSource = player.serverLevel().getChunkSource();
        var chunkMap = chunkSource.chunkMap;
        var chunkStorage = chunkMap.worker;

        int centerX = player.chunkPosition().x;
        int centerZ = player.chunkPosition().z;

        List<CompletableFuture<Void>> deletionFutures = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                var chunkPos = new ChunkPos(centerX + dx, centerZ + dz);
                deletionFutures.add(safelyUnloadAndQueueForDeletion(chunkMap, chunkStorage, chunkPos));
            }
        }

        // Wait for all deletion tasks to complete, then handle the result.
        CompletableFuture.allOf(deletionFutures.toArray(new CompletableFuture[0]))
            .whenCompleteAsync((res, ex) -> handleRegenerationCompletion(ex, player, chunkSource, startTime, chunkCount), player.getServer());
    }

    /**
     * Performs the direct unload logic for a single chunk.
     * This is the core of the regeneration strategy.
     */
    private static @NotNull CompletableFuture<Void> safelyUnloadAndQueueForDeletion(@NotNull net.minecraft.server.level.ChunkMap chunkMap, @NotNull IOWorker chunkStorage, @NotNull ChunkPos chunkPos) {
        long pos = chunkPos.toLong();

        // Find the chunk holder in the map of chunks being updated.
        ChunkHolder holder = chunkMap.getUpdatingChunkIfPresent(pos);

        if (holder != null) {
            // Mark the chunk as "clean" to prevent the autosave system from interfering.
            // We use getLastAvailable() to get the chunk in its current state without waiting for futures.
            ChunkAccess chunkAccess = holder.getLastAvailable();

            if (chunkAccess instanceof ImposterProtoChunk imposter) {
                imposter.getWrapped().setUnsaved(false);
            } else if (chunkAccess instanceof LevelChunk levelChunk) {
                levelChunk.setUnsaved(false);
            }

            // Forcibly remove the chunk from memory. This is the key step to prevent the
            // DistanceManager from keeping it loaded.
            chunkMap.updatingChunkMap.remove(pos);
        }

        // Queue the task to delete the chunk file from disk.
        return chunkStorage.store(chunkPos, null);
    }

    /**
     * Handles the final steps of the regeneration process after all chunks have been deleted.
     * This includes error handling, triggering the client-side update, and sending final messages.
     */
    private static void handleRegenerationCompletion(@Nullable Throwable ex, @NotNull ServerPlayer player, @NotNull ServerChunkCache chunkSource, long startTime, int chunkCount) {
        // Handle any exceptions that occurred during file deletion.
        if (ex != null) {
            messenger.error(player, ex.getMessage());
            ex.printStackTrace();
            return;
        }

        // A safety check in case the player disconnected or changed worlds during the operation.
        if (!player.isAddedToWorld() || player.serverLevel() != chunkSource.level) {
            messenger.error(player, "Operation cancelled due to player left the world");
            return;
        }

        // Trigger the server to notice the missing chunks and generate new ones.
        // chunkSource.move(player);
        // Force the client to discard its cached chunks and request the new ones from the server.
        // player.teleportTo(player.serverLevel(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());

        // Format the time in seconds with one decimal place
        double durationSeconds = (double) TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) / 1000.0;
        String formattedTime = String.format("%.1f", durationSeconds);

        messenger.success(player, "Regenerated " + chunkCount + " chunks in " + formattedTime + " sec");
    }
}
