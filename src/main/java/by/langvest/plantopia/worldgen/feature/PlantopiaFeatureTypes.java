package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaRadialPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaRadialPatchFeature;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaNaturalBlockFeature;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaFeatureTypes {
	public static final RegistryObject<PlantopiaNaturalBlockFeature> NATURAL_BLOCK = registerFeatureType("natural_block", () -> new PlantopiaNaturalBlockFeature(SimpleBlockConfiguration.CODEC));
	public static final RegistryObject<PlantopiaRadialPatchFeature> RADIAL_PATCH = registerFeatureType("radial_patch", () -> new PlantopiaRadialPatchFeature(PlantopiaRadialPatchConfiguration.CODEC));

	private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> registerFeatureType(String name, Supplier<F> supplier) {
		return registerFeatureType(plantopia(name), supplier);
	}

	private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> registerFeatureType(ResourceLocation identifier, Supplier<F> supplier) {
		return PlantopiaRegistries.FEATURE_TYPE.register(identifier, supplier);
	}

	public static void setup(@NotNull RegisterEvent event) {
		event.registerAll(Registries.FEATURE, PlantopiaRegistries.FEATURE_TYPE);
	}
}
