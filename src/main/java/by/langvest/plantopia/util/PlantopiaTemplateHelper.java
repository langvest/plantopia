package by.langvest.plantopia.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaTemplateHelper {
	@Contract(pure = true)
	public static @NotNull String advancementTitle(String group, String name) {
		return "advancements." + group + "." + name + ".title";
	}

	@Contract(pure = true)
	public static @NotNull String advancementDescription(String group, String name) {
		return "advancements." + group + "." + name + ".description";
	}

	@Contract(pure = true)
	public static @NotNull String advancementBackground(String name) {
		return "textures/gui/advancements/backgrounds/" + name + ".png";
	}

	@Contract(pure = true)
	public static @NotNull String creativeModeTabTitle(String name) {
		return "itemGroup." + name;
	}
}