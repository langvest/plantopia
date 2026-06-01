package by.langvest.plantopia.extension;

import net.minecraft.util.RandomSource;

public interface PlantopiaRandomizedHitResultExtension {
    void plantopia$setBaseSeed(byte baseSeed);

    byte plantopia$getBaseSeed();

    long plantopia$getRandomSeed();

    RandomSource plantopia$getRandom();
}
