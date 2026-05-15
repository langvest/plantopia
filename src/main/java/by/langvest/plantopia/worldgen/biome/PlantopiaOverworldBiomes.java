package by.langvest.plantopia.worldgen.biome;

import by.langvest.plantopia.worldgen.region.PlantopiaRegions;
import com.google.common.collect.Maps;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @see net.minecraft.data.worldgen.biome.OverworldBiomes
 */
public class PlantopiaOverworldBiomes extends PlantopiaBiomes {
    private static final Map<ResourceKey<Biome>, PlantopiaBiomeDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<Biome>, PlantopiaBiomeDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<Biome> declareBiome(String name, PlantopiaBiomeDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<Biome> MARSH = declareBiome(
        "marsh",
        PlantopiaBiomeDeclaration.builder()
            .region(PlantopiaRegions.OVERWORLD_REGULAR)
            .applySpawn(BiomeDefaultFeatures::commonSpawns)
            .addSpawn(MobCategory.CREATURE, EntityType.CHICKEN, 10, 4, 4)
            .addSpawn(MobCategory.MONSTER, EntityType.SLIME, 1, 1, 1)
            .applyGeneration(BiomeDefaultFeatures::addFossilDecoration)
            .applyGeneration(PlantopiaOverworldBiomes::globalOverworldGeneration)
            .applyGeneration(BiomeDefaultFeatures::addDefaultOres)
            .applyGeneration(BiomeDefaultFeatures::addSwampClayDisk)
            .applyGeneration(BiomeDefaultFeatures::addSwampVegetation)
            .applyGeneration(BiomeDefaultFeatures::addDefaultMushrooms)
            .applyGeneration(BiomeDefaultFeatures::addSwampExtraVegetation)
            .addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SWAMP)
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.9F)
            .waterColor("#ff578f")
            .waterFogColor(2302743)
            .fogColor(12638463)
            .foliageColorOverride(6975545)
            .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.SWAMP)
            .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
            .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_SWAMP))
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
