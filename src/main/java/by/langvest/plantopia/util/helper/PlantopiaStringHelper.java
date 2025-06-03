package by.langvest.plantopia.util.helper;

import org.jetbrains.annotations.NotNull;

public final class PlantopiaStringHelper {
	public static @NotNull String capitalize(@NotNull String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1);
	}

	public static @NotNull String toCamelCase(@NotNull String text) {
		String[] words = text.split("[\\W_]+");
		StringBuilder builder = new StringBuilder();

		for(int i = 0; i < words.length; i++) {
			String word = words[i];

			if(i == 0) {
				word = word.isEmpty() ? word : word.toLowerCase();
			} else {
				word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
			}

			builder.append(word);
		}

		return builder.toString();
	}
}
