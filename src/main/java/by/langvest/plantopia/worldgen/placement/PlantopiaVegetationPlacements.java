package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.biome.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.feature.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
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
	protected static final PlantopiaRangeFilter WATER_PLANT_RANGE_FILTER = PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel(-1));

	private static final Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> declarations = Maps.newHashMap();

	public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
		return declarations;
	}

	private static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacedFeatureDeclaration.@NotNull Builder builder) {
		var key = createKey(name);
		declarations.put(key, builder.build());
		return key;
	}

	/* VEGETATION PLACEMENTS ******************************************/

	public static final ResourceKey<PlacedFeature> HOGWEED_BONEMEAL = declarePlacement(
		compileNameFrom(PlantopiaBlocks.HOGWEED, BONEMEAL),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SINGLE_HOGWEED)
			.modifiers(context -> List.of(
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get())
			))
	);

	public static final ResourceKey<PlacedFeature> PATCH_HOGWEED = declarePlacement(
		compileNameFrom("patch", PlantopiaBlocks.HOGWEED),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SINGLE_HOGWEED)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(380.0F),
				CountPlacement.of(UniformInt.of(1, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome(),
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get())
			))
			.biomes(biomes -> biomes
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
				.addTag(BiomeTags.IS_SAVANNA)
				.apply(PlantopiaPlacements::addMountainBiomes)
			)
	);

	public static final ResourceKey<PlacedFeature> HOGWEED_INFESTED_GRASS_BLOCK = declarePlacement(
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

	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_FIREWEED_MOUNTAIN),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FIREWEED_MOUNTAIN)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(0.614D, 501, 402);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.102D, 274, 148);
				float bigNoiseLevel = -0.815F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 22),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.1F),
					PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.15F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.absolute(86)),
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
				);
			})
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addMountainBiomes)
				.add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN_2 = declarePlacement(
		compileNameFrom(PATCH_FIREWEED_MOUNTAIN, 2),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FIREWEED_MOUNTAIN)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(1.862D, 528, 811);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.112D, 332, 643);
				float bigNoiseLevel = -0.6F;
				float smallNoiseLevel = 0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 17),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.18F),
					PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.12F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.absolute(122)),
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
				);
			})
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addMountainBiomes)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_TINY_CACTUS = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS_ON_SAND),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS_ON_SAND)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(3.82F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.DESERT)
				.addTag(BiomeTags.IS_BADLANDS)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_REED = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_REED),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_REED)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(3.12F, 4.12F),
				CountPlacement.of(UniformInt.of(1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.matchesFluids(Fluids.WATER)
				)
			))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaOldGrowthBiomes)
				.apply(PlantopiaPlacements::addVanillaSwampBiomes)
				.apply(PlantopiaPlacements::addCascadesBiomes)
				.add(Biomes.SAVANNA)
				.add(Biomes.RIVER)
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
				.add(Biomes.BEACH, Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.TAIGA)
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_REED_SNOWY = declarePlacement(
		compileNameFrom(PATCH_REED, SNOWY),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_REED)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(2.0F),
				CountPlacement.of(UniformInt.of(1, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.anyOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesBlocks(Blocks.ICE)
					)
				)
			))
			.biomes(biomes -> biomes
				.add(Biomes.FROZEN_RIVER)
				.add(Biomes.SNOWY_PLAINS)
				.add(Biomes.GROVE, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_REED_MARSH = declarePlacement(
		compileNameFrom(PATCH_REED, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_REED)
			.modifiers(context -> List.of(
				CountPlacement.of(4),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.anyOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesBlocks(Blocks.ICE)
					)
				)
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_AZURE_BLUET_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_AZURE_BLUET, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_AZURE_BLUET)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(6),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CHICORY = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CHICORY),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_CHICORY)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(28.42F, 32.86F),
				CountPlacement.of(1),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_TANSY = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_TANSY),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_TANSY)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(6.42F, 10.86F),
				CountPlacement.of(ClampedInt.of(UniformInt.of(0, 3), 1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_TANSY_2 = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_TANSY, 2),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_TANSY)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(6.12F),
				CountPlacement.of(ClampedInt.of(UniformInt.of(0, 3), 1, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.WINDSWEPT_FOREST)
			)
	);

