package by.langvest.plantopia.util.helper;

import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
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
}
