package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaHeightRangeFilter;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseCountPlacement;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseFilter;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaRarityFilter;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class PlantopiaVegetationPlacements extends PlantopiaPlacements {
	protected static final PlantopiaHeightRangeFilter WATER_PLANT_HIGH_RANGE_FILTER = PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(62), VerticalAnchor.TOP);

	private static final Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> declarations = Maps.newHashMap();

	public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
		return declarations;
	}

	private static @NotNull ResourceKey<PlacedFeature> declarePlacedFeature(String name, PlantopiaPlacedFeatureDeclaration.@NotNull Builder builder) {
		var key = createKey(name);
		declarations.put(key, builder.build());
		return key;
	}

	/* VEGETATION PLACEMENTS ******************************************/

	public static final ResourceKey<PlacedFeature> HOGWEED_BONEMEAL = declarePlacedFeature(
		compileNameFrom(PlantopiaBlocks.HOGWEED, BONEMEAL),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SINGLE_HOGWEED)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(20.0F),
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get())
			))
	);

	public static final ResourceKey<PlacedFeature> HOGWEED_INFESTED_GRASS_BLOCK = declarePlacedFeature(
		compileNameFrom(PlantopiaBlocks.HOGWEED, PlantopiaBlocks.INFESTED_GRASS_BLOCK),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SINGLE_HOGWEED)
			.modifiers(context -> List.of(
				BlockPredicateFilter.forPredicate(
					BlockPredicate.allOf(
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get())
					)
				)
			))
	);

	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_FIREWEED, MOUNTAIN),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FIREWEED)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(0.614D, 501, 402);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.102D, 274, 148);
				float bigNoiseLevel = -0.815F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.belowLevel(bigNoiseConfig, bigNoiseLevel, 25),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.belowLevel(bigNoiseConfig, bigNoiseLevel, 0.1F),
					PlantopiaNoiseFilter.aboveLevel(smallNoiseConfig, smallNoiseLevel, 0.15F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(86), VerticalAnchor.TOP),
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
				);
			})
			.biomes(tagSet -> tagSet
				.apply(PlantopiaPlacements::addMountainBiomes)
				.add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN_2 = declarePlacedFeature(
		compileNameFrom(PATCH_FIREWEED_MOUNTAIN, 2),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FIREWEED)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(1.862D, 528, 811);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.112D, 332, 643);
				float bigNoiseLevel = -0.6F;
				float smallNoiseLevel = 0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.belowLevel(bigNoiseConfig, bigNoiseLevel, 17),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.belowLevel(bigNoiseConfig, bigNoiseLevel, 0.18F),
					PlantopiaNoiseFilter.aboveLevel(smallNoiseConfig, smallNoiseLevel, 0.12F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(122), VerticalAnchor.TOP),
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
				);
			})
			.biomes(tagSet -> tagSet
				.apply(PlantopiaPlacements::addMountainBiomes)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_TINY_CACTUS = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS_ON_SAND),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS_ON_SAND)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(3.82F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(tagSet -> tagSet
				.add(Biomes.DESERT)
				.addTag(BiomeTags.IS_BADLANDS)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_REEDS = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_REEDS),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_REEDS)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(3.62F),
				CountPlacement.of(UniformInt.of(1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_HIGH_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.matchesFluids(Fluids.WATER)
				)
			))
			.biomes(tagSet -> tagSet
				.apply(PlantopiaPlacements::addOldGrowthBiomes)
				.apply(PlantopiaPlacements::addSwampBiomes)
				.add(Biomes.SAVANNA)
				.add(Biomes.RIVER)
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
				.add(Biomes.BEACH, Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.TAIGA)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_REEDS_SNOWY = declarePlacedFeature(
		compileNameFrom(PATCH_REEDS, SNOWY),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_REEDS)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(2.12F),
				CountPlacement.of(UniformInt.of(1, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_HIGH_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.anyOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesBlocks(Blocks.ICE)
					)
				)
			))
			.biomes(tagSet -> tagSet
				.add(Biomes.FROZEN_RIVER)
				.add(Biomes.SNOWY_PLAINS)
				.add(Biomes.GROVE, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CATTAIL = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CATTAIL),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_CATTAIL)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(5.52F),
				CountPlacement.of(ClampedInt.of(UniformInt.of(1, 3), 2, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_HIGH_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.allOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT)
					)
				)
			))
			.biomes(tagSet -> tagSet
				.apply(PlantopiaPlacements::addOldGrowthBiomes)
				.add(Biomes.RIVER)
				.add(Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.TAIGA)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CATTAIL_SWAMP = declarePlacedFeature(
		compileNameFrom(PATCH_CATTAIL, SWAMP),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_CATTAIL)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(2.32F),
				CountPlacement.of(UniformInt.of(1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_HIGH_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.matchesFluids(Fluids.WATER)
				)
			))
			.biomes(tagSet -> tagSet
				.apply(PlantopiaPlacements::addSwampBiomes)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_DUNE_GRASS = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_DUNE_GRASS),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_DUNE_GRASS)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(2.92F),
				CountPlacement.of(ClampedInt.of(UniformInt.of(1, 4), 2, 4)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(tagSet -> tagSet
				.add(Biomes.BEACH)
			)
	);
}
