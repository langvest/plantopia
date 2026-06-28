package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCloverBlock;
import by.langvest.plantopia.block.special.PlantopiaLeafLitterBlock;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaIntegerPropertyHolder;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaGradientBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaSimpleBlockPlacer;
import by.langvest.plantopia.worldgen.feature.config.*;
import by.langvest.plantopia.worldgen.placement.PlantopiaMultiNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdType;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaSeasonalPlacements;
import by.langvest.toolkit.collection.catalog.Catalog;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public final class PlantopiaVegetationFeatures {
    public static final Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    public static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_HOGWEED = declareFeature(
        singleNameOf(PlantopiaBlocks.HOGWEED),
        PlantopiaFeatureDeclaration.builder()
            .feature(naturalBlock(context ->
                simpleConfig(PlantopiaBlocks.HOGWEED.get())
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> HOGWEED_COLONY = declareFeature(
        compileNameFrom(PlantopiaBlocks.HOGWEED, COLONY),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.HOGWEED_COLONY))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_MOSS_VEGETATION = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEA_MOSS_PATCH_BONEMEAL = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TINY_CACTUS_ON_SAND = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CHICORY = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TANSY = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CARROTWEED = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CARROTWEED_MOUNTAIN = declareFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.CARROTWEED), MOUNTAIN),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(56, 5, 3, PlacementUtils.filtered(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREWEED_MOUNTAIN = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FERN = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GIANT_GRASS = declareFeature(
        patchNameOf(PlantopiaBlocks.GIANT_GRASS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                FeatureUtils.simplePatchConfiguration(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.GIANT_GRASS.get().defaultBlockState(), 2)
                        .add(Blocks.TALL_GRASS.defaultBlockState(), 5)
                    )
                )))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_AZURE_BLUET = declareFeature(
        patchNameOf(Blocks.AZURE_BLUET),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                FeatureUtils.simpleRandomPatchConfiguration(
                    46,
                    PlacementUtils.onlyWhenEmpty(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        simpleConfig(Blocks.AZURE_BLUET)
                    )
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_REED = declareFeature(
        patchNameOf(PlantopiaBlocks.REED),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(24, 3, 1, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.REED.get()),
                    WATER_PlANT_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CATTAIL = declareFeature(
        patchNameOf(PlantopiaBlocks.CATTAIL),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(84, 5, 1, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> states
                        .add(PlantopiaBlocks.CATTAIL.get().defaultBlockState(), 4)
                        .add(PlantopiaBlocks.SWEET_FLAG.get().defaultBlockState(), 1)
                    ),
                    WATER_PlANT_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SWEET_FLAG = declareFeature(
        patchNameOf(PlantopiaBlocks.SWEET_FLAG),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(84, 5, 1, PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.SWEET_FLAG.get()),
                    WATER_PlANT_PREDICATE
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DUNE_GRASS = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SNOWDROP = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_ORANGE_WILDFLOWERS_JUNGLE = declareFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.ORANGE_WILDFLOWERS), JUNGLE),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(42, 5, 2, PlacementUtils.onlyWhenEmpty(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.ORANGE_WILDFLOWERS.get())
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LAVENDER = declareFeature(
        patchNameOf(PlantopiaBlocks.LAVENDER),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(42, 3, 2, PlacementUtils.onlyWhenEmpty(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.LAVENDER.get())
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> QUAGMIRE_WATER_LEVEL = declareFeature(
        compileNameFrom("quagmire_water_level"),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.QUAGMIRE_WATER_LEVEL, context ->
                new PlantopiaQuagmireConfiguration(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FLOWERING_LILY_PAD = declareFeature(
        patchNameOf("flowering_lily_pad"),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(getFloweringWaterlilyConfig(() -> List.of(
                PlantopiaBlocks.WHITE_FLOWERING_LILY_PAD.get().defaultBlockState(),
                PlantopiaBlocks.RED_FLOWERING_LILY_PAD.get().defaultBlockState(),
                PlantopiaBlocks.YELLOW_FLOWERING_LILY_PAD.get().defaultBlockState(),
                PlantopiaBlocks.PINK_FLOWERING_LILY_PAD.get().defaultBlockState()
            ))))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FLOWERING_LILY_PAD_MARSH = declareFeature(
        compileNameFrom(patchNameOf("flowering_lily_pad"), PlantopiaOverworldBiomes.MARSH),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(getFloweringWaterlilyConfig(() -> List.of(
                PlantopiaBlocks.WHITE_FLOWERING_LILY_PAD.get().defaultBlockState(),
                PlantopiaBlocks.PINK_FLOWERING_LILY_PAD.get().defaultBlockState()
            ))))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FLOWERING_LILY_PAD_DEAD_MARSH = declareFeature(
        compileNameFrom(patchNameOf("flowering_lily_pad"), PlantopiaOverworldBiomes.DEAD_MARSH),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(getFloweringWaterlilyConfig(() -> List.of(
                PlantopiaBlocks.RED_FLOWERING_LILY_PAD.get().defaultBlockState(),
                PlantopiaBlocks.YELLOW_FLOWERING_LILY_PAD.get().defaultBlockState()
            ))))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FLOWERING_SMALL_PLATTERLEAF = declareFeature(
        patchNameOf("flowering_small_platterleaf"),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(getFloweringWaterlilyConfig(() -> List.of(
                PlantopiaBlocks.WHITE_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState(),
                PlantopiaBlocks.RED_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState(),
                PlantopiaBlocks.YELLOW_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState(),
                PlantopiaBlocks.PINK_FLOWERING_SMALL_PLATTERLEAF.get().defaultBlockState()
            ))))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SMALL_PLATTERLEAF = declareFeature(
        patchNameOf(PlantopiaBlocks.SMALL_PLATTERLEAF),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomPatch(context ->
                new RandomPatchConfiguration(6, 7, 3, PlacementUtils.onlyWhenEmpty(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    simpleConfig(PlantopiaBlocks.SMALL_PLATTERLEAF.get())
                ))
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BRANCHING_SHRUB = declareFeature(
        patchNameOf(PlantopiaBlocks.BRANCHING_SHRUB),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.BRANCHING_SHRUB_PATCH, context ->
                new PlantopiaBranchingShrubPatchConfiguration(
                    weightedListInt(values -> values
                        .add(ConstantInt.of(1), 1)
                        .add(ConstantInt.of(2), 2)
                        .add(ConstantInt.of(3), 3)
                        .add(ConstantInt.of(4), 1)
                    ), // xzSpread
                    ConstantInt.of(1), // ySpread
                    UniformInt.of(22, 32), // tries
                    weightedListInt(values -> values
                        .add(ConstantInt.of(4), 1)
                        .add(ConstantInt.of(1), 3)
                        .add(ConstantInt.of(2), 4)
                        .add(ConstantInt.of(3), 3)
                    ), // height
                    UniformFloat.of(0.58F, 0.78F), // heightFalloff
                    ConstantFloat.of(0.364F), // heightErosion
                    ConstantFloat.of(-0.548F), // shapeSigma
                    ConstantFloat.of(0.332F), // shapeErosion
                    ConstantInt.of(6), // searchDistance
                    BlockPredicate.allOf(
                        BlockPredicate.matchesTag(PlantopiaBlockTags.BRANCHING_SHRUB_CAN_GENERATE_ON),
                        BlockPredicate.not(BlockPredicate.matchesBlocks(PlantopiaBlocks.SEA_MOSS_BLOCK.get()))
                    ),
                    BRANCHING_SHRUB_VERTICAL_PREDICATE.get(),
                    BRANCHING_SHRUB_HORIZONTAL_PREDICATE.get(),
                    List.of(Direction.UP)
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BRANCHING_SHRUB_CAVE = declareFeature(
        compileNameFrom(patchNameOf(PlantopiaBlocks.BRANCHING_SHRUB), CAVE),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.BRANCHING_SHRUB_PATCH, context ->
                new PlantopiaBranchingShrubPatchConfiguration(
                    weightedListInt(values -> values
                        .add(ConstantInt.of(2), 1)
                        .add(ConstantInt.of(3), 2)
                        .add(ConstantInt.of(4), 3)
                        .add(ConstantInt.of(5), 1)
                    ), // xzSpread
                    ConstantInt.of(1), // ySpread
                    UniformInt.of(32, 42), // tries
                    weightedListInt(values -> values
                        .add(ConstantInt.of(5), 1)
                        .add(ConstantInt.of(2), 3)
                        .add(ConstantInt.of(3), 4)
                        .add(ConstantInt.of(4), 5)
                    ), // height
                    UniformFloat.of(0.48F, 0.58F), // heightFalloff
                    ConstantFloat.of(0.364F), // heightErosion
                    ConstantFloat.of(-0.548F), // shapeSigma
                    ConstantFloat.of(0.332F), // shapeErosion
                    ConstantInt.of(12), // searchDistance
                    BlockPredicate.allOf(
                        BlockPredicate.matchesTag(PlantopiaBlockTags.BRANCHING_SHRUB_CAN_GENERATE_ON),
                        BlockPredicate.not(BlockPredicate.matchesBlocks(PlantopiaBlocks.SEA_MOSS_BLOCK.get()))
                    ),
                    BRANCHING_SHRUB_VERTICAL_PREDICATE.get(),
                    BRANCHING_SHRUB_HORIZONTAL_PREDICATE.get(),
                    List.of(Direction.DOWN, Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_FOREST_VEGETATION = declareFeature(
        compileNameFrom(PlantopiaOverworldBiomes.SEASONAL_DARK_FOREST, VEGETATION),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var features = lookupFeatures(context);
                var placements = lookupPlacements(context);

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM)), 0.025F),
                        new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)), 0.05F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaSeasonalPlacements.SEASONAL_DARK_OAK_LITTER_055), 0.6666667F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.BIRCH_CHECKED), 0.2F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_CHECKED), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.OAK_CHECKED)
                );
            }))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_SEASONAL_FOREST = declareFeature(
        compileNameFrom(TREES, PlantopiaOverworldBiomes.SEASONAL_FOREST),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var yellowMaple = PlantopiaKits.MAPLE.yellowFeature.placed;
                var orangeMaple = PlantopiaKits.MAPLE.orangeFeature.placed;
                var redMaple = PlantopiaKits.MAPLE.redFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED), 0.03F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.OAK_BEES_0002), 0.05F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.01F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.treeBees0002litter055), 0.25F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.fancyTreeBees0002litter055), 0.1F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.treeBees0002litter055), 0.25F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.fancyTreeBees0002litter055), 0.1F),
                        new WeightedPlacedFeature(placements.getOrThrow(orangeMaple.fancyTreeBees0002litter055), 0.1F)
                    ),
                    placements.getOrThrow(orangeMaple.treeBees0002litter055)
                );
            }))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_LAVENDER_FIELDS = declareFeature(
        compileNameFrom(TREES, PlantopiaOverworldBiomes.LAVENDER_FIELDS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var jacaranda = PlantopiaKits.JACARANDA.feature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(jacaranda.treeBees0002), 0.7F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.OAK_BEES_0002)
                );
            }))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_YELLOW_LEAF_LITTER = declareFeature(
        patchNameOf(PlantopiaBlocks.YELLOW_LEAF_LITTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getLeafLitterConfig(PlantopiaBlocks.YELLOW_LEAF_LITTER)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_ORANGE_LEAF_LITTER = declareFeature(
        patchNameOf(PlantopiaBlocks.ORANGE_LEAF_LITTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getLeafLitterConfig(PlantopiaBlocks.ORANGE_LEAF_LITTER)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_LEAF_LITTER = declareFeature(
        patchNameOf(PlantopiaBlocks.RED_LEAF_LITTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getLeafLitterConfig(PlantopiaBlocks.RED_LEAF_LITTER)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CLOVER = declareFeature(
        patchNameOf(PlantopiaBlocks.CLOVER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getCloverConfig(null)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_WHITE_CLOVER_BLOSSOM = declareFeature(
        patchNameOf(PlantopiaBlocks.WHITE_CLOVER_BLOSSOM),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getCloverConfig(PlantopiaBlocks.WHITE_CLOVER_BLOSSOM)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_PINK_CLOVER_BLOSSOM = declareFeature(
        patchNameOf(PlantopiaBlocks.PINK_CLOVER_BLOSSOM),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getCloverConfig(PlantopiaBlocks.PINK_CLOVER_BLOSSOM)))
    );

    /* HELPER METHODS *************************************************************************/

    @Contract(pure = true)
    public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> getLeafLitterConfig(Supplier<Block> leafLitterBlock) {
        return context -> {
            List<PlantopiaBlockPlacer> blocks = Lists.newArrayList();

            blocks.add(new PlantopiaGradientBlockPlacer(
                weightedProvider(states -> {
                    for (var direction : Direction.Plane.HORIZONTAL) {
                        var state = leafLitterBlock.get().defaultBlockState()
                            .setValue(PlantopiaLeafLitterBlock.FACING, direction);

                        states.add(state, 1);
                    }

                    return states;
                }),
                PlantopiaIntegerPropertyHolder.of(PlantopiaLeafLitterBlock.AMOUNT),
                0.68D,
                49
            ));

            return new PlantopiaRadialPatchConfiguration(
                ConstantInt.of(92), // tries
                weightedListInt(values -> values
                    .add(UniformInt.of(5, 9), 2)
                    .add(UniformInt.of(4, 8), 5)
                ), // xzSpread
                ConstantInt.of(3), // ySpread
                -0.292D, // sigma
                0.236D, // erosion
                blocks,
                Optional.of(GRASS_PLANT_PREDICATE),
                Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)
            );
        };
    }

    @Contract(pure = true)
    public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> getCloverConfig(@Nullable Supplier<Block> flowerBlock) {
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
                UniformInt.of(5, 9), // xzSpread
                ConstantInt.of(3), // ySpread
                -0.232D, // sigma
                0.242D, // erosion
                blocks,
                Optional.of(GRASS_PLANT_PREDICATE),
                Optional.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)
            );
        };
    }

    @Contract(pure = true)
    public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, RandomPatchConfiguration> getFloweringWaterlilyConfig(Supplier<List<BlockState>> flowerBlocks) {
        return context -> new RandomPatchConfiguration(6, 7, 1, PlacementUtils.onlyWhenEmpty(
            PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
            new SimpleBlockConfiguration(
                new DualNoiseProvider(
                    new InclusiveRange<>(1, 3),
                    new NormalNoise.NoiseParameters(-10, 1.0D),
                    1.0F,
                    2345L,
                    new NormalNoise.NoiseParameters(-3, 1.0D),
                    1.0F,
                    flowerBlocks.get()
                )
            )
        ));
    }
}
