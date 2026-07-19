package by.langvest.plantopia.worldgen.util;

import by.langvest.plantopia.worldgen.util.verticalanchor.PlantopiaVerticalAnchor;
import com.mojang.serialization.Codec;

public record PlantopiaVerticalAnchorType<P extends PlantopiaVerticalAnchor>(Codec<P> codec) {}
