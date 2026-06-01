package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

public final class PlantopiaPlacements {
    public static final Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaVegetationPlacements.DECLARATION,
        PlantopiaMiscOverworldPlacements.DECLARATION,
        PlantopiaCavePlacements.DECLARATION,
        PlantopiaMarshPlacements.DECLARATION,
        PlantopiaSeasonalPlacements.DECLARATION
    ));

    public static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        DECLARATION.forEach((key, declaration) -> {
            var placedFeature = declaration.getPlacedFeature(context);

            context.register(key, placedFeature);
        });
    }
}
