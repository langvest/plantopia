package by.langvest.plantopia.util.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class PlantopiaLogHelper {
	public static void logChat(String message) {
		logChat(Component.literal(message));
	}

	public static void logChat(Component message) {
		var player = Minecraft.getInstance().player;

		if(player == null) return;

		player.displayClientMessage(message, false);
	}
}
