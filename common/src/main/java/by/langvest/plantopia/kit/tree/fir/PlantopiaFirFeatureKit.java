package by.langvest.plantopia.kit.tree.fir;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaPineconeBlock;
import by.langvest.plantopia.kit.special.PlantopiaKit;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaMegaFirTreeConfiguration;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaFruitDecorator;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.MEGA;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.configuredFeature;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.simpleProvider;

@ParametersAreNonnullByDefault
public class PlantopiaFirFeatureKit extends PlantopiaKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;
    public final ResourceKey<ConfiguredFeature<?, ?>> megaTree;

    public PlantopiaFirFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves
    ) {
        Supplier<TreeDecorator> pineconeDecorator = () -> new PlantopiaFruitDecorator(
            simpleProvider(PlantopiaBlocks.PINE_CONE.get().defaultBlockState().setValue(PlantopiaPineconeBlock.DIRECTION, Direction.UP)),
            ConstantInt.of(1),
            ConstantInt.of(130),
            Direction.UP
        );

        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(configuredFeature(PlantopiaFeatureTypes.FIR_TREE, context ->
                    new PlantopiaMegaFirTreeConfiguration(
                        simpleProvider(log.get()),
                        simpleProvider(leaves.get()),
                        UniformInt.of(10, 20),
                        PlantopiaIntProportion.relative(
                            UniformFloat.of(0.2F, 0.3F)
                        ),
                        ConstantInt.of(1),
                        List.of(pineconeDecorator.get())
                    )
                ))
        );

        this.megaTree = PlantopiaFeatures.declareFeature(
            compileNameFrom(MEGA, baseName),
            PlantopiaFeatureDeclaration.builder()
                .feature(configuredFeature(PlantopiaFeatureTypes.FIR_TREE, context ->
                    new PlantopiaMegaFirTreeConfiguration(
                        simpleProvider(log.get()),
                        simpleProvider(leaves.get()),
                        UniformInt.of(20, 40),
                        PlantopiaIntProportion.relative(
                            UniformFloat.of(0.2F, 0.3F)
                        ),
                        ConstantInt.of(2),
                        List.of(pineconeDecorator.get())
                    )
                ))
        );
    }
}
