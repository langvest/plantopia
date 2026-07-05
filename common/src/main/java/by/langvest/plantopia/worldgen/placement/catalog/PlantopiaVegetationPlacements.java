package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
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
public interface PlantopiaVegetationPlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    /* VEGETATION PLACEMENTS ******************************************/

    ResourceKey<PlacedFeature> HOGWEED_BONEMEAL = declarePlacement(
        compileNameFrom(PlantopiaBlocks.HOGWEED, BONEMEAL),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SINGLE_HOGWEED)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get())
            ))
    );

    ResourceKey<PlacedFeature> HOGWEED_COLONY = declarePlacement(
        compileNameFrom(PlantopiaFeatures.HOGWEED_COLONY),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.HOGWEED_COLONY)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(2.886D, 384, 114);
                var smallNoiseConfig = PlantopiaNoiseConfig.of(0.116D, 562, 98);
                float bigNoiseLevel = -0.88F;
                float smallNoiseLevel = -0.22F;

                return List.of(
                    PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 1),
                    InSquarePlacement.spread(),
                    PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.1F),
                    PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.15F),
                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                    BiomeFilter.biome()
                );
            })
            .biomes(biomes -> biomes
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
                .addTag(BiomeTags.IS_SAVANNA)
                .addTag(PlantopiaBiomeTags.IS_MARSH)
                .apply(PlantopiaPlacementUtils::addVanillaMountainBiomes)
            )
    );

    ResourceKey<PlacedFeature> HOGWEED_INFESTED_GRASS_BLOCK = declarePlacement(
        compileNameFrom(PlantopiaBlocks.HOGWEED, PlantopiaBlocks.INFESTED_GRASS_BLOCK),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SINGLE_HOGWEED)
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

    ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_FIREWEED_MOUNTAIN),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_FIREWEED_MOUNTAIN)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(0.614D, 501, 402);
                var smallNoiseConfig = PlantopiaNoiseConfig.of(0.102D, 274, 148);
                float bigNoiseLevel = -0.815F;
                float smallNoiseLevel = -0.1F;

                return List.of(
                    PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 20),
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
                .add(Biomes.PLAINS, Biomes.MEADOW)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST)
            )
    );

    ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN_2 = declarePlacement(
        compileNameFrom(PATCH_FIREWEED_MOUNTAIN, 2),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_FIREWEED_MOUNTAIN)
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
                .add(Biomes.PLAINS, Biomes.MEADOW)
            )
    );

    ResourceKey<PlacedFeature> PATCH_TINY_CACTUS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_TINY_CACTUS_ON_SAND),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_TINY_CACTUS_ON_SAND)
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

    ResourceKey<PlacedFeature> PATCH_REED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_REED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_REED)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(3.12F, 4.12F),
                CountPlacement.of(UniformInt.of(1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                WATER_PLANT_FIND_WATER,
                WATER_PLANT_RANGE_FILTER,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaOldGrowthBiomes)
                .apply(PlantopiaPlacementUtils::addVanillaSwampBiomes)
                .apply(PlantopiaPlacementUtils::addCascadesBiomes)
                .add(Biomes.SAVANNA)
                .add(Biomes.RIVER)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.BEACH, Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.TAIGA)
                .add(PlantopiaBiomes.MARSH)
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST, PlantopiaBiomes.SEASONAL_FOREST)
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_REED_SNOWY = declarePlacement(
        compileNameFrom(PATCH_REED, SNOWY),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_REED)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(2.0F),
                CountPlacement.of(UniformInt.of(1, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                WATER_PLANT_FIND_WATER,
                WATER_PLANT_RANGE_FILTER,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.FROZEN_RIVER)
                .add(Biomes.SNOWY_PLAINS)
                .add(Biomes.GROVE, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH)
            )
    );

    ResourceKey<PlacedFeature> PATCH_CHICORY = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_CHICORY),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_CHICORY)
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

    ResourceKey<PlacedFeature> PATCH_FLUFFY_GRASS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_FLUFFY_GRASS),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_FLUFFY_GRASS)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(5.32F, 6.64F),
                CountPlacement.of(weightedListInt(values -> values
                    .add(ConstantInt.of(1), 3)
                    .add(ConstantInt.of(2), 2)
                    .add(ConstantInt.of(3), 1)
                )),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.DARK_FOREST, Biomes.TAIGA, Biomes.SNOWY_TAIGA)
                .add(Biomes.WINDSWEPT_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .add(PlantopiaBiomes.SEASONAL_FOREST, PlantopiaBiomes.SEASONAL_DARK_FOREST)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    //	public static final ResourceKey<PlacedFeature> PATCH_CHICORY_2 = declarePlacement(
    //		compileNameFrom(PlantopiaFeatures.PATCH_CHICORY, 2),
    //		PlantopiaPlacementDeclaration.builder()
    //			.feature(PlantopiaFeatures.PATCH_CHICORY)
    //			.modifiers(context -> List.of(
    //				PlantopiaRarityFilter.onAverageOnceEvery(8.12F, 10.42F),
    //				CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
    //				InSquarePlacement.spread(),
    //				PlacementUtils.HEIGHTMAP,
    //				BiomeFilter.biome()
    //			))
    //			.biomes(biomes -> biomes
    //				.add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS)
    //			)
    //	);

    ResourceKey<PlacedFeature> PATCH_TANSY = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_TANSY),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_TANSY)
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

    ResourceKey<PlacedFeature> PATCH_TANSY_2 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_TANSY, 2),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_TANSY)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(6.12F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 3), 1, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                //				.add(Biomes.WINDSWEPT_FOREST)
            )
    );

    //	public static final ResourceKey<PlacedFeature> PATCH_CARROTWEED = declarePlacedFeature(
    //		compileNameFrom(PlantopiaFeatures.PATCH_CARROTWEED),
    //		PlantopiaPlacedFeatureDeclaration.builder()
    //			.feature(PlantopiaFeatures.PATCH_CARROTWEED)
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

    ResourceKey<PlacedFeature> PATCH_CARROTWEED_MOUNTAIN = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_CARROTWEED_MOUNTAIN),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_CARROTWEED_MOUNTAIN)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(0.158D, 188, 398);
                var smallNoiseConfig = PlantopiaNoiseConfig.of(0.0684D, 68, 834);
                float bigNoiseLevel = -0.426F;
                float smallNoiseLevel = -0.1F;

                return List.of(
                    PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 20),
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

    ResourceKey<PlacedFeature> PATCH_GRASS_BONUS = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_GRASS, BONUS),
        PlantopiaPlacementDeclaration.builder()
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
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST, PlantopiaBiomes.SEASONAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> PATCH_TAIGA_GRASS_BONUS = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_TAIGA_GRASS, BONUS),
        PlantopiaPlacementDeclaration.builder()
            .feature(VegetationFeatures.PATCH_TAIGA_GRASS)
            .modifiers(context -> List.of(
                CountPlacement.of(ClampedInt.of(UniformInt.of(1, 3), 2, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.WINDSWEPT_FOREST)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_FERN_BONUS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_FERN, BONUS),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_FERN)
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
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST, PlantopiaBiomes.SEASONAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> PATCH_LARGE_FERN_BONUS = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_LARGE_FERN, BONUS),
        PlantopiaPlacementDeclaration.builder()
            .feature(VegetationFeatures.PATCH_LARGE_FERN)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(5.0F),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.DARK_FOREST)
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST)
            )
    );

    ResourceKey<PlacedFeature> PATCH_CATTAIL = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_CATTAIL),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_CATTAIL)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(5.0F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(1, 3), 2, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                WATER_PLANT_FIND_WATER,
                WATER_PLANT_RANGE_FILTER,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaOldGrowthBiomes)
                .apply(PlantopiaPlacementUtils::addCascadesBiomes)
                .add(Biomes.RIVER)
                .add(Biomes.FOREST, Biomes.DARK_FOREST, Biomes.BIRCH_FOREST, Biomes.TAIGA)
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST, PlantopiaBiomes.SEASONAL_FOREST)
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_SWEET_FLAG_SWAMP = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_SWEET_FLAG, SWAMP),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_SWEET_FLAG)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(4.0F),
                CountPlacement.of(UniformInt.of(1, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                WATER_PLANT_FIND_WATER,
                WATER_PLANT_RANGE_FILTER,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaSwampBiomes)
                .add(PlantopiaBiomes.DEAD_MARSH)
            )
    );

    ResourceKey<PlacedFeature> PATCH_CATTAIL_SWAMP = declarePlacement(
        compileNameFrom(PATCH_CATTAIL, SWAMP),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_CATTAIL)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(2.32F),
                CountPlacement.of(UniformInt.of(1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                WATER_PLANT_FIND_WATER,
                WATER_PLANT_RANGE_FILTER,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaSwampBiomes)
                .add(PlantopiaBiomes.DEAD_MARSH)
            )
    );

    ResourceKey<PlacedFeature> PATCH_DUNE_GRASS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_DUNE_GRASS),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_DUNE_GRASS)
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

    ResourceKey<PlacedFeature> PATCH_SNOWDROP = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_SNOWDROP),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_SNOWDROP)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(12.64f),
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.SNOWY_TAIGA)
            )
    );

    ResourceKey<PlacedFeature> PATCH_ORANGE_WILDFLOWERS_JUNGLE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_ORANGE_WILDFLOWERS_JUNGLE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_ORANGE_WILDFLOWERS_JUNGLE)
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

    ResourceKey<PlacedFeature> PATCH_LAVENDER = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_LAVENDER),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_LAVENDER)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(0.092D, 74, 193);
                var smallNoiseConfig = PlantopiaNoiseConfig.of(0.046D, 12, 543);
                float bigNoiseLevel = -0.32F;
                float smallNoiseLevel = -0.1F;

                return List.of(
                    PlantopiaNoiseCountPlacement.above(bigNoiseConfig, bigNoiseLevel, 20),
                    InSquarePlacement.spread(),
                    PlantopiaNoiseFilter.above(bigNoiseConfig, bigNoiseLevel, 0.1F),
                    PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.1F),
                    PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                    BiomeFilter.biome(),
                    PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.LAVENDER.get())
                );
            })
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
            )
    );

    ResourceKey<PlacedFeature> TREES_LAVENDER_FIELDS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_LAVENDER_FIELDS),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.TREES_LAVENDER_FIELDS)
            .modifiers(context -> List.of(
                PlacementUtils.countExtra(0, 0.125F, 1),
                InSquarePlacement.spread(),
                TREE_THRESHOLD,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_FLOWERING_LILY_PAD = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_FLOWERING_LILY_PAD),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_FLOWERING_LILY_PAD)
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

    ResourceKey<PlacedFeature> PATCH_FLOWERING_SMALL_PLATTERLEAF = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_FLOWERING_SMALL_PLATTERLEAF),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_FLOWERING_SMALL_PLATTERLEAF)
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

    ResourceKey<PlacedFeature> PATCH_SMALL_PLATTERLEAF = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_SMALL_PLATTERLEAF),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_SMALL_PLATTERLEAF)
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

    ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_BRANCHING_SHRUB),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_BRANCHING_SHRUB)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(8.24F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaSwampBiomes)
                .apply(PlantopiaPlacementUtils::addCascadesBiomes)
                .add(Biomes.TAIGA, Biomes.SNOWY_TAIGA, Biomes.DARK_FOREST)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                .addTag(BiomeTags.IS_JUNGLE)
                .addTag(BiomeTags.IS_BADLANDS)
                .addTag(BiomeTags.IS_SAVANNA)
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST, PlantopiaBiomes.SEASONAL_FOREST)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_RARE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_BRANCHING_SHRUB, RARE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_BRANCHING_SHRUB)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(18.24F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.SNOWY_PLAINS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_CAVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_BRANCHING_SHRUB_CAVE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_BRANCHING_SHRUB_CAVE)
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

    ResourceKey<PlacedFeature> QUAGMIRE_WATER_LEVEL = declarePlacement(
        compileNameFrom(PlantopiaFeatures.QUAGMIRE_WATER_LEVEL),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.QUAGMIRE_WATER_LEVEL)
            .biomes(biomes -> biomes
                .addTag(PlantopiaBiomeTags.ALLOWS_QUAGMIRE)
            )
            .step(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
    );

    ResourceKey<PlacedFeature> PATCH_CLOVER = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_CLOVER),
        getCloverDeclaration(PlantopiaFeatures.PATCH_CLOVER, ConstantFloat.of(12.24F))
            .biomes(biomes -> biomes
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.WINDSWEPT_FOREST)
                .addTag(BiomeTags.IS_SAVANNA, BiomeTags.IS_JUNGLE)
                .addTag(PlantopiaBiomeTags.IS_MARSH)
            )
    );

    ResourceKey<PlacedFeature> PATCH_CLOVER_2 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_CLOVER, 2),
        getCloverDeclaration(PlantopiaFeatures.PATCH_CLOVER, UniformFloat.of(4.24F, 6.24F))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaOldGrowthBiomes)
                .add(Biomes.MEADOW)
                .add(Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST)
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST)
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_WHITE_CLOVER_BLOSSOM = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_WHITE_CLOVER_BLOSSOM),
        getCloverDeclaration(PlantopiaFeatures.PATCH_WHITE_CLOVER_BLOSSOM, UniformFloat.of(26.64F, 29.32F))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaOldGrowthBiomes)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.WINDSWEPT_FOREST)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_PINK_CLOVER_BLOSSOM = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_PINK_CLOVER_BLOSSOM),
        getCloverDeclaration(PlantopiaFeatures.PATCH_PINK_CLOVER_BLOSSOM, UniformFloat.of(26.64F, 29.32F))
            .biomes(biomes -> biomes
                .apply(PlantopiaPlacementUtils::addVanillaOldGrowthBiomes)
                .add(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.TAIGA, Biomes.BIRCH_FOREST, Biomes.WINDSWEPT_FOREST)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    /* HELPER METHODS ****************************************************/

    static PlantopiaPlacementDeclaration.Builder getCloverDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature, FloatProvider chance) {
        return PlantopiaPlacementDeclaration.builder()
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
