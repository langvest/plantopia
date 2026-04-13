package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import com.mojang.serialization.Codec;

public record PlantopiaVerticalAnchorType<P extends PlantopiaVerticalAnchor>(Codec<P> codec) {}
