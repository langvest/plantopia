package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.PlantopiaDictionary.MUDDY;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.biome.PlantopiaBiomeUtils.createKey;

public interface PlantopiaRiverBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    ResourceKey<Biome> GRAVELLY_RIVER = declareBiome(
        compileNameFrom(GRAVELLY, Biomes.RIVER),
        PlantopiaBiomeDeclaration.builder()
            .addSpawn(MobCategory.WATER_CREATURE, EntityType.SQUID, 2, 1, 4)
            .addSpawn(MobCategory.WATER_AMBIENT, EntityType.SALMON, 5, 1, 5)
            .addSpawn(MobCategory.MONSTER, EntityType.DROWNED, 100, 1, 1)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.5F)
            .grassColorOverride("#8EB971")
            .foliageColorOverride("#71A74D")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );

    ResourceKey<Biome> SANDY_RIVER = declareBiome(
        compileNameFrom(SANDY, Biomes.RIVER),
        PlantopiaBiomeDeclaration.builder()
            .addSpawn(MobCategory.WATER_CREATURE, EntityType.SQUID, 2, 1, 4)
            .addSpawn(MobCategory.WATER_AMBIENT, EntityType.SALMON, 5, 1, 5)
            .addSpawn(MobCategory.MONSTER, EntityType.DROWNED, 100, 1, 1)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.5F)
            .grassColorOverride("#8EB971")
            .foliageColorOverride("#71A74D")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );

    ResourceKey<Biome> MUDDY_RIVER = declareBiome(
        compileNameFrom(MUDDY, Biomes.RIVER),
        PlantopiaBiomeDeclaration.builder()
            .addSpawn(MobCategory.WATER_CREATURE, EntityType.SQUID, 2, 1, 4)
            .addSpawn(MobCategory.WATER_AMBIENT, EntityType.SALMON, 5, 1, 5)
            .addSpawn(MobCategory.MONSTER, EntityType.DROWNED, 100, 1, 1)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addWaterTrees)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addDefaultGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_RIVER)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.5F)
            .grassColorOverride("#8fb354")
            .foliageColorOverride("#71A74D")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );
}
