package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;

public interface PlantopiaFeatures extends
    PlantopiaVegetationFeatures,
    PlantopiaMiscOverworldFeatures,
    PlantopiaCaveFeatures,
    PlantopiaTreeFeatures {
    Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaVegetationFeatures.DECLARATION,
        PlantopiaMiscOverworldFeatures.DECLARATION,
        PlantopiaCaveFeatures.DECLARATION,
        PlantopiaTreeFeatures.DECLARATION
    ));

    static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }
}
