package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.config.*;
import by.langvest.plantopia.worldgen.feature.special.*;
import by.langvest.plantopia.worldgen.feature.tree.PlantopiaHugeWitchyToadstoolFeature;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaFeatureTypes {
	public static final RegistryObject<PlantopiaNaturalBlockFeature> NATURAL_BLOCK = registerFeatureType("natural_block", () -> new PlantopiaNaturalBlockFeature(SimpleBlockConfiguration.CODEC));
	public static final RegistryObject<PlantopiaNaturalBlockColumnFeature> NATURAL_BLOCK_COLUMN = registerFeatureType("natural_block_column", () -> new PlantopiaNaturalBlockColumnFeature(BlockColumnConfiguration.CODEC));
	public static final RegistryObject<PlantopiaSeaHangingMossPatchFeature> SEA_HANGING_MOSS_PATCH = registerFeatureType("sea_hanging_moss_patch", () -> new PlantopiaSeaHangingMossPatchFeature(PlantopiaSeaHangingMossPatchConfiguration.CODEC));
	public static final RegistryObject<PlantopiaRadialPatchFeature> RADIAL_PATCH = registerFeatureType("radial_patch", () -> new PlantopiaRadialPatchFeature(PlantopiaRadialPatchConfiguration.CODEC));
	public static final RegistryObject<PlantopiaLimitedRandomPatchFeature> LIMITED_RANDOM_PATCH = registerFeatureType("limited_random_patch", () -> new PlantopiaLimitedRandomPatchFeature(PlantopiaLimitedRandomPatchConfiguration.CODEC));
	public static final RegistryObject<PlantopiaPitFeature> PIT = registerFeatureType("pit", () -> new PlantopiaPitFeature(PlantopiaPitConfiguration.CODEC));
	public static final RegistryObject<PlantopiaBranchingShrubPatchFeature> BRANCHING_SHRUB_PATCH = registerFeatureType("branching_shrub_patch", () -> new PlantopiaBranchingShrubPatchFeature(PlantopiaBranchingShrubPatchConfiguration.CODEC));
	public static final RegistryObject<PlantopiaVegetationPatchFeature> VEGETATION_PATCH = registerFeatureType("vegetation_patch", () -> new PlantopiaVegetationPatchFeature(PlantopiaVegetationPatchConfiguration.CODEC));
	public static final RegistryObject<PlantopiaHugeWitchyToadstoolFeature> HUGE_WITCHY_TOADSTOOL = registerFeatureType("huge_witchy_toadstool", () -> new PlantopiaHugeWitchyToadstoolFeature(HugeMushroomFeatureConfiguration.CODEC));
	public static final RegistryObject<PlantopiaPoiAnchorFeature> POI_ANCHOR = registerFeatureType("poi_anchor", () -> new PlantopiaPoiAnchorFeature(PlantopiaPoiAnchorConfiguration.CODEC));

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
