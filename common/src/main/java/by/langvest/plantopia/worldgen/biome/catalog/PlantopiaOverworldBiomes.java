package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
public interface PlantopiaOverworldBiomes extends
    PlantopiaFieldBiomes,
    PlantopiaForestBiomes,
    PlantopiaRiverBiomes,
    PlantopiaSwampBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaFieldBiomes.DECLARATION,
        PlantopiaForestBiomes.DECLARATION,
        PlantopiaRiverBiomes.DECLARATION,
        PlantopiaSwampBiomes.DECLARATION
    ));

    /* HELPER METHODS *******************************************************/

    static void globalOverworldGeneration(BiomeGenerationSettings.Builder generationBuilder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generationBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generationBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generationBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generationBuilder);
        BiomeDefaultFeatures.addDefaultSprings(generationBuilder);
        BiomeDefaultFeatures.addSurfaceFreezing(generationBuilder);
    }
}
