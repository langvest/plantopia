package by.langvest.plantopia.neoforge.datagen.extra;

import by.langvest.plantopia.Plantopia;
import com.google.common.hash.Hashing;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class PlantopiaForgeBiomeModifierProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;

    public PlantopiaForgeBiomeModifierProvider(PackOutput output) {
        this.output = output;
        this.modId = Plantopia.MOD_ID;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        return CompletableFuture.runAsync(() -> {
            try {
                processBiomeModifiers(cache);
            } catch (IOException e) {
                throw new RuntimeException("Error processing biome modifiers", e);
            }
        });
    }

    private void processBiomeModifiers(@NotNull CachedOutput cache) throws IOException {
        Path neoforgePath = output.getOutputFolder().resolve("data/" + modId + "/neoforge/biome_modifier");
        Path forgePath = output.getOutputFolder().resolve("data/" + modId + "/forge/biome_modifier");

        deleteOutdatedFiles(neoforgePath, forgePath);

        if (!Files.exists(neoforgePath)) return;

        Files.createDirectories(forgePath);
        copyAndModifyFiles(neoforgePath, forgePath, cache);
    }

    private void deleteOutdatedFiles(Path neoforgeDir, Path forgeDir) throws IOException {
        if (!Files.exists(forgeDir)) {
            return;
        }

        try (Stream<Path> stream = Files.walk(forgeDir)) {
            stream.filter(Files::isRegularFile).forEach(forgeFile -> {
                try {
                    Path relativePath = forgeDir.relativize(forgeFile);
                    Path neoforgeFile = neoforgeDir.resolve(relativePath);
                    if (!Files.exists(neoforgeFile)) {
                        Files.delete(forgeFile);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete outdated file: " + forgeFile, e);
                }
            });
        }
    }

    private void copyAndModifyFiles(Path sourceDir, Path targetDir, @NotNull CachedOutput cache) throws IOException {
        try (Stream<Path> stream = Files.walk(sourceDir)) {
            stream.filter(Files::isRegularFile).forEach(sourceFile -> {
                try {
                    processSingleFile(sourceFile, sourceDir, targetDir, cache);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process file: " + sourceFile, e);
                }
            });
        }
    }

    private void processSingleFile(Path sourceFile, @NotNull Path sourceDir, @NotNull Path targetDir, @NotNull CachedOutput cache) throws IOException {
        String content = Files.readString(sourceFile);
        String modifiedContent = content.replaceAll("neoforge:", "forge:");

        Path relativePath = sourceDir.relativize(sourceFile);
        Path targetFile = targetDir.resolve(relativePath);

        Files.createDirectories(targetFile.getParent());
        saveFileIfNeeded(cache, targetFile, modifiedContent.getBytes());
    }

    private void saveFileIfNeeded(@NotNull CachedOutput cache, Path path, byte[] data) throws IOException {
        var hash = Hashing.sha256().hashBytes(data);
        cache.writeIfNeeded(path, data, hash);
    }

    @Override
    public @NotNull String getName() {
        return "PlantopiaForgeBiomeModifier";
    }
}
