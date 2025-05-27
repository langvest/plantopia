package by.langvest.plantopia.util.helper;

import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaMathHelper {
	public static long getSeed(int x, int y, int z) {
		return Mth.getSeed(x, y, z);
	}

	public static long getSeed(@NotNull Vec3i pos) {
		return getSeed(pos.getX(), pos.getY(), pos.getZ());
	}
}
