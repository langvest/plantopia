package by.langvest.plantopia.worldgen.feature;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

public final class PlantopiaTreeFeatureUtils {
    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleTree(Block logBlock, Block leavesBlock) {
        return createStraightBlobTree(logBlock, leavesBlock, 4, 2, 0, 2);
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleFancyTree(Block logBlock, Block leavesBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new FancyTrunkPlacer(3, 11, 0),
            BlockStateProvider.simple(leavesBlock),
            new FancyFoliagePlacer(
                ConstantInt.of(2),
                ConstantInt.of(4),
                4
            ),
            new TwoLayersFeatureSize(
                0,
                0,
                0,
                OptionalInt.of(4)
            )
        );
    }

    @Contract("_, _, _, _, _, _ -> new")
    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createStraightBlobTree(Block logBlock, Block leavesBlock, int baseHeight, int heightRandA, int heightRandB, int foliageRadius) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
            BlockStateProvider.simple(leavesBlock),
            new BlobFoliagePlacer(
                ConstantInt.of(foliageRadius),
                ConstantInt.of(0),
                3
            ),
            new TwoLayersFeatureSize(
                1,
                0,
                1
            )
        );
    }
}
