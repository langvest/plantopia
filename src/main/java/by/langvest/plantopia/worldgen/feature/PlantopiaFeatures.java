package by.langvest.plantopia.worldgen.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaFeatures {
	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		PlantopiaVegetationFeatures.bootstrap(context);
	}

	protected static @NotNull ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, plantopiaLocationFrom(name));
	}

	protected static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
		context.register(key, new ConfiguredFeature<>(feature, configuration));
	}

	protected static void register(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Feature<NoneFeatureConfiguration> feature) {
		register(context, key, feature, FeatureConfiguration.NONE);
	}
}
