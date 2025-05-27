package by.langvest.plantopia.util.helper;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaTemplateHelper {
	/* ADVANCEMENT *****************************************************************/

	@Contract(pure = true)
	public static @NotNull String getAdvancementTitleKey(String groupName, String advancementName) {
		return "advancements." + groupName + "." + advancementName + ".title";
	}

	@Contract(pure = true)
	public static @NotNull String getAdvancementDescriptionKey(String groupName, String advancementName) {
		return "advancements." + groupName + "." + advancementName + ".description";
	}

	@Contract(pure = true)
	public static @NotNull String getAdvancementBackgroundPath(String textureName) {
		return "textures/gui/advancements/backgrounds/" + textureName + ".png";
	}

	/* CREATIVE MODE TAB *****************************************************************/

	@Contract(pure = true)
	public static @NotNull String getCreativeModeTabTitleKey(String groupName) {
		return "itemGroup." + groupName;
	}
}
