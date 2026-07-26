package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaCypressFoliagePlacer;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaLushFoliagePlacer;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaAlterBaseLogDecorator;
import by.langvest.plantopia.worldgen.feature.trunkplacer.PlantopiaStraightTrunkPlacer;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaRelativeIntProportion;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BushFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.*;

/**
 * @see net.minecraft.data.worldgen.features.TreeFeatures
 */
public final class PlantopiaTreeFeatureUtils {
    public static final TreeDecorator BEEHIVE_DECORATOR_005 = new BeehiveDecorator(CHANCE_005);
    public static final TreeDecorator BEEHIVE_DECORATOR_0002 = new BeehiveDecorator(CHANCE_0002);
    public static final TreeDecorator BIRCH_BASE_LOG_DECORATOR = new PlantopiaAlterBaseLogDecorator(simpleProvider(PlantopiaBlocks.BIRCH_BASE_LOG.get()));

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createCherryTree(Block logBlock, Block leavesBlock) {
        var weightedRandomList = weightedListInt(values -> values
            .add(ConstantInt.of(1), 1)
            .add(ConstantInt.of(2), 1)
            .add(ConstantInt.of(3), 1)
        );
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new CherryTrunkPlacer(7, 1, 0, weightedRandomList, UniformInt.of(2, 4), UniformInt.of(-4, -3), UniformInt.of(-1, 0)),
            BlockStateProvider.simple(leavesBlock),
            new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F),
            new TwoLayersFeatureSize(1, 0, 2)
        );
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleTree(Block logBlock, Block leavesBlock) {
        return createStraightBlobTree(logBlock, leavesBlock, 4, 2, 0, ConstantInt.of(2));
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleLushTree(Block logBlock, Block leavesBlock) {
        return createLushTree(
            logBlock, // logBlock
            leavesBlock, // leavesBlock
            weightedListInt(values -> values
                .add(ConstantInt.of(8), 3)
                .add(ConstantInt.of(10), 2)
            ), // baseHeight
            UniformInt.of(0, 2), // heightRand
            ConstantInt.of(2), // foliageRadius
            PlantopiaIntProportion.relative(
                ConstantFloat.of(0.925F),
                ConstantInt.of(8),
                ConstantInt.of(10)
            ) // foliageHeight
        );
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createSimpleAspenTree(Block logBlock, Block leavesBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            simpleProvider(logBlock), // logBlock
            new PlantopiaStraightTrunkPlacer(
                weightedListInt(values -> values
                    .add(UniformInt.of(9, 10), 3)
                    .add(UniformInt.of(11, 12), 2)
                ), // baseHeight
                UniformInt.of(0, 2) // heightRand
            ),
            simpleProvider(leavesBlock), // leavesBlock
            new PlantopiaCypressFoliagePlacer(
                ConstantInt.of(2), // foliageRadius
                ConstantInt.of(2), // foliageOffset
                PlantopiaIntProportion.relative(
                    UniformFloat.of(0.82F, 0.86F),
                    ConstantInt.of(8)
                ), // foliageHeight
                ConstantInt.of(2) // foliageTipStep
            ),
            new TwoLayersFeatureSize(
                4, // heightThreshold
                0, // lowerRadius
                2 // upperRadius
            )
        );
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createTinyAspenTree(Block logBlock, Block leavesBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            simpleProvider(logBlock), // logBlock
            new PlantopiaStraightTrunkPlacer(
                ConstantInt.of(4), // baseHeight
                UniformInt.of(0, 2) // heightRand
            ),
            simpleProvider(leavesBlock), // leavesBlock
            new PlantopiaCypressFoliagePlacer(
                ConstantInt.of(1), // foliageRadius
                ConstantInt.of(2), // foliageOffset
                PlantopiaIntProportion.relative(
                    weightedListFloat(values -> values
                        .add(ConstantFloat.of(1.0F), 1)
                        .add(ConstantFloat.of(1.2F), 1)
                    ),
                    ConstantInt.of(6)
                ), // foliageHeight
                ConstantInt.of(2) // foliageTipStep
            ),
            new TwoLayersFeatureSize(
                2, // heightThreshold
                0, // lowerRadius
                1 // upperRadius
            )
        );
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createCypressTree(Block logBlock, Block leavesBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            simpleProvider(logBlock), // logBlock
            new PlantopiaStraightTrunkPlacer(
                UniformInt.of(7, 8), // baseHeight
                UniformInt.of(0, 2) // heightRand
            ),
            simpleProvider(leavesBlock), // leavesBlock
            new PlantopiaCypressFoliagePlacer(
                ConstantInt.of(1), // foliageRadius
                ConstantInt.of(3), // foliageOffset
                PlantopiaIntProportion.relative(
                    ConstantFloat.of(1.233334F),
                    ConstantInt.of(8)
                ), // foliageHeight
                ConstantInt.of(3) // foliageTipStep
            ),
            new TwoLayersFeatureSize(
                2, // heightThreshold
                0, // lowerRadius
                1 // upperRadius
            )
        );
    }

    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createBushTree(Block logBlock, Block leavesBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            simpleProvider(logBlock), // logBlock
            new StraightTrunkPlacer(1, 0, 0),
            simpleProvider(leavesBlock), // leavesBlock
            new BushFoliagePlacer(
                ConstantInt.of(2), // foliageRadius
                ConstantInt.of(1), // foliageOffset
                2 // foliageHeight
            ),
            new TwoLayersFeatureSize(0, 0, 0)
        );
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

    @Contract("_, _, _, _, _, _ -> new")
    public static TreeConfiguration.@NotNull TreeConfigurationBuilder createLushTree(Block logBlock, Block leavesBlock, IntProvider baseHeight, IntProvider heightRand, IntProvider foliageRadius, PlantopiaRelativeIntProportion foliageHeight) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(logBlock),
            new PlantopiaStraightTrunkPlacer(baseHeight, heightRand),
            BlockStateProvider.simple(leavesBlock),
            new PlantopiaLushFoliagePlacer(foliageRadius, ConstantInt.of(2), foliageHeight),
            new TwoLayersFeatureSize(4, 0, 2)
        );
    }
}
