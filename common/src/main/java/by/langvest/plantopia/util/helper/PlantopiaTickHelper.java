package by.langvest.plantopia.util.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public final class PlantopiaTickHelper {
    private static int clientTick = 0;
    private static int inGameTick = 0;

    public static int getClientTick() {
        return clientTick;
    }

    public static int getInGameTick() {
        return inGameTick;
    }

    public static int getPlayerTick() {
        var instance = Minecraft.getInstance();

        return Objects.requireNonNull(instance.player).tickCount;
    }

    public static void tick() {
        clientTick++;

        var instance = Minecraft.getInstance();
        boolean isPaused = instance.screen instanceof PauseScreen && instance.isPaused();

        if (!isPaused) inGameTick++;
    }
}
