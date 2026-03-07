package by.langvest.plantopia.util.helper;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public final class PlantopiaColorHelper {
	public static int hexToInt(@NotNull String hexColor) {
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

	public static int hsbToInt(float hue, float saturation, float brightness) {
		return Color.HSBtoRGB(hue, saturation, brightness) & 0x00ffffff;
	}

	public static float @NotNull [] intToHsb(int packedColor) {
		return Color.RGBtoHSB(
			(int)(red(packedColor) * 250.0F),
			(int)(green(packedColor) * 250.0F),
			(int)(blue(packedColor) * 250.0F),
			null
		);
	}
}
