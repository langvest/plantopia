package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaLeafLitterBlock;
import by.langvest.plantopia.util.PlantopiaIntegerPropertyHolder;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaGradientBlockPlacer;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaRadialPatchConfiguration;
import by.langvest.toolkit.collection.catalog.Catalog;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.patchNameOf;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedListInt;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedProvider;

public interface PlantopiaSeasonalFeatures {
    Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_YELLOW_LEAF_LITTER = declareFeature(
        patchNameOf(PlantopiaBlocks.YELLOW_LEAF_LITTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getLeafLitterConfig(PlantopiaBlocks.YELLOW_LEAF_LITTER)))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_ORANGE_LEAF_LITTER = declareFeature(
        patchNameOf(PlantopiaBlocks.ORANGE_LEAF_LITTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getLeafLitterConfig(PlantopiaBlocks.ORANGE_LEAF_LITTER)))
    );

    ResourceKey<ConfiguredFeature<?, ?>> PATCH_RED_LEAF_LITTER = declareFeature(
        patchNameOf(PlantopiaBlocks.RED_LEAF_LITTER),
        PlantopiaFeatureDeclaration.builder()
            .feature(radialPatch(getLeafLitterConfig(PlantopiaBlocks.RED_LEAF_LITTER)))
    );

    /* HELPER METHODS *************************************************************************/

    @Contract(pure = true)
    static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> getLeafLitterConfig(Supplier<Block> leafLitterBlock) {
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
}
