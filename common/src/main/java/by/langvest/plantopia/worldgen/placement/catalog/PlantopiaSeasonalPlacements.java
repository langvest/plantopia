package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public interface PlantopiaSeasonalPlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<PlacedFeature> PATCH_YELLOW_LEAF_LITTER_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_YELLOW_LEAF_LITTER, CHECKED),
        getCheckedLeafLitterDeclaration(PlantopiaFeatures.PATCH_YELLOW_LEAF_LITTER)
    );

    ResourceKey<PlacedFeature> PATCH_ORANGE_LEAF_LITTER_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_ORANGE_LEAF_LITTER, CHECKED),
        getCheckedLeafLitterDeclaration(PlantopiaFeatures.PATCH_ORANGE_LEAF_LITTER)
    );

    ResourceKey<PlacedFeature> PATCH_RED_LEAF_LITTER_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_RED_LEAF_LITTER, CHECKED),
        getCheckedLeafLitterDeclaration(PlantopiaFeatures.PATCH_RED_LEAF_LITTER)
    );
}
