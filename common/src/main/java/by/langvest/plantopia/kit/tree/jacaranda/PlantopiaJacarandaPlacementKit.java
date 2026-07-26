package by.langvest.plantopia.kit.tree.jacaranda;

import by.langvest.plantopia.kit.special.PlantopiaAbstractTreePlacementKit;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.checkedTreeDeclaration;

@ParametersAreNonnullByDefault
public class PlantopiaJacarandaPlacementKit extends PlantopiaAbstractTreePlacementKit {
    public final ResourceKey<PlacedFeature> treeBees0002;

    public PlantopiaJacarandaPlacementKit(
        String baseName,
        @NotNull PlantopiaJacarandaFeatureKit feature,
        Supplier<Block> sapling
    ) {
        this.treeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(feature.treeBees0002),
            checkedTreeDeclaration(feature.treeBees0002, sapling)
        );
    }
}
