package by.langvest.plantopia.mixin.level.phys;

import by.langvest.plantopia.extension.PlantopiaRandomizedHitResultExtension;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(HitResult.class)
public abstract class PlantopiaHitResultMixin implements PlantopiaRandomizedHitResultExtension {
	@Unique
	private static final long plantopia$FNV_OFFSET_BASIS = 0xcbf29ce484222325L;
	@Unique
	private static final long plantopia$FNV_PRIME = 0x100000001b3L;
	@Unique
	private static final long plantopia$GOLDEN_MIX_CONSTANT = 0x9e3779b97f4a7c15L;
	@Unique
	private byte plantopia$baseSeed = plantopia$generateRandomBaseSeed();

	@Override
	public void plantopia$setBaseSeed(byte baseSeed) {
		this.plantopia$baseSeed = baseSeed;
	}

	@Override
	public byte plantopia$getBaseSeed() {
		return this.plantopia$baseSeed;
	}

	@Override
	public long plantopia$getRandomSeed() {
		HitResult hitResult = (HitResult)(Object)this;
		var location = hitResult.getLocation();

		long xBits = Double.doubleToLongBits(location.x);
		long yBits = Double.doubleToLongBits(location.y);
		long zBits = Double.doubleToLongBits(location.z);

		long seed = plantopia$FNV_OFFSET_BASIS;

		seed ^= xBits;
		seed *= plantopia$FNV_PRIME;
		seed ^= yBits;
		seed *= plantopia$FNV_PRIME;
		seed ^= zBits;
		seed *= plantopia$FNV_PRIME;

		seed ^= ((long)plantopia$baseSeed) * plantopia$GOLDEN_MIX_CONSTANT;

		return seed;
	}

	@Override
	public RandomSource plantopia$getRandom() {
		var seed = this.plantopia$getRandomSeed();
		return RandomSource.create(seed);
	}

	@Unique
	private static byte plantopia$generateRandomBaseSeed() {
		return (byte)ThreadLocalRandom.current().nextInt(256);
	}
}
