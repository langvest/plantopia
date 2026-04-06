package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.worldgen.feature.PlantopiaCaveFeatures;
import by.langvest.plantopia.worldgen.placement.special.*;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.placement.CavePlacements
 */
public class PlantopiaCavePlacements extends PlantopiaPlacements {
    private static final Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<PlacedFeature> declarePlacedFeature(String name, PlantopiaPlacedFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<PlacedFeature> PATCH_SEA_HANGING_MOSS_CAVE = declarePlacedFeature(
        compileNameFrom(PlantopiaCaveFeatures.PATCH_SEA_HANGING_MOSS_CAVE),
        PlantopiaPlacedFeatureDeclaration.builder()
            .feature(PlantopiaCaveFeatures.PATCH_SEA_HANGING_MOSS_CAVE)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(1.264D, 56, 332);
                float bigNoiseLevel = -0.286F;

                return List.of(
                    PlantopiaUndergroundDensityPlacement.of(
                        0.184F,
                        VerticalAnchor.aboveBottom(12),
                        Heightmap.Types.OCEAN_FLOOR_WG
                    ),
                    InSquarePlacement.spread(),
                    PlantopiaSwitchPlacement.switched(
                        List.of(
                            PlantopiaBiomeFilter.include(directBiomes(context, Biomes.DRIPSTONE_CAVES)),
                            PlantopiaNoiseFilter.belowLevel(bigNoiseConfig, bigNoiseLevel, 0.05F),
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
                                    PlantopiaHeightRangeFilter.uniform(VerticalAnchor.BOTTOM, VerticalAnchor.absolute(56))
                                ),
                                List.of(
                                    PlantopiaRarityFilter.onAverageOnceEvery(2.854F)
                                )
                            )
                        )
                    ),
                    PlantopiaBiomeFilter.exclude(directBiomes(context, Biomes.LUSH_CAVES, Biomes.DEEP_DARK))
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
