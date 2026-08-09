package by.langvest.plantopia.kit.tree.maple;

import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeFeatureKit;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaCompositeConfiguration;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaBranchDecorator;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.createSimpleFancyTree;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.simpleProvider;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedProvider;

@ParametersAreNonnullByDefault
public class PlantopiaMapleFeatureKit extends PlantopiaAbstractTreeFeatureKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> tree;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees005;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees0002;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees0002litter055;

    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTree;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees005;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees0002;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees0002litter055;

    public final ResourceKey<ConfiguredFeature<?, ?>> lushTreeBees0002;
    public final ResourceKey<ConfiguredFeature<?, ?>> lushTreeBees0002litter055;

    public PlantopiaMapleFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> balk,
        Supplier<Block> stub,
        ResourceKey<PlacedFeature> leafLitterPlacement
    ) {
        super();

        Supplier<TreeDecorator> fancyBranchDecorator = () -> PlantopiaBranchDecorator.builder(CHANCE_01, 1)
            .add(PlantopiaIntProportion.fixed(5), weightedProvider(states -> states
                .add(balk.get().defaultBlockState(), 3)
                .add(stub.get().defaultBlockState(), 2)
            ))
            .add(PlantopiaIntProportion.fixed(4), simpleProvider(stub.get()))
            .build();

        Supplier<TreeDecorator> lushBranchDecorator = () -> PlantopiaBranchDecorator.builder(CHANCE_01, 1)
            .add(PlantopiaIntProportion.fixed(4), simpleProvider(stub.get()))
            .build();

        this.tree = PlantopiaFeatures.declareFeature(
            baseName,
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .build()
                ))
        );

        this.treeBees005 = PlantopiaFeatures.declareFeature(
            compileNameFrom(baseName, BEES, CHANCE_005),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_005))
                        .build()
                ))
        );

        this.treeBees0002 = PlantopiaFeatures.declareFeature(
            compileNameFrom(baseName, BEES, CHANCE_0002),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_0002))
                        .build()
                ))
        );

        this.treeBees0002litter055 = PlantopiaFeatures.declareFeature(
            compileNameFrom(treeBees0002, LITTER, CHANCE_055),
            PlantopiaFeatureDeclaration.builder()
                .feature(composite(context -> {
                    var placements = lookupPlacements(context);
                    var treeBees0002Placement = PlantopiaPlacementUtils.createKey(treeBees0002.location().getPath());

                    return new PlantopiaCompositeConfiguration(
                        placements.getOrThrow(treeBees0002Placement),
                        List.of(
                            new WeightedPlacedFeature(placements.getOrThrow(leafLitterPlacement), CHANCE_055)
                        )
                    );
                }))
        );

        this.fancyTree = PlantopiaFeatures.declareFeature(
            compileNameFrom(FANCY, baseName),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(fancyBranchDecorator.get()))
                        .build()
                ))
        );

        this.fancyTreeBees005 = PlantopiaFeatures.declareFeature(
            compileNameFrom(FANCY, baseName, BEES, CHANCE_005),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_005, fancyBranchDecorator.get()))
                        .build()
                ))
        );

        this.fancyTreeBees0002 = PlantopiaFeatures.declareFeature(
            compileNameFrom(FANCY, baseName, BEES, CHANCE_0002),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleFancyTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_0002, fancyBranchDecorator.get()))
                        .build()
                ))
        );

        this.fancyTreeBees0002litter055 = PlantopiaFeatures.declareFeature(
            compileNameFrom(fancyTreeBees0002, LITTER, CHANCE_055),
            PlantopiaFeatureDeclaration.builder()
                .feature(composite(context -> {
                    var placements = lookupPlacements(context);
                    var fancyTreeBees0002Placement = PlantopiaPlacementUtils.createKey(fancyTreeBees0002.location().getPath());

                    return new PlantopiaCompositeConfiguration(
                        placements.getOrThrow(fancyTreeBees0002Placement),
                        List.of(
                            new WeightedPlacedFeature(placements.getOrThrow(leafLitterPlacement), CHANCE_055)
                        )
                    );
                }))
        );

        this.lushTreeBees0002 = PlantopiaFeatures.declareFeature(
            compileNameFrom(LUSH, baseName, BEES, CHANCE_0002),
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleLushTree(log.get(), leaves.get())
                        .ignoreVines()
                        .decorators(List.of(BEEHIVE_DECORATOR_0002, lushBranchDecorator.get()))
                        .build()
                ))
        );

        this.lushTreeBees0002litter055 = PlantopiaFeatures.declareFeature(
            compileNameFrom(lushTreeBees0002, LITTER, CHANCE_055),
            PlantopiaFeatureDeclaration.builder()
                .feature(composite(context -> {
                    var placements = lookupPlacements(context);
                    var lushTreeBees0002Placement = PlantopiaPlacementUtils.createKey(lushTreeBees0002.location().getPath());

                    return new PlantopiaCompositeConfiguration(
                        placements.getOrThrow(lushTreeBees0002Placement),
                        List.of(
                            new WeightedPlacedFeature(placements.getOrThrow(leafLitterPlacement), CHANCE_055)
                        )
                    );
                }))
        );
    }
}
