package by.langvest.plantopia.util.helper;

import org.jetbrains.annotations.NotNull;

public class PlantopiaColorHelper {
	public static int fromHexColor(@NotNull String hexColor) {
		if(hexColor.startsWith("#")) {
			hexColor = hexColor.substring(1);
		}

		// LanGvest: If the color is in RRGGBB format, add FF for full opacity.
		if(hexColor.length() == 6) {
			hexColor = "FF" + hexColor;
		}

		return (int)Long.parseLong(hexColor, 16);
	}

	public static float red(int packedColor) {
		return (packedColor >> 16 & 0xFF) / 255.0F;
	}

	public static float green(int packedColor) {
		return (packedColor >> 8 & 0xFF) / 255.0F;
	}

	public static float blue(int packedColor) {
		return (packedColor & 0xFF) / 255.0F;
	}
}
