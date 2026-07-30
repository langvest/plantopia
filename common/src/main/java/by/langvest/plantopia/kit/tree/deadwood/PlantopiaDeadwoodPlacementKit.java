package by.langvest.plantopia.kit.tree.deadwood;

import by.langvest.plantopia.kit.special.PlantopiaAbstractTreePlacementKit;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaDictionary.CHECKED;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.checkedTreeDeclaration;

@ParametersAreNonnullByDefault
public class PlantopiaDeadwoodPlacementKit extends PlantopiaAbstractTreePlacementKit {
    public final ResourceKey<PlacedFeature> tree;
    public final ResourceKey<PlacedFeature> fancyTree;

    public PlantopiaDeadwoodPlacementKit(
        String baseName,
        @NotNull PlantopiaDeadwoodFeatureKit feature,
        Supplier<Block> sapling
    ) {
        this.tree = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.tree, CHECKED),
            checkedTreeDeclaration(feature.tree, sapling)
        );

        this.fancyTree = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.fancyTree, CHECKED),
            checkedTreeDeclaration(feature.fancyTree, sapling)
        );
    }
}
