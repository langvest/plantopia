package by.langvest.plantopia.kit.tree.palm;

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
public class PlantopiaPalmPlacementKit extends PlantopiaAbstractTreePlacementKit {
    public final ResourceKey<PlacedFeature> tree;

    public PlantopiaPalmPlacementKit(
        String baseName,
        @NotNull PlantopiaPalmFeatureKit feature,
        Supplier<Block> sapling
    ) {
        this.tree = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.tree, CHECKED),
            checkedTreeDeclaration(feature.tree, sapling)
        );
    }
}
