package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public interface PlantopiaBiomes extends PlantopiaOverworldBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaOverworldBiomes.DECLARATION
    ));
}
