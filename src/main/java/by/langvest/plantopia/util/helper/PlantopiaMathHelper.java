package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaMathHelper {
	public static long getSeed(int x, int y, int z) {
		return Mth.getSeed(x, y, z);
	}

	public static long getSeed(@NotNull Vec3i pos) {
		return getSeed(pos.getX(), pos.getY(), pos.getZ());
	}

	@Contract("_, _ -> new")
	public static @NotNull Vec3 getXZOffset(long seed, float maxHorizontalOffset) {
		double dx = Mth.clamp(((double)((float)(seed & 15L) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
		double dz = Mth.clamp(((double)((float)(seed >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
		return new Vec3(dx, 0.0D, dz);
	}

	public static @NotNull Vec3i getRandomXYZOffsetInArea(@NotNull RandomSource random, int radius) {
		return getRandomXYZOffsetInArea(random, radius, radius);
	}

	public static @NotNull Vec3i getRandomXYZOffsetInArea(@NotNull RandomSource random, int horizontalRadius, int verticalRadius) {
		var dx = random.nextInt((horizontalRadius * 2) + 1) - horizontalRadius;
		var dy = random.nextInt((verticalRadius * 2) + 1) - verticalRadius;
		var dz = random.nextInt((horizontalRadius * 2) + 1) - horizontalRadius;
		return new Vec3i(dx, dy, dz);
	}

	public static @NotNull Vec3i getRandomXYZOffsetAlongFaces(@NotNull RandomSource random) {
		return switch(random.nextInt(7)) {
			case 1 -> BlockPos.ZERO.above();
			case 2 -> BlockPos.ZERO.below();
			case 3 -> BlockPos.ZERO.south();
			case 4 -> BlockPos.ZERO.west();
			case 5 -> BlockPos.ZERO.north();
			case 6 -> BlockPos.ZERO.east();
			default -> BlockPos.ZERO;
		};
	}
}
