package by.langvest.plantopia.worldgen.biome.catalog;

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
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.biome.PlantopiaBiomeUtils.createKey;

public interface PlantopiaSwampBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    ResourceKey<Biome> MARSH = declareBiome(
        "marsh",
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .addSpawn(MobCategory.CREATURE, EntityType.CHICKEN, 8, 2, 4)
            .addSpawn(MobCategory.CREATURE, EntityType.FROG, 6, 2, 4)
            .addSpawn(MobCategory.MONSTER, EntityType.SLIME, 1, 1, 1)
            .applyGeneration(BiomeDefaultFeatures::addFossilDecoration)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addSwampClayDisk)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP)
            .hasPrecipitation(true)
            .temperature(0.7F)
            .downfall(0.7F)
            .grassColorOverride("#80be52")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SWAMP))
    );

    ResourceKey<Biome> DEAD_MARSH = declareBiome(
        "dead_marsh",
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
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP)
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

    ResourceKey<Biome> FEN = declareBiome(
        "fen",
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
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.9F)
            .waterColor("#528d7c")
            .waterFogColor("#57a08a")
            .grassColorOverride("#9fb783")
            .foliageColorOverride("#68a054")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SWAMP))
    );
}
