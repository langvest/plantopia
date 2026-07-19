package by.langvest.plantopia.worldgen.util;

import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import com.mojang.serialization.Codec;

public record PlantopiaIntProportionType<P extends PlantopiaIntProportion>(Codec<P> codec) {}
