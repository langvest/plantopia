package by.langvest.plantopia.kit.tree.palm;

import by.langvest.plantopia.kit.special.PlantopiaKit;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaBlobPalmFoliagePlacer;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaLushPalmFoliagePlacer;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaRandomSelectorFoliagePlacer;
import by.langvest.plantopia.worldgen.feature.trunkplacer.PlantopiaStraightTrunkPlacer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.deciduousTree;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.simpleProvider;

@ParametersAreNonnullByDefault
public class PlantopiaPalmFeatureKit extends PlantopiaKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;

    public PlantopiaPalmFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves
    ) {
        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    new TreeConfiguration.TreeConfigurationBuilder(
                        simpleProvider(log.get()), // logBlock
                        new PlantopiaStraightTrunkPlacer(
                            ConstantInt.of(10), // baseHeight
                            UniformInt.of(0, 2) // bonusHeight
                        ),
                        simpleProvider(leaves.get()), // leavesBlock
                        new PlantopiaRandomSelectorFoliagePlacer(
                            SimpleWeightedRandomList.<FoliagePlacer>builder()
                                .add(new PlantopiaLushPalmFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1)), 2)
                                .add(new PlantopiaBlobPalmFoliagePlacer(ConstantInt.of(2), ConstantInt.of(1)), 1)
                                .build()
                        ),
                        new TwoLayersFeatureSize(
                            6, // heightThreshold
                            0, // lowerRadius
                            2 // upperRadius
                        )
                    ).ignoreVines().build()
                ))
        );
    }
}
