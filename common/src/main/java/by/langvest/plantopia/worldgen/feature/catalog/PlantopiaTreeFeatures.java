package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaCompositeConfiguration;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.OptionalInt;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.*;
import static by.langvest.plantopia.worldgen.value.PlantopiaProviderUtils.simpleProvider;

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public interface PlantopiaTreeFeatures {
    Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<ConfiguredFeature<?, ?>> HUGE_WITCHY_TOADSTOOL = declareFeature(
        compileNameFrom(HUGE, PlantopiaBlocks.WITCHY_TOADSTOOL),
        PlantopiaFeatureDeclaration.builder()
            .feature(configuredFeature(PlantopiaFeatureTypes.HUGE_WITCHY_TOADSTOOL, context ->
                new HugeMushroomFeatureConfiguration(
                    BlockStateProvider.simple(
                        PlantopiaBlocks.WITCHY_TOADSTOOL_BLOCK.get().defaultBlockState()
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ),
                    BlockStateProvider.simple(
                        Blocks.MUSHROOM_STEM.defaultBlockState()
                            .setValue(HugeMushroomBlock.UP, false)
                            .setValue(HugeMushroomBlock.DOWN, false)
                    ),
                    1
                )
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_OAK = declareFeature(
        compileNameFrom(SEASONAL, TreeFeatures.DARK_OAK),
        PlantopiaFeatureDeclaration.builder()
            .feature(tree(context ->
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
            .feature(tree(context ->
                createSimpleAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.yellowLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> YELLOW_ASPEN_BEES_0002 = declareFeature(
        compileNameFrom(YELLOW_ASPEN, BEES, CHANCE_0002),
        PlantopiaFeatureDeclaration.builder()
            .feature(tree(context ->
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
            .feature(tree(context ->
                createTinyAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.yellowLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> RED_ASPEN = declareFeature(
        "red_aspen",
        PlantopiaFeatureDeclaration.builder()
            .feature(tree(context ->
                createSimpleAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.redLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TINY_RED_ASPEN = declareFeature(
        compileNameFrom(TINY, RED_ASPEN),
        PlantopiaFeatureDeclaration.builder()
            .feature(tree(context ->
                createTinyAspenTree(Blocks.BIRCH_LOG, PlantopiaKits.MAPLE.redLeaves.get())
                    .ignoreVines()
                    .decorators(List.of(BIRCH_BASE_LOG_DECORATOR))
                    .build()
            ))
    );
}
