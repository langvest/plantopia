package by.langvest.plantopia.kit.tree.maple;

import by.langvest.plantopia.kit.special.PlantopiaAbstractTreePlacementKit;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.checkedTreeDeclaration;

@ParametersAreNonnullByDefault
public class PlantopiaMaplePlacementKit extends PlantopiaAbstractTreePlacementKit {
    public final ResourceKey<PlacedFeature> treeBees0002;
    public final ResourceKey<PlacedFeature> treeBees0002litter055;

    public final ResourceKey<PlacedFeature> fancyTreeBees0002;
    public final ResourceKey<PlacedFeature> fancyTreeBees0002litter055;

    public final ResourceKey<PlacedFeature> lushTreeBees0002;
    public final ResourceKey<PlacedFeature> lushTreeBees0002litter055;

    public PlantopiaMaplePlacementKit(
        String baseName,
        PlantopiaMapleFeatureKit feature,
        Supplier<Block> sapling
    ) {
        super();

        this.treeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.treeBees0002),
            checkedTreeDeclaration(feature.treeBees0002, sapling)
        );

        this.treeBees0002litter055 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.treeBees0002litter055),
            checkedTreeDeclaration(feature.treeBees0002litter055, sapling)
        );

        this.fancyTreeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.fancyTreeBees0002),
            checkedTreeDeclaration(feature.fancyTreeBees0002, sapling)
        );

        this.fancyTreeBees0002litter055 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.fancyTreeBees0002litter055),
            checkedTreeDeclaration(feature.fancyTreeBees0002litter055, sapling)
        );

        this.lushTreeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.lushTreeBees0002),
            checkedTreeDeclaration(feature.lushTreeBees0002, sapling)
        );

        this.lushTreeBees0002litter055 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.lushTreeBees0002litter055),
            checkedTreeDeclaration(feature.lushTreeBees0002litter055, sapling)
        );
    }
}
