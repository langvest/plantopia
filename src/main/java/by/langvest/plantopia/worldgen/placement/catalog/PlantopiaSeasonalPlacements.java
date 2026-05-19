package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaTreeFeatures;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaRarityFilter;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class PlantopiaSeasonalPlacements extends PlantopiaPlacements {
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

	public static final ResourceKey<PlacedFeature> PATCH_ORANGE_LEAF_LITTER = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_ORANGE_LEAF_LITTER),
		getLeafLitterDeclaration(PlantopiaVegetationFeatures.PATCH_ORANGE_LEAF_LITTER, UniformInt.of(0, 2))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.SEASONAL_DARK_FOREST)
			)
	);

	/* HELPER METHODS ******************************************************/

	protected static PlantopiaPlacementDeclaration.Builder getLeafLitterDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature, IntProvider count) {
		return PlantopiaPlacementDeclaration.builder()
			.feature(feature)
			.modifiers(context -> List.of(
				CountPlacement.of(count),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			));
	}
}
