package by.langvest.plantopia.util;

import net.minecraft.util.RandomSource;

public interface PlantopiaRandomizedHitResult {
	void plantopia$setBaseSeed(byte baseSeed);

	byte plantopia$getBaseSeed();

	long plantopia$getRandomSeed();

	RandomSource plantopia$getRandom();
}
