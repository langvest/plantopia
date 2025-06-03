package by.langvest.plantopia.worldgen.feature;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatures.createKey;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatures.register;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class PlantopiaVegetationFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_HOGWEED = createKey("single_hogweed");

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		register(context, SINGLE_HOGWEED, PlantopiaFeatureTypes.HOGWEED.get());
	}
}
