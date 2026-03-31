package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class PlantopiaDebugHelper {
    public static void logChat(@NotNull Object object) {
        if (Plantopia.getPlatform().isClient()) {
            logChat(Component.literal(object.toString()));
        }
    }

    public static void logConsole(String message) {
        var logger = Plantopia.getPlatform().getLogger();
        logger.log(org.apache.logging.log4j.Level.INFO, message);
    }

    public static void logConsole(@NotNull Object object) {
        logConsole(object.toString());
    }

    public static void logChat(Component message) {
        if (Plantopia.getPlatform().isClient()) {
            var player = net.minecraft.client.Minecraft.getInstance().player;
            if (player != null) {
                player.displayClientMessage(message, false);
            }
        }
    }

    public static void logInWorld(Level level, BlockPos pos, @NotNull String message) {
        if (message.length() <= 100) {
            placeSign(level, pos, message);
        } else {
            placeBookInFrame(level, pos, message);
        }
    }

    public static void placeSign(LevelAccessor level, BlockPos pos, String message) {
        placeSign(level, pos, message, Blocks.OAK_SIGN);
    }

    public static void placeSign(@NotNull LevelAccessor level, BlockPos pos, String message, @NotNull Block signBlock) {
        if (level.isClientSide()) {
            return;
        }

        BlockPos supportPos = pos.below();
        var supportState = level.getBlockState(supportPos);
        if (!supportState.is(BlockTags.SIGNS) || !supportState.isFaceSturdy(level, supportPos, Direction.UP, SupportType.CENTER)) {
            level.setBlock(supportPos, Blocks.YELLOW_WOOL.defaultBlockState(), Block.UPDATE_CLIENTS); // Changed to UPDATE_CLIENTS
        }

        level.setBlock(pos, signBlock.defaultBlockState(), Block.UPDATE_CLIENTS); // Changed to UPDATE_CLIENTS

        BlockEntity be = level.getBlockEntity(pos);
        if (be != null) {
            CompoundTag nbt = new CompoundTag();
            List<String> lines = splitText(message, 25);

            // Front Text NBT
            CompoundTag frontTextNbt = new CompoundTag();
            ListTag frontMessages = new ListTag();
            for (int i = 0; i < 4; i++) {
                String line = i < lines.size() ? lines.get(i) : "";
                frontMessages.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(line))));
            }
            frontTextNbt.put("messages", frontMessages);
            frontTextNbt.putString("color", "black"); // Default color, can be customized if needed

            // Back Text NBT (same as front for this debug helper)
            CompoundTag backTextNbt = (CompoundTag) frontTextNbt.copy();

            nbt.put("front_text", frontTextNbt);
            nbt.put("back_text", backTextNbt);

            // Load the NBT data into the BlockEntity
            be.load(nbt);
        } else {
            LogManager.getLogger().warn("Failed to get BlockEntity for sign at {} during worldgen. Sign text will not be set.", pos);
        }
    }

    public static void placeBookInFrame(@NotNull Level level, BlockPos pos, String message) {
        if (level.isClientSide()) {
            return;
        }

        BlockPos supportPos = pos.below();
        if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP)) {
            level.setBlock(supportPos, Blocks.YELLOW_WOOL.defaultBlockState(), Block.UPDATE_CLIENTS);
        }

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);

        ItemStack bookStack = new ItemStack(Items.WRITTEN_BOOK);
        CompoundTag bookNbt = new CompoundTag();
        bookNbt.putString("author", "Plantopia Debug");
        bookNbt.putString("title", "Log");

        List<String> lines = splitText(message, 35);
        final int linesPerPage = 14;
        final ListTag pagesTag = new ListTag();
        for (int i = 0; i < lines.size(); i += linesPerPage) {
            int end = Math.min(i + linesPerPage, lines.size());
            List<String> pageLines = lines.subList(i, end);
            String pageText = String.join("\n", pageLines);
            pagesTag.add(StringTag.valueOf(Component.Serializer.toJson(Component.literal(pageText))));
        }
        bookNbt.put("pages", pagesTag);
        bookStack.setTag(bookNbt);

        ItemFrame frame = new ItemFrame(level, pos, Direction.UP);
        frame.setItem(bookStack);
        level.addFreshEntity(frame);
    }

    private static List<String> splitText(@NotNull String text, int maxLineLength) {
        return Arrays.stream(text.split("\n"))
            .flatMap(line -> wrapLine(line, maxLineLength).stream())
            .collect(Collectors.toList());
    }

    private static @NotNull List<String> wrapLine(@NotNull String line, int maxLineLength) {
        List<String> wrappedLines = new ArrayList<>();
        String[] words = line.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxLineLength && !currentLine.isEmpty()) {
                wrappedLines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            if (!currentLine.isEmpty()) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }
        if (!currentLine.isEmpty()) {
            wrappedLines.add(currentLine.toString());
        }
        return wrappedLines;
    }
}
