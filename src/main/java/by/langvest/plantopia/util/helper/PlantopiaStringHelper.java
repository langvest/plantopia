package by.langvest.plantopia.util.helper;

import org.jetbrains.annotations.NotNull;

public final class PlantopiaStringHelper {
	public static @NotNull String capitalize(@NotNull String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}
}