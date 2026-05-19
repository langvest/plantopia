package by.langvest.plantopia.worldgen.region.special;

import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class PlantopiaOverworldRegion extends PlantopiaRegion {
    public PlantopiaOverworldRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        addModifiedVanillaOverworldBiomes(mapper, modifiedVanillaOverworldBuilder -> {
            modifiedVanillaOverworldBuilder.replaceBiome(Biomes.SWAMP, PlantopiaOverworldBiomes.MARSH);
            modifiedVanillaOverworldBuilder.replaceBiome(Biomes.MANGROVE_SWAMP, PlantopiaOverworldBiomes.DEAD_MARSH);
            modifiedVanillaOverworldBuilder.replaceBiome(Biomes.DARK_FOREST, PlantopiaOverworldBiomes.SEASONAL_DARK_FOREST);
        });
    }
}
