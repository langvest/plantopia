package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import com.mojang.serialization.Codec;

public record PlantopiaBlockPlacerType<P extends PlantopiaBlockPlacer>(Codec<P> codec) {}
