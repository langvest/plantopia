package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.foliageplacer.*;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaCompositeConfiguration;
import by.langvest.plantopia.worldgen.feature.trunkplacer.PlantopiaStraightTrunkPlacer;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalInt;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.*;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.*;

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public interface PlantopiaTreeFeatures {
    Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<ConfiguredFeature<?, ?>> HUGE_TOADSTOOL = declareFeature(
        compileNameFrom(HUGE, PlantopiaBlocks.TOADSTOOL),
        PlantopiaFeatureDeclaration.builder()
            .feature(mushroomTree(context ->
                new TreeConfiguration.TreeConfigurationBuilder(
                    simpleProvider(
                        Blocks.MUSHROOM_STEM.defaultBlockState()
                            .setValue(HugeMushroomBlock.UP, false)
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ), // logBlock
                    new PlantopiaStraightTrunkPlacer(
                        weightedListInt(values -> values
                            .add(UniformInt.of(8, 9), 3)
                            .add(UniformInt.of(10, 11), 2)
                        ) // baseHeight
                    ),
                    simpleProvider(
                        PlantopiaBlocks.TOADSTOOL_BLOCK.get().defaultBlockState()
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ), // leavesBlock
                    new PlantopiaToadstoolFoliagePlacer(
                        ConstantInt.of(1), // foliageRadius
                        ConstantInt.of(0), // foliageOffset
                        PlantopiaIntProportion.fixed(UniformInt.of(3, 4)) // foliageHeight
                    ),
                    new TwoLayersFeatureSize(
                        4, // heightThreshold
                        0, // lowerRadius
                        1 // upperRadius
                    )
                ).ignoreVines().build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> HUGE_WITCHY_TOADSTOOL = declareFeature(
        compileNameFrom(HUGE, PlantopiaBlocks.WITCHY_TOADSTOOL),
        PlantopiaFeatureDeclaration.builder()
            .feature(mushroomTree(context ->
                new TreeConfiguration.TreeConfigurationBuilder(
                    simpleProvider(
                        Blocks.MUSHROOM_STEM.defaultBlockState()
                            .setValue(HugeMushroomBlock.UP, false)
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ), // logBlock
                    new PlantopiaStraightTrunkPlacer(
                        UniformInt.of(5, 6) // baseHeight
                    ),
                    simpleProvider(
                        PlantopiaBlocks.WITCHY_TOADSTOOL_BLOCK.get().defaultBlockState()
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ), // leavesBlock
                    new PlantopiaWitchyToadstoolFoliagePlacer(
                        ConstantInt.of(1), // foliageRadius
                        ConstantInt.of(3), // foliageOffset
                        PlantopiaIntProportion.fixed(UniformInt.of(6, 7)) // foliageHeight
                    ),
                    new TwoLayersFeatureSize(
                        2, // heightThreshold
                        0, // lowerRadius
                        1 // upperRadius
                    )
                ).ignoreVines().build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> HUGE_CHANTERELLE = declareFeature(
        compileNameFrom(HUGE, PlantopiaBlocks.CHANTERELLE),
        PlantopiaFeatureDeclaration.builder()
            .feature(mushroomTree(context ->
                new TreeConfiguration.TreeConfigurationBuilder(
                    simpleProvider(PlantopiaBlocks.CHANTERELLE_BLOCK.get()), // logBlock
                    new PlantopiaStraightTrunkPlacer(
                        UniformInt.of(3, 4) // baseHeight
                    ),
                    simpleProvider(PlantopiaBlocks.CHANTERELLE_BLOCK.get()), // leavesBlock
                    new PlantopiaChanterelleFoliagePlacer(
                        ConstantInt.of(2), // foliageRadius
                        ConstantInt.of(1), // foliageOffset
                        PlantopiaIntProportion.fixed(ConstantInt.of(3)) // foliageHeight
                    ),
                    new TwoLayersFeatureSize(
                        2, // heightThreshold
                        0, // lowerRadius
                        2 // upperRadius
                    )
                ).ignoreVines().build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> HUGE_PORTOBELLO = declareFeature(
        compileNameFrom(HUGE, PlantopiaBlocks.PORTOBELLO),
        PlantopiaFeatureDeclaration.builder()
            .feature(mushroomTree(context ->
                new TreeConfiguration.TreeConfigurationBuilder(
                    simpleProvider(
                        Blocks.MUSHROOM_STEM.defaultBlockState()
                            .setValue(HugeMushroomBlock.UP, false)
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ), // logBlock
                    new PlantopiaStraightTrunkPlacer(
                        UniformInt.of(5, 6) // baseHeight
                    ),
                    simpleProvider(
                        PlantopiaBlocks.PORTOBELLO_BLOCK.get().defaultBlockState()
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ), // leavesBlock
                    new PlantopiaPortobelloFoliagePlacer(
                        ConstantInt.of(3), // foliageRadius
                        ConstantInt.of(0), // foliageOffset
                        PlantopiaIntProportion.fixed(ConstantInt.of(2)) // foliageHeight
                    ),
                    new TwoLayersFeatureSize(
                        4, // heightThreshold
                        0, // lowerRadius
                        3 // upperRadius
                    )
                ).ignoreVines().build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_OAK = declareFeature(
        compileNameFrom(SEASONAL, TreeFeatures.DARK_OAK),
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                new TreeConfiguration.TreeConfigurationBuilder(
                    simpleProvider(Blocks.DARK_OAK_LOG),
                    new DarkOakTrunkPlacer(6, 2, 1),
                    simpleProvider(PlantopiaKits.MAPLE.orangeLeaves.get()),
                    new DarkOakFoliagePlacer(
                        ConstantInt.of(0),
                        ConstantInt.of(0)
                    ),
                    new ThreeLayersFeatureSize(
                        1,
                        1,
                        0,
                        1,
                        2,
                        OptionalInt.empty()
                    )
                ).ignoreVines().build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_OAK_LITTER_055 = declareFeature(
        compileNameFrom(SEASONAL_DARK_OAK, LITTER, CHANCE_055),
        PlantopiaFeatureDeclaration.builder()
            .feature(composite(context -> {
                var placements = lookupPlacements(context);

                return new PlantopiaCompositeConfiguration(
                    placements.getOrThrow(PlantopiaPlacements.SEASONAL_DARK_OAK_CHECKED),
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.PATCH_ORANGE_LEAF_LITTER_CHECKED), CHANCE_055)
                    )
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> YELLOW_ASPEN = declareFeature(
        "yellow_aspen",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createSimpleAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.yellowLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> YELLOW_ASPEN_BEES_0002 = declareFeature(
        compileNameFrom(YELLOW_ASPEN, BEES, CHANCE_0002),
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createSimpleAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.yellowLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR, BEEHIVE_DECORATOR_0002))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> YELLOW_ASPEN_BEES_0002_LITTER_055 = declareFeature(
        compileNameFrom(YELLOW_ASPEN_BEES_0002, LITTER, CHANCE_055),
        PlantopiaFeatureDeclaration.builder()
            .feature(composite(context -> {
                var placements = lookupPlacements(context);

                return new PlantopiaCompositeConfiguration(
                    placements.getOrThrow(PlantopiaPlacements.YELLOW_ASPEN_BEES_0002),
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.PATCH_YELLOW_LEAF_LITTER_CHECKED), CHANCE_055)
                    )
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TINY_YELLOW_ASPEN = declareFeature(
        compileNameFrom(TINY, YELLOW_ASPEN),
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createTinyAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.yellowLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> RED_ASPEN = declareFeature(
        "red_aspen",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createSimpleAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.redLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TINY_RED_ASPEN = declareFeature(
        compileNameFrom(TINY, RED_ASPEN),
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createTinyAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.redLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> ACACIA_CYPRESS = declareFeature(
        "acacia_cypress",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createCypressTree(Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES)
                    .ignoreVines()
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> BIRCH_CYPRESS = declareFeature(
        "birch_cypress",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createCypressTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES)
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> SPRUCE_CYPRESS = declareFeature(
        "spruce_cypress",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createCypressTree(Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES)
                    .ignoreVines()
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TALL_OAK_BEES_0002 = declareFeature(
        compileNameFrom(TALL, "oak", BEES, CHANCE_0002),
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                new TreeConfiguration.TreeConfigurationBuilder(
                    simpleProvider(Blocks.OAK_LOG), // logBlock
                    new PlantopiaStraightTrunkPlacer(
                        ConstantInt.of(6), // baseHeight
                        UniformInt.of(0, 2) // bonusHeight
                    ),
                    simpleProvider(Blocks.OAK_LEAVES), // leavesBlock
                    new BlobFoliagePlacer(
                        ConstantInt.of(2), // foliageRadius
                        ConstantInt.of(0), // foliageOffset
                        3 // foliageHeight
                    ),
                    new TwoLayersFeatureSize(
                        4, // heightThreshold
                        0, // lowerRadius
                        2 // upperRadius
                    )
                ).decorators(List.of(BEEHIVE_DECORATOR_0002)).ignoreVines().build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> OAK_BUSH = declareFeature(
        "oak_bush",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createBushTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES)
                    .ignoreVines()
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> DEADWOOD_SPIRE = declareFeature(
        "deadwood_spire",
        PlantopiaFeatureDeclaration.builder()
            .feature(deciduousTree(context ->
                createSpireTree(PlantopiaKits.DEADWOOD.trunk.log.get(), PlantopiaKits.DEADWOOD.leaves.get())
                    .build()
            ))
    );
}
