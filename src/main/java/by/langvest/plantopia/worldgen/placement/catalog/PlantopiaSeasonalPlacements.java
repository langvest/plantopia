package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaTreeFeatures;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public final class PlantopiaSeasonalPlacements {
	public static final Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

	public static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
		return DECLARATION.add(createKey(name), builder.build()).getKey();
	}

	/* MARSH PLACEMENTS ******************************************/

	public static final ResourceKey<PlacedFeature> SEASONAL_DARK_OAK_CHECKED = declarePlacement(
		compileNameFrom(PlantopiaTreeFeatures.SEASONAL_DARK_OAK, CHECKED),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaTreeFeatures.SEASONAL_DARK_OAK)
			.modifiers(context -> List.of(
				PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING)
			))
	);

	public static final ResourceKey<PlacedFeature> SEASONAL_DARK_OAK_LITTER_055 = declarePlacement(
		compileNameFrom(PlantopiaTreeFeatures.SEASONAL_DARK_OAK_LITTER_055),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaTreeFeatures.SEASONAL_DARK_OAK_LITTER_055)
			.modifiers(context -> List.of(
				PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING)
			))
	);

	public static final ResourceKey<PlacedFeature> SEASONAL_DARK_FOREST_VEGETATION = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.SEASONAL_DARK_FOREST_VEGETATION),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SEASONAL_DARK_FOREST_VEGETATION)
			.modifiers(context -> List.of(
				CountPlacement.of(16),
				InSquarePlacement.spread(),
				SurfaceWaterDepthFilter.forMaxDepth(0),
				PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.SEASONAL_DARK_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> TREES_SEASONAL_FOREST = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.TREES_SEASONAL_FOREST),
		getTreeDeclaration(PlantopiaVegetationFeatures.TREES_SEASONAL_FOREST, PlacementUtils.countExtra(10, 0.1F, 1))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.SEASONAL_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_YELLOW_LEAF_LITTER_CHECKED = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_YELLOW_LEAF_LITTER, CHECKED),
		getCheckedLeafLitterDeclaration(PlantopiaVegetationFeatures.PATCH_YELLOW_LEAF_LITTER)
	);

	public static final ResourceKey<PlacedFeature> PATCH_ORANGE_LEAF_LITTER_CHECKED = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_ORANGE_LEAF_LITTER, CHECKED),
		getCheckedLeafLitterDeclaration(PlantopiaVegetationFeatures.PATCH_ORANGE_LEAF_LITTER)
	);

	public static final ResourceKey<PlacedFeature> PATCH_RED_LEAF_LITTER_CHECKED = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_RED_LEAF_LITTER, CHECKED),
		getCheckedLeafLitterDeclaration(PlantopiaVegetationFeatures.PATCH_RED_LEAF_LITTER)
	);

	/* HELPER METHODS ***********************************************************/

	public static PlantopiaPlacementDeclaration.Builder getTreeDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier modifier) {
		return PlantopiaPlacementDeclaration.builder()
			.feature(feature)
			.modifiers(context -> List.of(
				modifier,
				InSquarePlacement.spread(),
				TREE_THRESHOLD,
				PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
				BiomeFilter.biome()
			));
	}

	public static PlantopiaPlacementDeclaration.Builder getCheckedLeafLitterDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature) {
		return PlantopiaPlacementDeclaration.builder()
			.feature(feature)
			.modifiers(context -> List.of(
				RandomOffsetPlacement.horizontal(weightedListInt(values -> values
					.add(UniformInt.of(-3, 3), 2)
					.add(UniformInt.of(-2, 2), 5)
				)),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE
			));
	}
}
