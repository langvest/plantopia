package by.langvest.plantopia.command;

import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PlantopiaMessenger {
    private static final Map<String, PlantopiaMessenger> CACHE = Maps.newHashMap();
    public static final PlantopiaMessenger DEFAULT = new PlantopiaMessenger();

    private final @Nullable String name;

    private PlantopiaMessenger() {
        this.name = null;
    }

    private PlantopiaMessenger(@Nullable String name) {
        this.name = name;
    }

    public static synchronized PlantopiaMessenger getByName(String name) {
        return CACHE.computeIfAbsent(name, PlantopiaMessenger::new);
    }

    public static PlantopiaMessenger getDefaultInstance() {
        return DEFAULT;
    }

    public @Nullable String getName() {
        return name;
    }

    public String getPrefix() {
        String prefix = "Plantopia";

        if (name != null) {
            prefix += " " + name;
        }

        return "[" + prefix + "]: ";
    }

    public MutableComponent getPrefixComponent() {
        return Component.literal(getPrefix()).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD);
    }

    public void success(@NotNull ServerPlayer player, String message) {
        sendMessage(player, Component.literal(message).withStyle(ChatFormatting.GREEN));
    }

    public void info(@NotNull ServerPlayer player, String message) {
        sendMessage(player, Component.literal(message).withStyle(ChatFormatting.WHITE));
    }

    public void error(@NotNull ServerPlayer player, String message) {
        sendMessage(player, Component.literal(message).withStyle(ChatFormatting.RED));
    }

    public void warning(@NotNull ServerPlayer player, String message) {
        sendMessage(player, Component.literal(message).withStyle(ChatFormatting.GOLD));
    }

    private void sendMessage(@NotNull ServerPlayer player, @NotNull Component message) {
        player.sendSystemMessage(getPrefixComponent().copy().append(message.copy().withStyle(style -> style.withBold(false))));
    }
}
