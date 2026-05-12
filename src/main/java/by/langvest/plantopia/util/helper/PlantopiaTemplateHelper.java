package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import net.minecraft.resources.ResourceLocation;
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
	public static @NotNull String getCreativeModeTabTitleKey(@NotNull ResourceLocation identifier) {
		return "itemGroup." + identifier.getNamespace() + "." + identifier.getPath();
	}

	/* DAMAGE TYPE *****************************************************************/

	@Contract(pure = true)
	public static @NotNull String getDamageTypeTitleKey(String messageId) {
		return getDamageTypeTitleKey(messageId, null);
	}

	@Contract(pure = true)
	public static @NotNull String getDamageTypeTitleKey(String messageId, String qualifier) {
		String key = "death.attack." + messageId;

		if(qualifier != null) key += "." + qualifier;

		return key;
	}

	/* ENTITY TYPE *****************************************************************/

	@Contract(pure = true)
	public static @NotNull String getEntityTypeTitleKey(@NotNull ResourceLocation identifier) {
		return "entity." + identifier.getNamespace() + "." + identifier.getPath();
	}

	/* SOUND EVENT *******************************************************************/

	@Contract(pure = true)
	public static @NotNull String getSoundEventSubtitleKey(String name) {
		return "subtitles." + Plantopia.MOD_ID + "." + name;
	}
}
