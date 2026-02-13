package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.*;

public final class PlantopiaDebugHelper {
	public static void logCoords(@NotNull BlockPos pos) {
		logCoords(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
	}

	public static void logCoords(@NotNull Vec3 vec3) {
		logCoords(vec3.x, vec3.y, vec3.z);
	}

	public static void logCoords(double x, double y, double z) {
		var player = Minecraft.getInstance().player;

		if(player == null) return;

		for(int i = 0; i < 5; i++) {
			player.level().addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}

	public static void logColor(int packedColor) {
		var hsb = PlantopiaColorHelper.rgbToHsb(packedColor);
		PlantopiaDebugHelper.logChat("=====> Color: " + packedColor + " <=====");
		PlantopiaDebugHelper.logChat("R: " + red(packedColor) + "; G: " + green(packedColor) + "; B: " + blue(packedColor));
		PlantopiaDebugHelper.logChat("H: " + hsb[0] + "; S: " + hsb[1] + "; B: " + hsb[2]);
	}

	public static void logChat(@NotNull Object object) {
		logChat(Component.literal(object.toString()));
	}

	public static void logConsole(String message) {
		var logger = Plantopia.getPlatform().getLogger();
		logger.log(Level.INFO, message);
	}

	public static void logConsole(@NotNull Object object) {
		logConsole(object.toString());
	}

	public static void logChat(Component message) {
		var player = Minecraft.getInstance().player;

		if(player == null) return;

		player.displayClientMessage(message, false);
	}
}
