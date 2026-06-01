package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;

public final class PlantopiaFeatures {
    public static final Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaVegetationFeatures.DECLARATION,
        PlantopiaMiscOverworldFeatures.DECLARATION,
        PlantopiaCaveFeatures.DECLARATION,
        PlantopiaTreeFeatures.DECLARATION
    ));

    public static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        DECLARATION.forEach((key, declaration) -> {
            var configuredFeature = declaration.getConfiguredFeature(context);

            context.register(key, configuredFeature);
        });
    }
}
