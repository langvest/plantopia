package by.langvest.plantopia.neoforge.datagen.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class PlantopiaJsonReindentProvider implements DataProvider {
    // This GSON instance produces a predictably formatted JSON string with 2-space indents.
    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();

    private final PackOutput output;

    public PlantopiaJsonReindentProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        Path root = output.getOutputFolder();

        return CompletableFuture.runAsync(() -> {
            try (Stream<Path> stream = Files.walk(root)) {
                stream
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(path -> reindentFile(root, path));
            } catch (IOException e) {
                throw new RuntimeException("Failed to walk path: " + root, e);
            }
        });
    }

    private void reindentFile(Path root, Path path) {
        try {
            String raw = Files.readString(path);
            String updated = reindentJsonString(raw);

            if (!raw.equals(updated)) {
                Files.writeString(path, updated, StandardCharsets.UTF_8);
                logReindent(root, path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to reindent " + path, e);
        }
    }

    private void logReindent(@NotNull Path root, @NotNull Path path) {
        Path relativePath = root.relativize(path);

        // ANSI cyan
        String CYAN = "\u001B[36m";
        String RESET = "\u001B[0m";

        System.out.println(CYAN + "[Reindent]" + RESET + " " + relativePath.toString().replace("\\", "/"));
    }


    private @NotNull String reindentJsonString(@NotNull String text) {
        JsonElement jsonElement = JsonParser.parseString(text);
        String twoSpaceIndented = GSON.toJson(jsonElement);

        String[] lines = twoSpaceIndented.split("\n");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            int leadingSpaces = 0;
            while (leadingSpaces < line.length() && line.charAt(leadingSpaces) == ' ') {
                leadingSpaces++;
            }

            // The pretty printer uses 2 spaces per indentation level.
            // We want 4 spaces, so we double the number of leading spaces.
            int newIndentSize = leadingSpaces * 2;

            builder.append(" ".repeat(newIndentSize));
            builder.append(line.substring(leadingSpaces));

            if (i < lines.length - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    @Override
    public @NotNull String getName() {
        return "PlantopiaJsonReindent";
    }
}
