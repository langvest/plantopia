package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaHogweedFeature;
import by.langvest.toolkit.event.RegistryEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaFeatureTypes {
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> HOGWEED = registerFeature("hogweed", () -> new PlantopiaHogweedFeature(NoneFeatureConfiguration.CODEC));

	private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> registerFeature(String name, Supplier<F> supplier) {
		return registerFeature(plantopia(name), supplier);
	}

	private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> registerFeature(ResourceLocation identifier, Supplier<F> supplier) {
		return PlantopiaRegistries.FEATURE_TYPE.register(identifier, supplier);
	}

	public static void setup(@NotNull RegistryEvent event) {
		event.registerAll(Registries.FEATURE, PlantopiaRegistries.FEATURE_TYPE);
	}
}
