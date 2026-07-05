package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.util.PlantopiaDictionary;
import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.biome.PlantopiaBiomeUtils.*;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
public interface PlantopiaOverworldBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    ResourceKey<Biome> MARSH = declareBiome(
        PlantopiaDictionary.MARSH,
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .addSpawn(MobCategory.CREATURE, EntityType.CHICKEN, 8, 2, 4)
            .addSpawn(MobCategory.CREATURE, EntityType.FROG, 6, 2, 4)
            .addSpawn(MobCategory.MONSTER, EntityType.SLIME, 1, 1, 1)
            .applyGeneration(BiomeDefaultFeatures::addFossilDecoration)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addSwampClayDisk)
            .addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP)
            .hasPrecipitation(true)
            .temperature(0.7F)
            .downfall(0.7F)
            .grassColorOverride("#80be52")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SWAMP))
    );

    ResourceKey<Biome> DEAD_MARSH = declareBiome(
        compileNameFrom(DEAD, MARSH),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .addSpawn(MobCategory.CREATURE, EntityType.PIG, 8, 2, 4)
            .addSpawn(MobCategory.CREATURE, EntityType.CHICKEN, 8, 2, 4)
            .addSpawn(MobCategory.MONSTER, EntityType.SLIME, 1, 1, 1)
            .addSpawn(MobCategory.CREATURE, EntityType.FROG, 6, 2, 4)
            .applyGeneration(BiomeDefaultFeatures::addFossilDecoration)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addSwampClayDisk)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
            .addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.9F)
            .waterColor("#617B64")
            .waterFogColor("#232317")
            .fogColor("#C0D8FF")
            .grassColorOverride("#a39255")
            .foliageColorOverride("#b7965b")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SWAMP))
    );

    ResourceKey<Biome> SEASONAL_FOREST = declareBiome(
        compileNameFrom(SEASONAL, Biomes.FOREST),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .addSpawn(MobCategory.CREATURE, EntityType.FOX, 5, 4, 4)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addForestFlowers)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addForestGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.7F)
            .downfall(0.6F)
            .grassColorOverride("#bf9b3d") // Old: #c1a741
            .foliageColorOverride("#adac3b") // Old: #adac37
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> SEASONAL_DARK_FOREST = declareBiome(
        compileNameFrom(SEASONAL, Biomes.DARK_FOREST),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addForestFlowers)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addForestGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .grassColorOverride("#6a7a32")
            .foliageColorOverride("#74aa2e")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> BOREAL_WOODS = declareBiome(
        compileNameFrom(BOREAL, WOODS),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .addSpawn(MobCategory.CREATURE, EntityType.WOLF, 8, 4, 4)
            .addSpawn(MobCategory.CREATURE, EntityType.RABBIT, 4, 2, 3)
            .addSpawn(MobCategory.CREATURE, EntityType.FOX, 8, 2, 4)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addFerns)
            .applyGeneration(BiomeDefaultFeatures::addForestFlowers)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .applyGeneration(BiomeDefaultFeatures::addCommonBerryBushes)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .grassColorOverride("#7a9f48")
            .foliageColorOverride("#87a52c")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

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

    /* HELPER METHODS *************************************************************************************************/

    static void globalOverworldGeneration(BiomeGenerationSettings.Builder generationBuilder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generationBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generationBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generationBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generationBuilder);
        BiomeDefaultFeatures.addDefaultSprings(generationBuilder);
        BiomeDefaultFeatures.addSurfaceFreezing(generationBuilder);
    }
}
