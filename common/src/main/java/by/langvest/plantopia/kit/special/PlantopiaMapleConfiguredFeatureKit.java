package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaCompositeConfiguration;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.BEEHIVE_DECORATOR_0002;
import static by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatureUtils.createSimpleLushTree;

public class PlantopiaMapleConfiguredFeatureKit extends PlantopiaTreeConfiguredFeatureKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> lushTreeBees0002;
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees0002litter055;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees0002litter055;
    public final ResourceKey<ConfiguredFeature<?, ?>> lushTreeBees0002litter055;

    public PlantopiaMapleConfiguredFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        ResourceKey<PlacedFeature> leafLitterPlacement,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, log, leaves, config);

        this.lushTreeBees0002 = PlantopiaFeatures.declareFeature(
            "lush_" + baseName + "_bees_0002",
            PlantopiaFeatureDeclaration.builder()
                .feature(deciduousTree(context ->
                    createSimpleLushTree(log.get(), leaves.get())
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
