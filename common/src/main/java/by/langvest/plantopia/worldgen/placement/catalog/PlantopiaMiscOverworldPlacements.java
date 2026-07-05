package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

/**
 * @see net.minecraft.data.worldgen.placement.MiscOverworldPlacements
 */
public interface PlantopiaMiscOverworldPlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<PlacedFeature> PIT_QUICKSAND = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PIT_QUICKSAND),
        PlantopiaPlacementDeclaration.builder()
            .step(GenerationStep.Decoration.LOCAL_MODIFICATIONS)
            .feature(PlantopiaFeatures.PIT_QUICKSAND)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(20.0F, 22.0F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.DESERT)
            )
    );

    ResourceKey<PlacedFeature> PATCH_SEA_SHELL = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_SEA_SHELL),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_SEA_SHELL)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(8.48F),
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.BEACH)
            )
    );

    ResourceKey<PlacedFeature> PATCH_SEA_SHELL_OCEAN = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_SEA_SHELL, OCEAN),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_SEA_SHELL)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(4.24F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                PlantopiaRangeFilter.below(PlantopiaVerticalAnchor.seaLevel(-4)),
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER))
            ))
            .biomes(biomes -> biomes
                .add(Biomes.OCEAN, Biomes.COLD_OCEAN)
                .add(Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_COLD_OCEAN)
            )
    );

    ResourceKey<PlacedFeature> PATCH_SEA_SHELL_OCEAN_2 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_SEA_SHELL, OCEAN, 2),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_SEA_SHELL)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(1.12F, 2.24F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                PlantopiaRangeFilter.below(PlantopiaVerticalAnchor.seaLevel(-4)),
                BiomeFilter.biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.matchesFluids(Fluids.WATER))
            ))
            .biomes(biomes -> biomes
                .add(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN)
            )
    );

    ResourceKey<PlacedFeature> PATCH_COBBLESTONE_SHARD = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_COBBLESTONE_SHARD),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_COBBLESTONE_SHARD)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(2.86F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.OCEAN, Biomes.COLD_OCEAN, Biomes.FROZEN_OCEAN)
                .add(Biomes.MUSHROOM_FIELDS, Biomes.MEADOW, Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS)
                .add(Biomes.CHERRY_GROVE)
                .add(Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST)
                .add(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.TAIGA, Biomes.SNOWY_TAIGA, Biomes.BIRCH_FOREST, Biomes.DARK_FOREST)
                .add(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.SPARSE_JUNGLE)
                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                .add(Biomes.RIVER, Biomes.FROZEN_RIVER)
                .add(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA)
                .add(PlantopiaBiomes.MARSH, PlantopiaBiomes.DEAD_MARSH)
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST, PlantopiaBiomes.SEASONAL_FOREST)
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
                .add(PlantopiaBiomes.BOREAL_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_MOSSY_COBBLESTONE_SHARD = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_MOSSY_COBBLESTONE_SHARD),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_MOSSY_COBBLESTONE_SHARD)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(2.86F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN)
                .add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP)
            )
    );

    ResourceKey<PlacedFeature> PATCH_COBBLESTONE_SHARD_2 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_COBBLESTONE_SHARD, 2),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_COBBLESTONE_SHARD)
            .modifiers(context -> List.of(
                CountPlacement.of(ClampedInt.of(UniformInt.of(0, 3), 1, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.DEEP_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.DEEP_FROZEN_OCEAN)
                .add(Biomes.STONY_PEAKS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.STONY_SHORE)
            )
    );

    ResourceKey<PlacedFeature> PATCH_MOSSY_COBBLESTONE_SHARD_2 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_MOSSY_COBBLESTONE_SHARD, 2),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_MOSSY_COBBLESTONE_SHARD)
            .modifiers(context -> List.of(
                CountPlacement.of(UniformInt.of(1, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.DEEP_LUKEWARM_OCEAN)
                .add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA)
            )
    );

    ResourceKey<PlacedFeature> PATCH_COBBLESTONE_SHARD_CAVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_COBBLESTONE_SHARD, CAVE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_COBBLESTONE_SHARD)
            .modifiers(context -> List.of(
                PlantopiaDensityPlacement.of(
                    0.256F,
                    PlantopiaVerticalAnchor.oceanFloorWg(-1),
                    PlantopiaVerticalAnchor.absolute(4)
                ),
                InSquarePlacement.spread(),
                EnvironmentScanPlacement.scanningFor(
                    Direction.DOWN,
                    BlockPredicate.allOf(
                        BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                        BlockPredicate.solid(BlockPos.ZERO.below())
                    ),
                    6
                ),
                PlantopiaRangeFilter.below(PlantopiaVerticalAnchor.oceanFloorWg(-1))
            ))
            .biomes(biomes -> biomes
                .addTag(BiomeTags.IS_OVERWORLD)
            )
    );

    ResourceKey<PlacedFeature> SINGLE_ICICLE_STALACTITE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SINGLE_ICICLE_STALACTITE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SINGLE_ICICLE_STALACTITE)
            .modifiers(context -> List.of(
                PlantopiaDensityPlacement.of(
                    2.48F,
                    PlantopiaVerticalAnchor.worldSurfaceWg(60),
                    PlantopiaVerticalAnchor.worldSurfaceWg(-6)
                ),
                InSquarePlacement.spread(),
                EnvironmentScanPlacement.scanningFor(
                    Direction.UP,
                    BlockPredicate.allOf(
                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                        BlockPredicate.matchesBlocks(BlockPos.ZERO.above(), Blocks.PACKED_ICE)
                    ),
                    6
                ),
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.ICE_SPIKES, Biomes.FROZEN_PEAKS)
                .add(Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN)
            )
    );

    ResourceKey<PlacedFeature> PATCH_ICICLE_STALAGMITE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_ICICLE_STALAGMITE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.PATCH_ICICLE_STALAGMITE)
            .modifiers(context -> List.of(
                CountPlacement.of(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.motionBlocking()),
                BlockPredicateFilter.forPredicate(
                    BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), Blocks.PACKED_ICE)
                ),
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.ICE_SPIKES)
            )
    );

    ResourceKey<PlacedFeature> FRAZIL_WATER_LEVEL = declarePlacement(
        compileNameFrom(PlantopiaFeatures.FRAZIL_WATER_LEVEL),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.FRAZIL_WATER_LEVEL)
            .biomes(biomes -> biomes
                .addTag(PlantopiaBiomeTags.ALLOWS_FRAZIL)
            )
            .step(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
    );
}
