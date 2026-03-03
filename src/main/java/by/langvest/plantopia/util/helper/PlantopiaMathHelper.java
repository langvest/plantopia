package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class PlantopiaMathHelper {
    public static long getSeed(int x, int y, int z) {
        return Mth.getSeed(x, y, z);
    }

    public static long getSeed(@NotNull Vec3i pos) {
        return getSeed(pos.getX(), pos.getY(), pos.getZ());
    }

    public static @NotNull Vec3 getSeededOffset(Vec3i pos, float maxOffset) {
        return getSeededOffset(getSeed(pos), maxOffset, maxOffset);
    }

    public static @NotNull Vec3 getSeededOffset(Vec3i pos, float maxHorizontalOffset, float maxVerticalOffset) {
        return getSeededOffset(getSeed(pos), maxHorizontalOffset, maxVerticalOffset);
    }

    public static @NotNull Vec3 getSeededOffset(long seed, float maxOffset) {
        return getSeededOffset(seed, maxOffset, maxOffset);
    }

    @Contract(value = "_, _, _ -> new")
    public static @NotNull Vec3 getSeededOffset(long seed, float maxHorizontalOffset, float maxVerticalOffset) {
        double dx = Mth.clamp(((double) ((float) (seed & 15L) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
        double dy = Mth.clamp(((double) ((float) (seed >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D, -maxVerticalOffset, maxVerticalOffset);
        double dz = Mth.clamp(((double) ((float) (seed >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
        return new Vec3(dx, dy, dz);
    }

    public static @NotNull Vec3i getRandomOffsetInArea(@NotNull RandomSource random, int radius) {
        return getRandomOffsetInArea(random, radius, radius);
    }

    public static @NotNull Vec3i getRandomOffsetInArea(@NotNull RandomSource random, int horizontalRadius, int verticalRadius) {
        var dx = random.nextInt((horizontalRadius * 2) + 1) - horizontalRadius;
        var dy = random.nextInt((verticalRadius * 2) + 1) - verticalRadius;
        var dz = random.nextInt((horizontalRadius * 2) + 1) - horizontalRadius;
        return new Vec3i(dx, dy, dz);
    }

    public static @NotNull Vec3i getHorizontalRadialOffset(@NotNull RandomSource random, int radius, double sigma, double erosion) {
        double angle = random.nextDouble() * 2 * Math.PI;
        double baseDistribution = 1.0 - Math.sqrt(random.nextDouble());
        double exponent = Math.pow(2, sigma);
        double adjustedDistanceFactor = Math.pow(baseDistribution, exponent);
        double distance = adjustedDistanceFactor * radius;

        if (erosion > 0) {
            double distortion = (random.nextDouble() * 2.0 - 1.0) * erosion * distance;
            distance += distortion;
        }

        distance = Mth.clamp(distance, 0, radius);

        int dx = (int) Math.round(Math.cos(angle) * distance);
        int dz = (int) Math.round(Math.sin(angle) * distance);

        return new Vec3i(dx, 0, dz);
    }

    public static @NotNull Vec3i getRandomOffsetAlongFaces(@NotNull RandomSource random) {
        return switch (random.nextInt(7)) {
            case 1 -> BlockPos.ZERO.above();
            case 2 -> BlockPos.ZERO.below();
            case 3 -> BlockPos.ZERO.south();
            case 4 -> BlockPos.ZERO.west();
            case 5 -> BlockPos.ZERO.north();
            case 6 -> BlockPos.ZERO.east();
            default -> BlockPos.ZERO;
        };
    }

    public static void shuffle(@NotNull List<?> list, RandomSource random) {
        int size = list.size();
        for (int i = size; i > 1; i--) {
            swap(list, i - 1, random.nextInt(i));
        }
    }

    private static <T> void swap(@NotNull List<T> list, int firstIndex, int secondIndex) {
        T tempItem = list.get(firstIndex);
        list.set(firstIndex, list.get(secondIndex));
        list.set(secondIndex, tempItem);
    }

    public static boolean isCloseNeighbours(@NotNull BlockPos pos1, @NotNull BlockPos pos2) {
        for (var direction : Direction.values()) {
            if (pos1.relative(direction).equals(pos2)) return true;
        }

        return false;
    }

    public static float getRandomFloatInclusive(RandomSource random, float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("Invalid range: min must be less than max.");
        }

        return min + random.nextFloat() * (max - min);
    }
}
