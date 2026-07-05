package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;

import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.*;

public class PlantopiaTreeConfiguredFeatureKit extends PlantopiaKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees005;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees0002;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTree;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees005;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees0002;

    public static final BeehiveDecorator beehiveDecorator005 = new BeehiveDecorator(0.05F);
    public static final BeehiveDecorator beehiveDecorator0002 = new BeehiveDecorator(0.002F);

    public PlantopiaTreeConfiguredFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        PlantopiaTreeKitConfiguration config
    ) {
        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.treeBees005 = PlantopiaFeatures.declareFeature(
            baseName + "_bees_005",
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(beehiveDecorator005))
                        .build()
                ))
        );

        this.treeBees0002 = PlantopiaFeatures.declareFeature(
            baseName + "_bees_0002",
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(beehiveDecorator0002))
                        .build()
                ))
        );

        this.fancyTree = PlantopiaFeatures.declareFeature(
            "fancy_" + baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.fancyTreeBees005 = PlantopiaFeatures.declareFeature(
            "fancy_" + baseName + "_bees_005",
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(beehiveDecorator005))
                        .build()
                ))
        );

        this.fancyTreeBees0002 = PlantopiaFeatures.declareFeature(
            "fancy_" + baseName + "_bees_0002",
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(beehiveDecorator0002))
                        .build()
                ))
        );
    }
}
