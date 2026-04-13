package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCloverBlock;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaIntegerPropertyHolder;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaGradientBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaSimpleBlockPlacer;
import by.langvest.plantopia.worldgen.feature.config.*;
import by.langvest.plantopia.worldgen.placement.PlantopiaMultiNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.DualNoiseProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class PlantopiaVegetationFeatures extends PlantopiaFeatures {
    protected static final BlockPredicate WATER_PlANT_PREDICATE = BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GRASS, Blocks.SEAGRASS);
    protected static final BlockPredicate BRANCHING_SHRUB_VERTICAL_PREDICATE = BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GLOW_LICHEN, Blocks.SEAGRASS, Blocks.GRASS, Blocks.FERN);

    protected static final BlockPredicate BRANCHING_SHRUB_HORIZONTAL_PREDICATE = BlockPredicate.allOf(
        BRANCHING_SHRUB_VERTICAL_PREDICATE,
        BlockPredicate.anyOf(
            BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.BRANCHING_SHRUB.get()),
            BlockPredicate.replaceable(BlockPos.ZERO.below())
        )
    );

    protected static final BlockPredicate GRASS_PLANT_PREDICATE = BlockPredicate.allOf(
        BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS),
        BlockPredicate.solid(BlockPos.ZERO.below())
    );

    private static final Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareConfiguredFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_HOGWEED = declareConfiguredFeature(
        singleNameOf(PlantopiaBlocks.HOGWEED),
        PlantopiaFeatureDeclaration.builder()
            .feature(naturalBlock(context ->
                simpleConfig(PlantopiaBlocks.HOGWEED.get())
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_MOSS_VEGETATION = declareConfiguredFeature(
        compileNameFrom(PlantopiaBlockMeta.MetaType.SEA_MOSS, VEGETATION),
        PlantopiaFeatureDeclaration.builder()
            .feature(naturalBlock(context ->
                weightedConfig(states -> {
                    states.add(PlantopiaBlocks.SEA_MOSS_CARPET.get().defaultBlockState(), 25);
                    states.add(Blocks.SEAGRASS.defaultBlockState(), 50);
                    states.add(Blocks.TALL_SEAGRASS.defaultBlockState(), 10);

                    SeaPickleBlock.PICKLES.getPossibleValues().forEach(pickles ->
                        states.add(Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, pickles), 1)
                    );

                    return states;
                })
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_MOSS_PATCH_BONEMEAL = declareConfiguredFeature(
        compileNameFrom(PlantopiaBlockMeta.MetaType.SEA_MOSS, PATCH, BONEMEAL),
        PlantopiaFeatureDeclaration.builder()
            .feature(vegetationPatch(context ->
                new PlantopiaVegetationPatchConfiguration(
                    BlockPredicate.anyOf(
                        BlockPredicate.matchesFluids(Fluids.WATER),
                        BlockPredicate.matchesBlocks(Blocks.WATER)
                    ),
                    PlantopiaBlockTags.SEA_MOSS_REPLACEABLE,
                    simpleProvider(PlantopiaBlocks.SEA_MOSS_BLOCK.get()),
                    PlacementUtils.inlinePlaced(
                        lookupFeatures(context).getOrThrow(SEA_MOSS_VEGETATION)
                    ),
                    CaveSurface.FLOOR,
                    ConstantInt.of(1),
                    0.0F,
                    5,
                    0.6F,
                    UniformInt.of(1, 2),
                    0.75F
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TINY_CACTUS_ON_SAND = declareConfiguredFeature(
        compileNameFrom(PATCH, PlantopiaBlocks.TINY_CACTUS, ON_SAND),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(8, 6, 2, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.TINY_CACTUS.get().defaultBlockState(), 5)
                        .add(PlantopiaBlocks.FLOWERING_TINY_CACTUS.get().defaultBlockState(), 2)
                    ),
                    ON_SAND_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CHICORY = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.CHICORY),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(context ->
                new PlantopiaLimitedRandomPatchConfiguration(
                    ConstantInt.of(8),
                    ClampedInt.of(UniformInt.of(2, 4), 3, 4),
                    ConstantInt.of(3),
                    ConstantInt.of(2),
                    PlacementUtils.filtered(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        simpleConfig(PlantopiaBlocks.CHICORY.get()),
                        GRASS_PLANT_PREDICATE
                    )
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TANSY = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.TANSY),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(context ->
                new PlantopiaLimitedRandomPatchConfiguration(
                    UniformInt.of(10, 14),
                    ConstantInt.of(6),
                    ConstantInt.of(2),
                    ConstantInt.of(1),
                    PlacementUtils.filtered(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        simpleConfig(PlantopiaBlocks.TANSY.get()),
                        GRASS_PLANT_PREDICATE
                    )
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CARROTWEED = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.CARROTWEED),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(context ->
                new PlantopiaLimitedRandomPatchConfiguration(
                    UniformInt.of(16, 18),
                    ConstantInt.of(6),
                    ConstantInt.of(3),
                    ConstantInt.of(2),
                    PlacementUtils.filtered(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        simpleConfig(PlantopiaBlocks.CARROTWEED.get()),
                        GRASS_PLANT_PREDICATE
                    )
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CARROTWEED_MOUNTAIN = declareConfiguredFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.CARROTWEED), MOUNTAIN),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(60, 5, 3, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.CARROTWEED.get().defaultBlockState(), 10)
                        .add(Blocks.TALL_GRASS.defaultBlockState(), 2)
                        .add(Blocks.GRASS.defaultBlockState(), 1)
                    ),
                    BlockPredicate.allOf(
                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                        BlockPredicate.solid(BlockPos.ZERO.below())
                    )
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREWEED_MOUNTAIN = declareConfiguredFeature(
        compileNameFrom(PATCH, PlantopiaBlocks.FIREWEED, MOUNTAIN),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(60, 5, 3, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.FIREWEED.get().defaultBlockState(), 10)
                        .add(Blocks.TALL_GRASS.defaultBlockState(), 2)
                        .add(Blocks.GRASS.defaultBlockState(), 1)
                    ),
                    GRASS_PLANT_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FERN = declareConfiguredFeature(
        patchNameOf(Blocks.FERN),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(32, 6, 2, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(Blocks.FERN),
                    GRASS_PLANT_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_REEDS = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.REEDS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(26, 3, 1, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.REEDS.get()),
                    WATER_PlANT_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CATTAIL = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.CATTAIL),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(96, 6, 1, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.CATTAIL.get().defaultBlockState(), 4)
                        .add(PlantopiaBlocks.SWEET_FLAG.get().defaultBlockState(), 1)
                    ),
                    BlockPredicate.allOf(
                        WATER_PlANT_PREDICATE,
                        BlockPredicate.not(
                            BlockPredicate.matchesTag(BlockTags.ICE)
                        )
                    )
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SWEET_FLAG = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.SWEET_FLAG),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(96, 6, 1, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.SWEET_FLAG.get()),
                    BlockPredicate.allOf(
                        WATER_PlANT_PREDICATE,
                        BlockPredicate.not(
                            BlockPredicate.matchesTag(BlockTags.ICE)
                        )
                    )
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DUNE_GRASS = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.DUNE_GRASS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(64, 3, 2, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.DUNE_GRASS.get().defaultBlockState(), 5)
                        .add(PlantopiaBlocks.TALL_DUNE_GRASS.get().defaultBlockState(), 2)
                    ),
                    ON_SAND_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SNOWDROP = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.SNOWDROP),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(96, 6, 3, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.SNOWDROP.get()),
                    BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS, Blocks.SNOW)
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_ORANGE_WILDFLOWERS_JUNGLE = declareConfiguredFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.ORANGE_WILDFLOWERS), JUNGLE),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                FeatureUtils.simpleRandomPatchConfiguration(
                    64,
                    PlacementUtils.onlyWhenEmpty(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        simpleConfig(PlantopiaBlocks.ORANGE_WILDFLOWERS.get())
                    )
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> QUAGMIRE_WATER_LEVEL = declareConfiguredFeature(
        compileNameFrom("quagmire_water_level"),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.QUAGMIRE_WATER_LEVEL, context ->
                new PlantopiaAzollaAndMossConfiguration(
                    PlantopiaMultiNoiseConfig.builder()
                        .add(PlantopiaNoiseConfig.of(0.362D, 719, 112), 1)
                        .add(PlantopiaNoiseConfig.of(0.158D, 32, 775), 0.42)
                        .add(PlantopiaNoiseConfig.of(0.074D, 78, 21), 0.24)
                        .build(),
                    PlantopiaThresholdType.BELOW,
                    -0.012F,
                    0.64F,
                    3,
                    BlockPredicate.allOf(
                        BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS, Blocks.VINE),
                        BlockPredicate.anyOf(
                            BlockPredicate.matchesFluids(BlockPos.ZERO.below(), Fluids.WATER),
                            BlockPredicate.hasSturdyFace(BlockPos.ZERO.below(), Direction.UP)
                        )
                    ),
                    lookupBiomes(context).getOrThrow(PlantopiaBiomeTags.ALLOWS_QUAGMIRE)
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FLOWERING_LILY_PAD = declareConfiguredFeature(
        patchNameOf("flowering_lily_pad"),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(6, 7, 1, PlacementUtils.onlyWhenEmpty(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    new SimpleBlockConfiguration(
                        new DualNoiseProvider(
                            new InclusiveRange<>(1, 3),
                            new NormalNoise.NoiseParameters(-10, 1.0D),
                            1.0F,
                            2345L,
                            new NormalNoise.NoiseParameters(-3, 1.0D),
                            1.0F,
                            List.of(
                                PlantopiaBlocks.WHITE_FLOWERING_LILY_PAD.get().defaultBlockState(),
                                PlantopiaBlocks.RED_FLOWERING_LILY_PAD.get().defaultBlockState(),
                                PlantopiaBlocks.YELLOW_FLOWERING_LILY_PAD.get().defaultBlockState(),
                                PlantopiaBlocks.PINK_FLOWERING_LILY_PAD.get().defaultBlockState()
                            )
                        )
                    )
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FLOWERING_SMALL_PLATTERLEAF = declareConfiguredFeature(
        patchNameOf("flowering_small_platterleaf"),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(6, 7, 1, PlacementUtils.onlyWhenEmpty(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    new SimpleBlockConfiguration(
                        new DualNoiseProvider(
                            new InclusiveRange<>(1, 3),
                            new NormalNoise.NoiseParameters(-10, 1.0D),
                            1.0F,
                            2345L,
                            new NormalNoise.NoiseParameters(-3, 1.0D),
                            1.0F,
                            List.of(
                                PlantopiaBlocks.WHITE_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState(),
                                PlantopiaBlocks.RED_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState(),
                                PlantopiaBlocks.YELLOW_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState(),
                                PlantopiaBlocks.PINK_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState()
                            )
                        )
                    )
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SMALL_PLATTERLEAF = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.SMALL_PLATTERLEAF),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(6, 7, 3, PlacementUtils.onlyWhenEmpty(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.SMALL_PLATTERLEAF.get())
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BRANCHING_SHRUB = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.BRANCHING_SHRUB),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.BRANCHING_SHRUB_PATCH, context ->
                new PlantopiaBranchingShrubPatchConfiguration(
                    ConstantInt.of(2), // xzSpread
                    ConstantInt.of(1), // ySpread
                    ConstantInt.of(32), // tries
                    ClampedInt.of(UniformInt.of(1, 4), 3, 4), // height
                    UniformFloat.of(0.58F, 0.72F), // heightFalloff
                    ConstantFloat.of(0.248F), // heightErosion
                    ConstantFloat.of(-0.164F), // shapeSigma
                    ConstantFloat.of(0.148F), // shapeErosion
                    ConstantInt.of(6), // searchDistance
                    BlockPredicate.matchesTag(PlantopiaBlockTags.BRANCHING_SHRUB_CAN_GENERATE_ON),
                    BRANCHING_SHRUB_VERTICAL_PREDICATE,
                    BRANCHING_SHRUB_HORIZONTAL_PREDICATE,
                    List.of(Direction.UP)
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BRANCHING_SHRUB_CAVE = declareConfiguredFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.BRANCHING_SHRUB), CAVE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.BRANCHING_SHRUB_PATCH, context ->
                new PlantopiaBranchingShrubPatchConfiguration(
                    ConstantInt.of(3), // xzSpread
                    ConstantInt.of(1), // ySpread
                    ConstantInt.of(32), // tries
                    ClampedInt.of(UniformInt.of(3, 5), 4, 5), // height
                    UniformFloat.of(0.58F, 0.72F), // heightFalloff
                    ConstantFloat.of(0.228F), // heightErosion
                    ConstantFloat.of(-0.232F), // shapeSigma
                    ConstantFloat.of(0.126F), // shapeErosion
                    ConstantInt.of(12), // searchDistance
                    BlockPredicate.matchesTag(PlantopiaBlockTags.BRANCHING_SHRUB_CAN_GENERATE_ON),
                    BRANCHING_SHRUB_VERTICAL_PREDICATE,
                    BRANCHING_SHRUB_HORIZONTAL_PREDICATE,
                    List.of(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CLOVER = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.CLOVER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getCloverConfig(null)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_WHITE_CLOVER_BLOSSOM = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.WHITE_CLOVER_BLOSSOM),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getCloverConfig(PlantopiaBlocks.WHITE_CLOVER_BLOSSOM)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PINK_CLOVER_BLOSSOM = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.PINK_CLOVER_BLOSSOM),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getCloverConfig(PlantopiaBlocks.PINK_CLOVER_BLOSSOM)))
    );

    /* HELPER METHODS *************************************************************************/

    @Contract(pure = true)
    private static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> getCloverConfig(@Nullable Supplier<Block> flowerBlock) {
        return context -> {
            List<PlantopiaBlockPlacer> blocks = Lists.newArrayList();

            blocks.add(new PlantopiaGradientBlockPlacer(
                weightedProvider(states -> {
                    for (var direction : Direction.Plane.HORIZONTAL) {
                        var state = PlantopiaBlocks.CLOVER.get().defaultBlockState()
                            .setValue(PlantopiaCloverBlock.FACING, direction);

                        states.add(state, 1);
                    }

                    return states;
                }),
                PlantopiaIntegerPropertyHolder.of(PlantopiaCloverBlock.AMOUNT),
                0.68D,
                49
            ));

            if (flowerBlock != null) {
                blocks.add(new PlantopiaSimpleBlockPlacer(
                    simpleProvider(flowerBlock.get()),
                    2
                ));
            }

            return new PlantopiaRadialPatchConfiguration(
                ConstantInt.of(96), // tries
                UniformInt.of(5,9), // xzSpread
                ConstantInt.of(3), // ySpread
                -0.232D, // sigma
                0.242D, // erosion
                blocks,
                Optional.of(GRASS_PLANT_PREDICATE),
                Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)
            );
        };
    }
}
