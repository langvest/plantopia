package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

public class PlantopiaMaplePlacedFeatureKit extends PlantopiaTreePlacedFeatureKit {
    public final ResourceKey<PlacedFeature> lushTreeBees0002;
    public final ResourceKey<PlacedFeature> treeBees0002litter055;
    public final ResourceKey<PlacedFeature> fancyTreeBees0002litter055;
    public final ResourceKey<PlacedFeature> lushTreeBees0002litter055;

    public PlantopiaMaplePlacedFeatureKit(
        String baseName,
        PlantopiaMapleConfiguredFeatureKit configured,
        Supplier<Block> sapling,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, configured, sapling, config);

        this.lushTreeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(configured.lushTreeBees0002),
            PlantopiaPlacementDeclaration.builder()
                .feature(configured.lushTreeBees0002)
                .modifiers(context -> List.of(
                    PlacementUtils.filteredByBlockSurvival(sapling.get())
                ))
        );

        this.treeBees0002litter055 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(configured.treeBees0002litter055),
            PlantopiaPlacementDeclaration.builder()
                .feature(configured.treeBees0002litter055)
                .modifiers(context -> List.of(
                    PlacementUtils.filteredByBlockSurvival(sapling.get())
                ))
        );

        this.fancyTreeBees0002litter055 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(configured.fancyTreeBees0002litter055),
            PlantopiaPlacementDeclaration.builder()
                .feature(configured.fancyTreeBees0002litter055)
                .modifiers(context -> List.of(
                    PlacementUtils.filteredByBlockSurvival(sapling.get())
                ))
        );

        this.lushTreeBees0002litter055 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(configured.lushTreeBees0002litter055),
            PlantopiaPlacementDeclaration.builder()
                .feature(configured.lushTreeBees0002litter055)
                .modifiers(context -> List.of(
                    PlacementUtils.filteredByBlockSurvival(sapling.get())
                ))
        );
    }
}
