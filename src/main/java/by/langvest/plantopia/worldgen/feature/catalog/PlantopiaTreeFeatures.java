package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaCompositeConfiguration;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaSeasonalPlacements;
import by.langvest.toolkit.util.Catalog;
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

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public final class PlantopiaTreeFeatures {
    public static final Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    public static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_WITCHY_TOADSTOOL = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_OAK = declareFeature(
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

    public static final ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_OAK_LITTER_055 = declareFeature(
        compileNameFrom(SEASONAL, TreeFeatures.DARK_OAK, LITTER, CHANCE_055),
        PlantopiaFeatureDeclaration.builder()
            .feature(composite(context -> {
                var placements = lookupPlacements(context);

                return new PlantopiaCompositeConfiguration(
                    placements.getOrThrow(PlantopiaSeasonalPlacements.SEASONAL_DARK_OAK_CHECKED),
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaSeasonalPlacements.PATCH_ORANGE_LEAF_LITTER_CHECKED), CHANCE_055)
                    )
                );
            }))
    );
}
