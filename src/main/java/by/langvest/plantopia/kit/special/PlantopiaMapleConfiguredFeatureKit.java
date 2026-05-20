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

public class PlantopiaMapleConfiguredFeatureKit extends PlantopiaTreeConfiguredFeatureKit {
    public final ResourceKey<ConfiguredFeature<?, ?>> treeBees0002litter055;
    public final ResourceKey<ConfiguredFeature<?, ?>> fancyTreeBees0002litter055;

    public PlantopiaMapleConfiguredFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        ResourceKey<PlacedFeature> leafLitterPlacement,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, log, leaves, config);

        var supposedTreeBees0002PlacedFeature = PlantopiaPlacementUtils.createKey(treeBees0002.location().getPath());
        var supposedFancyTreeBees0002PlacedFeature = PlantopiaPlacementUtils.createKey(fancyTreeBees0002.location().getPath());

        this.treeBees0002litter055 = PlantopiaFeatures.declareFeature(
            compileNameFrom(treeBees0002, LITTER, CHANCE_055),
            PlantopiaFeatureDeclaration.builder()
                .feature(composite(context -> {
                    var placements = lookupPlacements(context);

                    return new PlantopiaCompositeConfiguration(
                        placements.getOrThrow(supposedTreeBees0002PlacedFeature),
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

                    return new PlantopiaCompositeConfiguration(
                        placements.getOrThrow(supposedFancyTreeBees0002PlacedFeature),
                        List.of(
                            new WeightedPlacedFeature(placements.getOrThrow(leafLitterPlacement), CHANCE_055)
                        )
                    );
                }))
        );
    }
}
