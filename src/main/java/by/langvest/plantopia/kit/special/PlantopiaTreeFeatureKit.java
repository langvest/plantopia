package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.PlantopiaKit;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaTreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.worldgen.feature.catalog.PlantopiaTreeFeatures.*;

public class PlantopiaTreeFeatureKit extends PlantopiaKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees005;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTree;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees005;

    public final AbstractTreeGrower treeGrower;

    protected PlantopiaTreeFeatureKit(String baseName, Supplier<Block> log, Supplier<Block> leaves, PlantopiaTreeKitConfiguration config) {
        var beehiveDecorator005 = new BeehiveDecorator(0.05F);

        this.tree = PlantopiaTreeFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.treeBees005 = PlantopiaTreeFeatures.declareFeature(
            baseName + "_bees_005",
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(beehiveDecorator005))
                        .build()
                ))
        );

        this.fancyTree = PlantopiaTreeFeatures.declareFeature(
            "fancy_" + baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.fancyTreeBees005 = PlantopiaTreeFeatures.declareFeature(
            "fancy_" + baseName + "_bees_005",
            PlantopiaFeatureDeclaration.builder()
                .feature(tree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(beehiveDecorator005))
                        .build()
                ))
        );

        this.treeGrower = createTreeGrower();
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull PlantopiaTreeFeatureKit registerTreeFeatureKit(String baseName, Supplier<Block> log, Supplier<Block> leaves, PlantopiaTreeKitConfiguration config) {
        return new PlantopiaTreeFeatureKit(baseName, log, leaves, config);
    }

    protected AbstractTreeGrower createTreeGrower() {
        return new AbstractTreeGrower() {
            @Override
            protected @NotNull ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {
                if (random.nextInt(10) == 0) {
                    return hasFlowers ? fancyTreeBees005 : fancyTree;
                } else {
                    return hasFlowers ? treeBees005 : tree;
                }
            }
        };
    }
}
