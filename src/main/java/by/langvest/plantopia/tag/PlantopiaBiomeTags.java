package by.langvest.plantopia.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBiomeTags {
	public static final TagKey<Biome> ALLOWS_QUAGMIRE = createBiomeTag("allows_quagmire");
	public static final TagKey<Biome> ALLOWS_FRAZIL = createBiomeTag("allows_frazil");
	public static final TagKey<Biome> IS_MARSH = createBiomeTag("is_marsh");

	public static @NotNull TagKey<Biome> createBiomeTag(String name) {
		return TagKey.create(Registries.BIOME, plantopia(name));
	}

	public static @NotNull TagKey<Biome> createBiomeHasFeatureTag(String featureName) {
		return TagKey.create(Registries.BIOME, plantopia("has_feature", featureName));
	}
}