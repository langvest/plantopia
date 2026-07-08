package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.createKey;

public interface PlantopiaPlacements extends
    PlantopiaVegetationPlacements,
    PlantopiaMiscOverworldPlacements,
    PlantopiaCavePlacements,
    PlantopiaMarshPlacements,
    PlantopiaSeasonalPlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaVegetationPlacements.DECLARATION,
        PlantopiaMiscOverworldPlacements.DECLARATION,
        PlantopiaCavePlacements.DECLARATION,
        PlantopiaMarshPlacements.DECLARATION,
        PlantopiaSeasonalPlacements.DECLARATION
    ));

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }
}
