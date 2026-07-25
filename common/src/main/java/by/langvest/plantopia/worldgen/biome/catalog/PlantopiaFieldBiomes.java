package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.FIELDS;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.biome.PlantopiaBiomeUtils.createKey;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
public interface PlantopiaFieldBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    ResourceKey<Biome> LAVENDER_FIELDS = declareBiome(
        compileNameFrom(PlantopiaBlocks.LAVENDER, FIELDS),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::plainsSpawns)
            .creatureGenerationProbability(0.07F)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addPlainGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.4F)
            .grassColorOverride("#a5c96e")
            .foliageColorOverride("#a1cb61")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );

    ResourceKey<Biome> POPPY_FIELDS = declareBiome(
        compileNameFrom(Blocks.POPPY, FIELDS),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .addSpawn(MobCategory.CREATURE, EntityType.HORSE, 1, 2, 6)
            .addSpawn(MobCategory.CREATURE, EntityType.DONKEY, 1, 1, 1)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .creatureGenerationProbability(0.07F)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addSavannaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addWarmFlowers)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(2.0F)
            .downfall(0.0F)
            .grassColorOverride("#d5cb64")
            .foliageColorOverride("#aacb53")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );
}
