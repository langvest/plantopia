package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIcicleClusterConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaIcicleConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaLargeIcicleConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaSeaHangingMossPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.stateprovider.PlantopiaTiltedLayeredBlockStateProvider;
import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.LargeDripstoneConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.features.CaveFeatures
 */
public class PlantopiaCaveFeatures extends PlantopiaFeatures {
    private static final Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareConfiguredFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SEA_HANGING_MOSS_CAVE = declareConfiguredFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.SEA_HANGING_MOSS), CAVE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.SEA_HANGING_MOSS_PATCH, context ->
                new PlantopiaSeaHangingMossPatchConfiguration(
                    UniformInt.of(5, 7), // xzSpread
                    ConstantInt.of(2), // ySpread
                    weightedListInt(values -> values
                        .add(UniformInt.of(2, 3), 4)
                        .add(UniformInt.of(5, 10), 3)
                        .add(UniformInt.of(12, 14), 5)
                        .add(UniformInt.of(16, 20), 2)
                    ), // height
                    UniformFloat.of(0.68F, 0.88F), // heightFalloff
                    ConstantFloat.of(0.148F), // heightErosion
                    ConstantFloat.of(0.96F), // edgeErosion
                    ConstantInt.of(9), // searchDistance
                    BlockPredicate.matchesTag(PlantopiaBlockTags.SEA_HANGING_MOSS_CAN_GENERATE_ON), // allowedAttachment
                    BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GLOW_LICHEN) // allowedPlacement
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_ICICLE = declareConfiguredFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICICLE_CLUSTER = declareConfiguredFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> ICICLE = declareConfiguredFeature(
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
