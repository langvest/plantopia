package by.langvest.plantopia.kit.tree.fir;

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
public class PlantopiaFirPlacementKit extends PlantopiaAbstractTreePlacementKit {
    public final ResourceKey<PlacedFeature> tree;
    public final ResourceKey<PlacedFeature> megaTree;

    public PlantopiaFirPlacementKit(
        String baseName,
        @NotNull PlantopiaFirFeatureKit feature,
        Supplier<Block> sapling
    ) {
        this.tree = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.tree, CHECKED),
            checkedTreeDeclaration(feature.tree, sapling)
        );

        this.megaTree = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.megaTree, CHECKED),
            checkedTreeDeclaration(feature.megaTree, sapling)
        );
    }
}
