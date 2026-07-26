package by.langvest.plantopia.kit.tree.jacaranda;

import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeFeatureKit;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.BEES;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.*;

@ParametersAreNonnullByDefault
public class PlantopiaJacarandaFeatureKit extends PlantopiaAbstractTreeFeatureKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees005;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees0002;

    public PlantopiaJacarandaFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves
    ) {
        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createCherryTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.treeBees005 = PlantopiaFeatures.declareFeature(
            compileNameFrom(baseName, BEES, CHANCE_005),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createCherryTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_005))
                        .build()
                ))
        );

        this.treeBees0002 = PlantopiaFeatures.declareFeature(
            compileNameFrom(baseName, BEES, CHANCE_0002),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createCherryTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_0002))
                        .build()
                ))
        );
    }
}
