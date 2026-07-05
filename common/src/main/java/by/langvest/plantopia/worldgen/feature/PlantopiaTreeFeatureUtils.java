package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaLushFoliagePlacer;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;
import java.util.function.Function;

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public final class PlantopiaTreeFeatureUtils {
    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createCherryTree(Block logBlock, Block leavesBlock) {
        var weightedRandomList = SimpleWeightedRandomList.<IntProvider>builder()
            .add(ConstantInt.of(1), 1)
            .add(ConstantInt.of(2), 1)
            .add(ConstantInt.of(3), 1)
            .build();

        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new CherryTrunkPlacer(7, 1, 0, new WeightedListInt(weightedRandomList), UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)),
            BlockStateProvider.simple(leavesBlock),
            new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F),
            new TwoLayersFeatureSize(1, 0, 2)
        );
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleTree(Block logBlock, Block leavesBlock) {
        return createStraightBlobTree(logBlock, leavesBlock, 4, 2, 0, ConstantInt.of(2));
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleLushTree(Block logBlock, Block leavesBlock) {
        return createLushTree(logBlock, leavesBlock, 8, 2, 0, ConstantInt.of(2), ConstantInt.of(4));
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createTallLushTree(Block logBlock, Block leavesBlock) {
        return createLushTree(logBlock, leavesBlock, 10, 2, 0, ConstantInt.of(2), ConstantInt.of(6));
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleFancyTree(Block logBlock, Block leavesBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new FancyTrunkPlacer(3, 11, 0),
            BlockStateProvider.simple(leavesBlock),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
            new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        );
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createStraightBlobTree(Block logBlock, Block leavesBlock, int baseHeight, int heightRandA, int heightRandB, IntProvider foliageRadius) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
            BlockStateProvider.simple(leavesBlock),
            new BlobFoliagePlacer(foliageRadius, ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        );
    }

    @Contract("_, _, _, _, _, _, _ -> new")
    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createLushTree(Block logBlock, Block leavesBlock, int baseHeight, int heightRandA, int heightRandB, IntProvider foliageRadius, IntProvider foliageBaseHeight) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
            BlockStateProvider.simple(leavesBlock),
            new PlantopiaLushFoliagePlacer(foliageRadius, ConstantInt.of(2), foliageBaseHeight),
            new TwoLayersFeatureSize(4, 0, 2)
        );
    }

    /* PROVIDER *********************************************/

    @Contract(pure = true)
    public static @NotNull WeightedListInt weightedListInt(@NotNull Function<SimpleWeightedRandomList.Builder<IntProvider>, SimpleWeightedRandomList.Builder<IntProvider>> values) {
        return new WeightedListInt(values.apply(SimpleWeightedRandomList.builder()).build());
    }
}
