package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.SNOWY;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.biome.PlantopiaBiomeUtils.createKey;
import static by.langvest.plantopia.worldgen.biome.catalog.PlantopiaSeasonalBiomes.ASPEN_CLEARING;
import static by.langvest.plantopia.worldgen.biome.catalog.PlantopiaSeasonalBiomes.ASPEN_GROVE;

public interface PlantopiaColdBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    ResourceKey<Biome> SNOWY_ASPEN_GROVE = declareBiome(
        compileNameFrom(SNOWY, ASPEN_GROVE),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .addSpawn(MobCategory.CREATURE, EntityType.WOLF, 8, 4, 4)
            .addSpawn(MobCategory.CREATURE, EntityType.RABBIT, 4, 2, 3)
            .addSpawn(MobCategory.CREATURE, EntityType.FOX, 8, 2, 4)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .creatureGenerationProbability(0.07F)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addFerns)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(-0.5F)
            .downfall(0.4F)
            .grassColorOverride("#c06459")
            .foliageColorOverride("#b3795a")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> SNOWY_ASPEN_CLEARING = declareBiome(
        compileNameFrom(SNOWY, ASPEN_CLEARING),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::plainsSpawns)
            .creatureGenerationProbability(0.07F)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addDefaultGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(-0.5F)
            .downfall(0.4F)
            .grassColorOverride("#c06459")
            .foliageColorOverride("#b3795a")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );
}