//	public static final ResourceKey<PlacedFeature> PATCH_CARROTWEED = declarePlacedFeature(
//		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CARROTWEED),
//		PlantopiaPlacedFeatureDeclaration.builder()
//			.feature(PlantopiaVegetationFeatures.PATCH_CARROTWEED)
//			.modifiers(context -> List.of(
//				PlantopiaRarityFilter.onAverageOnceEvery(10.24F),
//				CountPlacement.of(ClampedInt.of(UniformInt.of(0, 3), 1, 3)),
//				InSquarePlacement.spread(),
//				PlacementUtils.HEIGHTMAP,
//				BiomeFilter.biome()
//			))
//			.biomes(biomes -> biomes
//				.add(Biomes.TAIGA)
//			)
//	);

	public static final ResourceKey<PlacedFeature> PATCH_CARROTWEED_MOUNTAIN = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CARROTWEED_MOUNTAIN),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_CARROTWEED_MOUNTAIN)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(0.158D, 188, 398);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.0684D, 68, 834);
				float bigNoiseLevel = -0.426F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 22),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.15F),
					PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.2F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.CARROTWEED.get())
				);
			})
			.biomes(biomes -> biomes
				.add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_GRASS_BONUS = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_GRASS, BONUS),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(VegetationFeatures.PATCH_GRASS)
			.modifiers(context -> List.of(
				CountPlacement.of(ClampedInt.of(UniformInt.of(1, 3), 2, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS)
				.add(Biomes.DARK_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_MARSH = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_SUGAR_CANE, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(VegetationFeatures.PATCH_SUGAR_CANE)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(5.24F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_GRASS_MARSH = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_GRASS, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(VegetationFeatures.PATCH_GRASS)
			.modifiers(context -> List.of(
				CountPlacement.of(12),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.addTag(PlantopiaBiomeTags.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN_MARSH = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_PUMPKIN, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(VegetationFeatures.PATCH_PUMPKIN)
			.modifiers(context -> List.of(
				RarityFilter.onAverageOnceEvery(80),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_GIANT_GRASS_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_GIANT_GRASS, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_GIANT_GRASS)
			.modifiers(context -> List.of(
				CountPlacement.of(UniformInt.of(0, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.addTag(PlantopiaBiomeTags.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_TAIGA_GRASS_BONUS = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_TAIGA_GRASS, BONUS),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(VegetationFeatures.PATCH_TAIGA_GRASS)
			.modifiers(context -> List.of(
				CountPlacement.of(ClampedInt.of(UniformInt.of(1, 3), 2, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.WINDSWEPT_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_FERN_BONUS = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_FERN, BONUS),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FERN)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(5.0F),
				CountPlacement.of(1),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.FOREST)
				.add(Biomes.SWAMP)
				.add(Biomes.OLD_GROWTH_BIRCH_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_LARGE_FERN_BONUS = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_LARGE_FERN, BONUS),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(VegetationFeatures.PATCH_LARGE_FERN)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(5.0F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.DARK_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CATTAIL = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CATTAIL),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_CATTAIL)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(5.0F),
				CountPlacement.of(ClampedInt.of(UniformInt.of(1, 3), 2, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.allOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT)
					)
				)
			))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaOldGrowthBiomes)
				.apply(PlantopiaPlacements::addCascadesBiomes)
				.add(Biomes.RIVER)
				.add(Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.TAIGA)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SWEET_FLAG_SWAMP = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG, SWAMP),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(4.0F),
				CountPlacement.of(UniformInt.of(1, 3)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.allOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT)
					)
				)
			))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaSwampBiomes)
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SWEET_FLAG_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(0.046D, 534, 22);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.022D, 43, 884);
				float bigNoiseLevel = -0.25F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 22),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.1F),
					PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.15F),
					PlacementUtils.HEIGHTMAP_TOP_SOLID,
					WATER_PLANT_RANGE_FILTER,
					BiomeFilter.biome(),
					BlockPredicateFilter.forPredicate(
						BlockPredicate.allOf(
							BlockPredicate.matchesFluids(Fluids.WATER),
							BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT)
						)
					)
				);
			})
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CATTAIL_SWAMP = declarePlacement(
		compileNameFrom(PATCH_CATTAIL, SWAMP),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_CATTAIL)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(2.32F),
				CountPlacement.of(UniformInt.of(1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.matchesFluids(Fluids.WATER)
				)
			))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaSwampBiomes)
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_DUNE_GRASS = declarePlacement(
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
			.biomes(biomes -> biomes
				.add(Biomes.BEACH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SNOWDROP = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_SNOWDROP),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_SNOWDROP)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(12.32f),
				CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.SNOWY_TAIGA)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_ORANGE_WILDFLOWERS_JUNGLE = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_ORANGE_WILDFLOWERS_JUNGLE),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_ORANGE_WILDFLOWERS_JUNGLE)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(8.24F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.addTag(BiomeTags.IS_JUNGLE)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_FLOWERING_LILY_PAD = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_FLOWERING_LILY_PAD),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FLOWERING_LILY_PAD)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(4.12F),
				CountPlacement.of(1),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.WHITE_FLOWERING_LILY_PAD.get())
			))
			.biomes(biomes -> biomes
				.add(Biomes.SWAMP)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_FLOWERING_SMALL_PLATTERLEAF = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_FLOWERING_SMALL_PLATTERLEAF),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FLOWERING_SMALL_PLATTERLEAF)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(4.12F),
				CountPlacement.of(1),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.WHITE_FLOWERING_SMALL_PLATTERLEAF.get())
			))
			.biomes(biomes -> biomes
				.add(Biomes.MANGROVE_SWAMP)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SMALL_PLATTERLEAF = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_SMALL_PLATTERLEAF),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_SMALL_PLATTERLEAF)
			.modifiers(context -> List.of(
				CountPlacement.of(2),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.WHITE_FLOWERING_SMALL_PLATTERLEAF.get())
			))
			.biomes(biomes -> biomes
				.add(Biomes.MANGROVE_SWAMP)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(8.24F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaSwampBiomes)
				.apply(PlantopiaPlacements::addCascadesBiomes)
				.add(Biomes.TAIGA, Biomes.SNOWY_TAIGA, Biomes.DARK_FOREST)
				.add(Biomes.WINDSWEPT_FOREST)
				.add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA)
				.addTag(BiomeTags.IS_JUNGLE)
				.addTag(BiomeTags.IS_BADLANDS)
				.addTag(BiomeTags.IS_SAVANNA)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB, MARSH),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(4.42F),
				CountPlacement.of(UniformInt.of(1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_RARE = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB, RARE),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(18.24F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(Biomes.SNOWY_PLAINS)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_CAVE = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB_CAVE),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB_CAVE)
			.modifiers(context -> List.of(
				PlantopiaDensityPlacement.of(
					0.246F,
					PlantopiaVerticalAnchor.oceanFloorWg(-1),
					PlantopiaVerticalAnchor.absolute(-32)
				),
				InSquarePlacement.spread(),
				PlantopiaRangeFilter.below(PlantopiaVerticalAnchor.oceanFloorWg(-1)),
				BiomeFilter.biome(),
				PlantopiaBiomeFilter.exclude(directBiomes(context, Biomes.LUSH_CAVES))
			))
			.biomes(biomes -> biomes
				.addTag(BiomeTags.IS_OVERWORLD)
			)
	);

	public static final ResourceKey<PlacedFeature> QUAGMIRE_WATER_LEVEL = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.QUAGMIRE_WATER_LEVEL),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.QUAGMIRE_WATER_LEVEL)
			.biomes(biomes -> biomes
				.addTag(PlantopiaBiomeTags.ALLOWS_QUAGMIRE)
			)
			.step(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CLOVER = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CLOVER),
		getCloverDeclaration(PlantopiaVegetationFeatures.PATCH_CLOVER, ConstantFloat.of(12.24F))
			.biomes(biomes -> biomes
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
				.add(Biomes.WINDSWEPT_FOREST)
				.addTag(BiomeTags.IS_SAVANNA, BiomeTags.IS_JUNGLE)
				.addTag(PlantopiaBiomeTags.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_CLOVER_2 = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_CLOVER, 2),
		getCloverDeclaration(PlantopiaVegetationFeatures.PATCH_CLOVER, UniformFloat.of(4.24F, 6.24F))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaOldGrowthBiomes)
				.add(Biomes.MEADOW)
				.add(Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_WHITE_CLOVER_BLOSSOM = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_WHITE_CLOVER_BLOSSOM),
		getCloverDeclaration(PlantopiaVegetationFeatures.PATCH_WHITE_CLOVER_BLOSSOM, UniformFloat.of(26.64F, 29.32F))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaOldGrowthBiomes)
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
				.add(Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.WINDSWEPT_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_PINK_CLOVER_BLOSSOM = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_PINK_CLOVER_BLOSSOM),
		getCloverDeclaration(PlantopiaVegetationFeatures.PATCH_PINK_CLOVER_BLOSSOM, UniformFloat.of(26.64F, 29.32F))
			.biomes(biomes -> biomes
				.apply(PlantopiaPlacements::addVanillaOldGrowthBiomes)
				.add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
				.add(Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.WINDSWEPT_FOREST)
			)
	);

	protected static PlantopiaPlacedFeatureDeclaration.Builder getCloverDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature, FloatProvider chance) {
		return PlantopiaPlacedFeatureDeclaration.builder()
			.feature(feature)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(chance),
				CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			));
	}
}
