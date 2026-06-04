package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.config.*;
import by.langvest.plantopia.worldgen.feature.stateprovider.PlantopiaTiltedLayeredBlockStateProvider;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;

/**
 * @see net.minecraft.data.worldgen.features.CaveFeatures
 */
public final class PlantopiaCaveFeatures {
    public static final Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    public static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_HANGING_MOSS_CLUSTER = declareFeature(
        compileNameFrom(PlantopiaBlocks.SEA_HANGING_MOSS, CLUSTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.SEA_HANGING_MOSS_CLUSTER, context ->
                new PlantopiaSeaHangingMossClusterConfiguration(
                    UniformInt.of(5, 8), // xzSpread
                    ConstantInt.of(2), // ySpread
                    ConstantInt.of(12), // searchDistance
                    weightedListInt(values -> values
                        .add(ConstantInt.of(1), 1)
                        .add(ConstantInt.of(2), 2)
                        .add(ConstantInt.of(3), 3)
                    ), // depth
                    UniformFloat.of(0.3F, 0.7F), // curvature
                    BlockPredicate.allOf(
                        BlockPredicate.matchesTag(PlantopiaBlockTags.SEA_MOSS_REPLACEABLE),
                        BlockPredicate.anyOf(
                            BlockPredicate.not(BlockPredicate.solid(BlockPos.ZERO.below())),
                            BlockPredicate.allOf(
                                BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.SEA_MOSS_BLOCK.get()),
                                BlockPredicate.anyOf(
                                    BlockPredicate.not(BlockPredicate.solid(BlockPos.ZERO.north())),
                                    BlockPredicate.not(BlockPredicate.solid(BlockPos.ZERO.south())),
                                    BlockPredicate.not(BlockPredicate.solid(BlockPos.ZERO.east())),
                                    BlockPredicate.not(BlockPredicate.solid(BlockPos.ZERO.west()))
                                )
                            )
                        ),
                        BlockPredicate.solid(BlockPos.ZERO.above())
                    ), // allowedBasisPlacement
                    weightedListInt(values -> values
                        .add(UniformInt.of(2, 3), 6)
                        .add(UniformInt.of(4, 7), 4)
                        .add(UniformInt.of(8, 15), 2)
                        .add(UniformInt.of(16, 20), 1)
                    ), // height
                    UniformFloat.of(0.58F, 0.88F), // heightFalloff
                    ConstantFloat.of(0.218F), // heightErosion
                    ConstantFloat.of(0.98F), // edgeErosion
                    BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GLOW_LICHEN) // allowedPlacement
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_ICICLE = declareFeature(
        compileNameFrom(LARGE, PlantopiaBlocks.ICICLE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.LARGE_ICICLE, context ->
                new PlantopiaLargeIcicleConfiguration(
                    30,
                    UniformInt.of(3, 19),
                    UniformFloat.of(0.4F, 2.0F),
                    0.33F,
                    UniformFloat.of(0.3F, 0.9F),
                    UniformFloat.of(0.4F, 1.0F),
                    UniformFloat.of(0.0F, 0.3F),
                    4,
                    0.6F,
                    PlantopiaTiltedLayeredBlockStateProvider.builder()
                        .angle(35.0F)
                        .rotation(45.0F)
                        .addLayer(5, simpleProvider(Blocks.PACKED_ICE))
                        .addLayer(3, simpleProvider(Blocks.ICE))
                        .build()
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICICLE_CLUSTER = declareFeature(
        compileNameFrom(PlantopiaBlocks.ICICLE, CLUSTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.ICICLE_CLUSTER, context ->
                new PlantopiaIcicleClusterConfiguration(
                    12,
                    UniformInt.of(3, 6),
                    UniformInt.of(2, 8),
                    1,
                    3,
                    UniformInt.of(2, 4),
                    UniformFloat.of(0.3F, 0.7F),
                    0.1F,
                    3,
                    8
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICICLE = declareFeature(
        compileNameFrom(PlantopiaBlocks.ICICLE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(Feature.SIMPLE_RANDOM_SELECTOR, context ->
                new SimpleRandomFeatureConfiguration(
                    HolderSet.direct(
                        PlacementUtils.inlinePlaced(
                            PlantopiaFeatureTypes.ICICLE.get(),
                            new PlantopiaIcicleConfiguration(0.2F, 0.7F, 0.5F, 0.5F),
                            EnvironmentScanPlacement.scanningFor(
                                Direction.DOWN,
                                BlockPredicate.solid(),
                                BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                12
                            ),
                            RandomOffsetPlacement.vertical(ConstantInt.of(1))
                        ),
                        PlacementUtils.inlinePlaced(
                            PlantopiaFeatureTypes.ICICLE.get(),
                            new PlantopiaIcicleConfiguration(0.2F, 0.7F, 0.5F, 0.5F),
                            EnvironmentScanPlacement.scanningFor(
                                Direction.UP,
                                BlockPredicate.solid(),
                                BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE,
                                12
                            ),
                            RandomOffsetPlacement.vertical(ConstantInt.of(-1))
                        )
                    )
                )
            ))
    );
}
