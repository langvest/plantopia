package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCobblestoneShardBlock;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaDictionary;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.config.*;
import by.langvest.plantopia.worldgen.placement.PlantopiaDipType;
import by.langvest.plantopia.worldgen.placement.PlantopiaMultiNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaSimpleBlockPlacer;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.value.PlantopiaProviderUtils.simpleProvider;
import static by.langvest.plantopia.worldgen.value.PlantopiaProviderUtils.weightedListInt;

/**
 * @see net.minecraft.data.worldgen.features.MiscOverworldFeatures
 */
public interface PlantopiaMiscOverworldFeatures {
    Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<ConfiguredFeature<?, ?>> ROCK = declareFeature(
        compileNameFrom(PlantopiaDictionary.ROCK),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(Feature.FOREST_ROCK, context ->
                new BlockStateConfiguration(Blocks.COBBLESTONE.defaultBlockState())
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> MOSSY_ROCK = declareFeature(
        compileNameFrom(MOSSY, PlantopiaDictionary.ROCK),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(Feature.FOREST_ROCK, context ->
                new BlockStateConfiguration(Blocks.MOSSY_COBBLESTONE.defaultBlockState())
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> LAKE_WATER_MARSH = declareFeature(
        compileNameFrom(LAKE, Blocks.WATER, MARSH),
        PlantopiaFeatureDeclaration.builder()
            .feature(lake(context ->
                new LakeFeature.Configuration(
                    simpleProvider(Blocks.WATER),
                    simpleProvider(Blocks.AIR)
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PIT_QUICKSAND = declareFeature(
        compileNameFrom(PIT, PlantopiaBlocks.QUICKSAND),
        PlantopiaFeatureDeclaration.builder()
            .feature(pit(context ->
                new PlantopiaPitConfiguration(
                    UniformInt.of(5, 9),
                    UniformInt.of(3, 5),
                    UniformInt.of(3, 5),
                    UniformFloat.of(0.2F, 0.25F),
                    simpleProvider(PlantopiaBlocks.QUICKSAND.get()),
                    Optional.of(BlockPredicate.matchesBlocks(Blocks.SAND)),
                    Optional.of(Heightmap.Types.OCEAN_FLOOR_WG)
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_SEA_SHELL = declareFeature(
        patchNameOf("sea_shell"),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(context ->
                new PlantopiaLimitedRandomPatchConfiguration(
                    UniformInt.of(8, 12),
                    UniformInt.of(3, 4),
                    ClampedInt.of(UniformInt.of(1, 4), 2, 4),
                    ClampedInt.of(UniformInt.of(1, 4), 2, 4),
                    PlacementUtils.filtered(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        weightedConfig(states -> states
                            .add(PlantopiaBlocks.ROUND_SEA_SHELL.get().defaultBlockState(), 3)
                            .add(PlantopiaBlocks.TWISTY_SEA_SHELL.get().defaultBlockState(), 4)
                            .add(PlantopiaBlocks.TUBE_SEA_SHELL.get().defaultBlockState(), 4)
                        ),
                        BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.SEAGRASS)
                    )
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_COBBLESTONE_SHARD = declareFeature(
        patchNameOf(PlantopiaBlocks.COBBLESTONE_SHARD),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(getCobblestoneShardConfig(PlantopiaBlocks.COBBLESTONE_SHARD)))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOSSY_COBBLESTONE_SHARD = declareFeature(
        patchNameOf(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(getCobblestoneShardConfig(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD)))
    );

    ResourceKey<ConfiguredFeature<?, ?>> FAIRY_RING = declareFeature(
        compileNameFrom("fairy_ring"),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(context ->
                new PlantopiaRadialPatchConfiguration(
                    ConstantInt.of(48),
                    UniformInt.of(3, 5),
                    ConstantInt.of(2),
                    -4.82,
                    0,
                    List.of(
                        new PlantopiaSimpleBlockPlacer(
                            simpleProvider(PlantopiaBlocks.WITCHY_TOADSTOOL.get())
                        )
                    ),
                    Optional.of(
                        BlockPredicate.allOf(
                            BlockPredicate.anyOf(
                                BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS, Blocks.FERN, Blocks.VINE, PlantopiaBlocks.CLOVER.get()),
                                BlockPredicate.matchesTag(BlockTags.SMALL_FLOWERS)
                            ),
                            BlockPredicate.matchesTag(BlockPos.ZERO.below(), PlantopiaBlockTags.WITCHY_TOADSTOOL_CAN_GENERATE_ON)
                        )
                    ),
                    Optional.empty()
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> FAIRY_RING_ANCHOR = declareFeature(
        compileNameFrom(FAIRY_RING, ANCHOR),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.POI_ANCHOR, context ->
                new PlantopiaPoiAnchorConfiguration(
                    lookupFeatures(context).getOrThrow(FAIRY_RING),
                    PlantopiaDipType.SURFACE_OR_CAVE,
                    VerticalAnchor.aboveBottom(32),
                    VerticalAnchor.belowTop(32),
                    ConstantInt.of(5),
                    List.of(PlantopiaThresholdType.ABOVE),
                    Optional.of(BlockPredicate.matchesTag(BlockPos.ZERO.below(), PlantopiaBlockTags.WITCHY_TOADSTOOL_CAN_GENERATE_ON)),
                    Optional.of(Heightmap.Types.OCEAN_FLOOR_WG)
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> SINGLE_ICICLE_STALACTITE = declareFeature(
        compileNameFrom(SINGLE, PlantopiaBlocks.ICICLE, STALACTITE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.ICICLE_COLUMN, context ->
                new PlantopiaIcicleColumnConfiguration(
                    weightedListInt(values -> values
                        .add(ConstantInt.of(1), 2)
                        .add(ConstantInt.of(2), 3)
                        .add(ConstantInt.of(3), 1)
                    ), // height
                    Direction.DOWN,
                    BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_ICICLE_STALAGMITE = declareFeature(
        compileNameFrom(PATCH, PlantopiaBlocks.ICICLE, STALAGMITE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.ICICLE_PATCH, context ->
                new PlantopiaIciclePatchConfiguration(
                    ConstantInt.of(3), // xzSpread
                    ConstantInt.of(1), // ySpread
                    ConstantInt.of(4), // tries
                    UniformInt.of(3, 4), // height
                    UniformFloat.of(0.52F, 0.56F), // heightFalloff
                    ConstantFloat.of(0.128F), // heightErosion
                    ConstantFloat.of(-0.448F), // shapeSigma
                    ConstantFloat.of(0.354F), // shapeErosion
                    ConstantInt.of(6), // searchDistance
                    BlockPredicate.matchesBlocks(Blocks.PACKED_ICE),
                    BlockPredicate.ONLY_IN_AIR_PREDICATE,
                    List.of(Direction.UP),
                    Optional.of(Heightmap.Types.WORLD_SURFACE_WG)
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> FRAZIL_WATER_LEVEL = declareFeature(
        compileNameFrom("frazil_water_level"),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.FRAZIL_WATER_LEVEL, context ->
                new PlantopiaFrazilConfiguration(
                    PlantopiaMultiNoiseConfig.builder()
                        .add(PlantopiaNoiseConfig.of(0.312D, 719, 21), 1)
                        .add(PlantopiaNoiseConfig.of(0.138D, 32, 75), 0.64)
                        .add(PlantopiaNoiseConfig.of(0.072D, 78, 321), 0.48)
                        .build(),
                    PlantopiaThresholdType.BELOW,
                    -0.032F,
                    BlockPredicate.allOf(
                        BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS, Blocks.VINE),
                        BlockPredicate.matchesFluids(BlockPos.ZERO.below(), Fluids.WATER)
                    ),
                    lookupBiomes(context).getOrThrow(PlantopiaBiomeTags.ALLOWS_FRAZIL)
                )
            ))
    );

    /* HELPER METHODS *************************************************************************/

    @Contract(pure = true)
    private static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaLimitedRandomPatchConfiguration> getCobblestoneShardConfig(Supplier<Block> cobblestoneShardBlock) {
        return context ->
            new PlantopiaLimitedRandomPatchConfiguration(
                UniformInt.of(3, 4), // tries
                UniformInt.of(2, 3), // limit
                ConstantInt.of(1), // xzSpread
                ConstantInt.of(1), // ySpread
                PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> {
                        for (int amount = PlantopiaCobblestoneShardBlock.MIN_SHARDS; amount <= PlantopiaCobblestoneShardBlock.MAX_SHARDS; amount++) {
                            int weight = calculateExponentialWeight(amount, 0.2165);

                            var state = cobblestoneShardBlock.get().defaultBlockState()
                                .setValue(PlantopiaCobblestoneShardBlock.AMOUNT, amount);

                            states.add(state, weight);
                        }

                        return states;
                    }),
                    BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS, Blocks.WATER, Blocks.SEAGRASS)
                )
            );
    }
}
