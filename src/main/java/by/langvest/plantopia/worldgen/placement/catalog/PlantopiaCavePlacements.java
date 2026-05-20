package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaCaveFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

/**
 * @see net.minecraft.data.worldgen.placement.CavePlacements
 */
public final class PlantopiaCavePlacements {
    public static final Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    public static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    public static final ResourceKey<PlacedFeature> PATCH_SEA_HANGING_MOSS_CAVE = declarePlacement(
        compileNameFrom(PlantopiaCaveFeatures.PATCH_SEA_HANGING_MOSS_CAVE),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaCaveFeatures.PATCH_SEA_HANGING_MOSS_CAVE)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(1.264D, 56, 332);
                float bigNoiseLevel = -0.286F;

                return List.of(
                    PlantopiaDensityPlacement.of(
                        0.18F,
                        PlantopiaVerticalAnchor.oceanFloorWg(-1),
                        PlantopiaVerticalAnchor.aboveBottom(12)
                    ),
                    InSquarePlacement.spread(),
                    PlantopiaSwitchPlacement.switched(
                        List.of(
                            PlantopiaBiomeFilter.include(directBiomes(context, Biomes.DRIPSTONE_CAVES)),
                            PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.05F),
                            PlantopiaRarityFilter.onAverageOnceEvery(1.352F)
                        ),
                        List.of(
                            PlantopiaEnvironmentScanFilter.scanningFor(
                                Direction.DOWN,
                                BlockPredicate.anyOf(
                                    BlockPredicate.matchesFluids(Fluids.WATER),
                                    BlockPredicate.matchesBlocks(Blocks.WATER)
                                ),
                                BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                12
                            ),
                            PlantopiaConditionPlacement.conditional(
                                List.of(
                                    PlantopiaRangeFilter.below(PlantopiaVerticalAnchor.absolute(56))
                                ),
                                List.of(
                                    PlantopiaRarityFilter.onAverageOnceEvery(2.854F)
                                )
                            )
                        )
                    ),
                    PlantopiaBiomeFilter.exclude(directBiomes(context, Biomes.LUSH_CAVES, Biomes.DEEP_DARK, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS))
                );
            })
            .biomes(biomes -> biomes
                .addTag(BiomeTags.IS_OVERWORLD)
            )
    );

//    public static final ResourceKey<PlacedFeature> ICICLE_CLUSTER = declarePlacedFeature(
//        compileNameFrom(PlantopiaCaveFeatures.ICICLE_CLUSTER),
//        PlantopiaPlacedFeatureDeclaration.builder()
//            .feature(PlantopiaCaveFeatures.ICICLE_CLUSTER)
//            .modifiers(context -> List.of(
//                CountPlacement.of(UniformInt.of(48, 96)),
//                InSquarePlacement.spread(),
//                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
//                BiomeFilter.biome()
//            ))
//            .biomes(biomes -> biomes
//                .add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH, Biomes.SNOWY_SLOPES)
//                .add(Biomes.ICE_SPIKES, Biomes.GROVE)
//                .add(Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.FROZEN_PEAKS, Biomes.DEEP_FROZEN_OCEAN)
//            )
//    );
//
//    public static final ResourceKey<PlacedFeature> LARGE_ICICLE = declarePlacedFeature(
//        compileNameFrom(PlantopiaCaveFeatures.LARGE_ICICLE),
//        PlantopiaPlacedFeatureDeclaration.builder()
//            .feature(PlantopiaCaveFeatures.LARGE_ICICLE)
//            .modifiers(context -> List.of(
//                CountPlacement.of(UniformInt.of(10, 48)),
//                InSquarePlacement.spread(),
//                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
//                BiomeFilter.biome()
//            ))
//            .biomes(biomes -> biomes
//                .add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH, Biomes.SNOWY_SLOPES)
//                .add(Biomes.ICE_SPIKES, Biomes.GROVE)
//                .add(Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.FROZEN_PEAKS, Biomes.DEEP_FROZEN_OCEAN)
//            )
//    );
//
//    public static final ResourceKey<PlacedFeature> ICICLE = declarePlacedFeature(
//        compileNameFrom(PlantopiaCaveFeatures.ICICLE),
//        PlantopiaPlacedFeatureDeclaration.builder()
//            .feature(PlantopiaCaveFeatures.ICICLE)
//            .modifiers(context -> List.of(
//                CountPlacement.of(UniformInt.of(192, 256)),
//                InSquarePlacement.spread(),
//                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
//                CountPlacement.of(UniformInt.of(1, 5)),
//                RandomOffsetPlacement.of(
//                    ClampedNormalInt.of(0.0F, 3.0F, -10, 10),
//                    ClampedNormalInt.of(0.0F, 0.6F, -2, 2)
//                ),
//                BiomeFilter.biome()
//            ))
//            .biomes(biomes -> biomes
//                .add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_BEACH, Biomes.SNOWY_SLOPES)
//                .add(Biomes.ICE_SPIKES, Biomes.GROVE)
//                .add(Biomes.FROZEN_OCEAN, Biomes.FROZEN_RIVER, Biomes.FROZEN_PEAKS, Biomes.DEEP_FROZEN_OCEAN)
//            )
//    );
}
