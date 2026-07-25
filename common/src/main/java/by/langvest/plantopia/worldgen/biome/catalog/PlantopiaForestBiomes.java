package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.biome.PlantopiaBiomeUtils.createKey;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
@ParametersAreNonnullByDefault
public interface PlantopiaForestBiomes {
    Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    ResourceKey<Biome> BOREAL_FOREST = declareBiome(
        compileNameFrom(BOREAL, Biomes.FOREST),
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
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .applyGeneration(BiomeDefaultFeatures::addCommonBerryBushes)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .grassColorOverride("#86ac4e")
            .foliageColorOverride("#80a434")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> MAPLE_WOODS = declareBiome(
        compileNameFrom(MAPLE, WOODS),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .addSpawn(MobCategory.CREATURE, EntityType.WOLF, 8, 4, 4)
            .addSpawn(MobCategory.CREATURE, EntityType.RABBIT, 4, 2, 3)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addFerns)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .applyGeneration(BiomeDefaultFeatures::addCommonBerryBushes)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .grassColorOverride("#75b07f")
            .foliageColorOverride("#67b181")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> AMBER_THICKET = declareBiome(
        compileNameFrom("amber", THICKET),
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
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .applyGeneration(BiomeDefaultFeatures::addCommonBerryBushes)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .grassColorOverride("#9eb44c")
            .foliageColorOverride("#889f32")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
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
            .grassColorOverride("#c79942")
            .foliageColorOverride("#adac3b")
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

    ResourceKey<Biome> ASPEN_GROVE = declareBiome(
        compileNameFrom("aspen", GROVE),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .addSpawn(MobCategory.CREATURE, EntityType.RABBIT, 4, 2, 3)
            .addSpawn(MobCategory.CREATURE, EntityType.FOX, 8, 2, 4)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addFerns)
            .applyGeneration(BiomeDefaultFeatures::addForestFlowers)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .apply(PlantopiaForestBiomes::aspenGroveColors)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> ASPEN_CLEARING = declareBiome(
        compileNameFrom("aspen", CLEARING),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::plainsSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addFerns)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .apply(PlantopiaForestBiomes::aspenGroveColors)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );

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
            .apply(PlantopiaForestBiomes::snowyAspenGroveColors)
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
            .apply(PlantopiaForestBiomes::snowyAspenGroveColors)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
    );

    ResourceKey<Biome> OAK_FOREST = declareBiome(
        compileNameFrom("oak", Biomes.FOREST),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addForestFlowers)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addForestGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.8F)
            .apply(PlantopiaForestBiomes::oakForestColors)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> OLD_GROWTH_OAK_FOREST = declareBiome(
        compileNameFrom(OLD_GROWTH, OAK_FOREST),
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::farmAnimals)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addFerns)
            .applyGeneration(BiomeDefaultFeatures::addForestFlowers)
            .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addForestGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.8F)
            .apply(PlantopiaForestBiomes::oakForestColors)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
    );

    ResourceKey<Biome> BLOOMING_GLADE = declareBiome(
        compileNameFrom("blooming", GLADE),
        PlantopiaBiomeDeclaration.builder()
            .addSpawn(MobCategory.CREATURE, EntityType.DONKEY, 1, 1, 2)
            .addSpawn(MobCategory.CREATURE, EntityType.RABBIT, 2, 2, 6)
            .addSpawn(MobCategory.CREATURE, EntityType.SHEEP, 2, 2, 4)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addDefaultSoftDisks)
            .applyGeneration(BiomeDefaultFeatures::addDefaultFlowers)
            .applyGeneration(BiomeDefaultFeatures::addExtraEmeralds)
            .applyGeneration(BiomeDefaultFeatures::addInfestedStone)
            .applyGeneration(BiomeDefaultFeatures::addTaigaGrass)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addDefaultExtraVegetation)
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .grassColorOverride("#83BB6D")
            .foliageColorOverride("#6cab53")
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW))
    );

    /* HELPER METHODS *******************************************************/

    static void aspenGroveColors(PlantopiaBiomeDeclaration.Builder builder) {
        builder.grassColorOverride("#c8b348").foliageColorOverride("#c0a63e");
    }

    static void snowyAspenGroveColors(PlantopiaBiomeDeclaration.Builder builder) {
        builder.grassColorOverride("#c06459").foliageColorOverride("#b3795a");
    }

    static void oakForestColors(PlantopiaBiomeDeclaration.Builder builder) {
        builder.grassColorOverride("#92bd42").foliageColorOverride("#7cb312");
    }
}
