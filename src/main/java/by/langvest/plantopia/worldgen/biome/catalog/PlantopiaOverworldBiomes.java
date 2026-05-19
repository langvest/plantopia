package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.plantopia.worldgen.region.PlantopiaRegions;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
public class PlantopiaOverworldBiomes extends PlantopiaBiomes {
    public static final Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog();

    public static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build(name)).getKey();
    }

    public static final ResourceKey<Biome> MARSH = declareBiome(
        "marsh",
        PlantopiaBiomeDeclaration.builder()
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .addSpawn(MobCategory.CREATURE, EntityType.CHICKEN, 8, 2, 4)
            .addSpawn(MobCategory.MONSTER, EntityType.SLIME, 1, 1, 1)
            .addSpawn(MobCategory.CREATURE, EntityType.FROG, 6, 2, 4)
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

    public static final ResourceKey<Biome> DEAD_MARSH = declareBiome(
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

    public static final ResourceKey<Biome> SEASONAL_DARK_FOREST = declareBiome(
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

    /* HELPER METHODS *************************************************************************************************/

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder generationBuilder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generationBuilder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generationBuilder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generationBuilder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generationBuilder);
        BiomeDefaultFeatures.addDefaultSprings(generationBuilder);
        BiomeDefaultFeatures.addSurfaceFreezing(generationBuilder);
    }
}
