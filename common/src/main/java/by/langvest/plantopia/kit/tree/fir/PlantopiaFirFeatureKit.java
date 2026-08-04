package by.langvest.plantopia.kit.tree.fir;

import by.langvest.plantopia.kit.special.PlantopiaKit;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaFirTreeConfiguration;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.MEGA;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.configuredFeature;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.UPWARD_PINE_CONE_DECORATOR_0015;
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
        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(configuredFeature(PlantopiaFeatureTypes.FIR_TREE, context ->
                    new PlantopiaFirTreeConfiguration(
                        simpleProvider(log.get()),
                        UniformInt.of(9, 11),
                        ConstantInt.of(1),
                        simpleProvider(leaves.get()),
                        PlantopiaIntProportion.relative(
                            UniformFloat.of(0.82F, 0.92F),
                            ConstantInt.of(8),
                            ConstantInt.of(10)
                        ),
                        ConstantInt.of(1),
                        List.of(UPWARD_PINE_CONE_DECORATOR_0015.get())
                    )
                ))
        );

        this.megaTree = PlantopiaFeatures.declareFeature(
            compileNameFrom(MEGA, baseName),
            PlantopiaFeatureDeclaration.builder()
                .feature(configuredFeature(PlantopiaFeatureTypes.FIR_TREE, context ->
                    new PlantopiaFirTreeConfiguration(
                        simpleProvider(log.get()),
                        UniformInt.of(25, 35),
                        ConstantInt.of(2),
                        simpleProvider(leaves.get()),
                        PlantopiaIntProportion.relative(
                            UniformFloat.of(0.76F, 0.82F)
                        ),
                        ConstantInt.of(1),
                        List.of(UPWARD_PINE_CONE_DECORATOR_0015.get())
                    )
                ))
        );
    }
}
