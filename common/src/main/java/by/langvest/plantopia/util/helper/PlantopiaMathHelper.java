package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public final class PlantopiaMathHelper {
    public static long getSeed(int x, int y, int z) {
        return Mth.getSeed(x, y, z);
    }

    public static long getSeed(Vec3i pos) {
        return getSeed(pos.getX(), pos.getY(), pos.getZ());
    }

    @Contract("_, _ -> new")
    public static @NotNull Vec3 getSeededOffset(Vec3i pos, float maxOffset) {
        return getSeededOffset(getSeed(pos), maxOffset, maxOffset);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Vec3 getSeededOffset(Vec3i pos, float maxHorizontalOffset, float maxVerticalOffset) {
        return getSeededOffset(getSeed(pos), maxHorizontalOffset, maxVerticalOffset);
    }

    @Contract("_, _ -> new")
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

    public static @NotNull Vec3i getRandomOffsetInArea(RandomSource random, int radius) {
        return getRandomOffsetInArea(random, radius, radius);
    }

    public static @NotNull Vec3i getRandomOffsetInArea(RandomSource random, int horizontalRadius, int verticalRadius) {
        var dx = random.nextInt((horizontalRadius * 2) + 1) - horizontalRadius;
        var dy = random.nextInt((verticalRadius * 2) + 1) - verticalRadius;
        var dz = random.nextInt((horizontalRadius * 2) + 1) - horizontalRadius;
        return new Vec3i(dx, dy, dz);
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull Vec3i getHorizontalRadialOffset(RandomSource random, int radius, double sigma, double erosion) {
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

    public static Vec3i getRandomOffsetAlongFaces(RandomSource random) {
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

    public static void shuffle(List<?> list, RandomSource random) {
        int size = list.size();
        for (int i = size; i > 1; i--) {
            swap(list, i - 1, random.nextInt(i));
        }
    }

    private static <T> void swap(List<T> list, int firstIndex, int secondIndex) {
        T tempItem = list.get(firstIndex);
        list.set(firstIndex, list.get(secondIndex));
        list.set(secondIndex, tempItem);
    }

    public static boolean isCloseNeighbours(BlockPos pos1, BlockPos pos2) {
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


    public static double @NotNull[] @NotNull[] boxBlurMatrix(double @NotNull[] @NotNull[] matrix, int blurRadius) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return new double[0][0];
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] blurredMatrix = new double[rows][cols];

        double[][] sat = new double[rows + 1][cols + 1];
        for (int row = 1; row <= rows; row++) {
            double rowSum = 0;
            for (int col = 1; col <= cols; col++) {
                rowSum += matrix[row - 1][col - 1];
                sat[row][col] = sat[row - 1][col] + rowSum;
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int y1 = Math.max(0, row - blurRadius);
                int y2 = Math.min(rows - 1, row + blurRadius);
                int x1 = Math.max(0, col - blurRadius);
                int x2 = Math.min(cols - 1, col + blurRadius);

                double sum = sat[y2 + 1][x2 + 1] - sat[y1][x2 + 1] - sat[y2 + 1][x1] + sat[y1][x1];
                int area = (y2 - y1 + 1) * (x2 - x1 + 1);

                blurredMatrix[row][col] = sum / area;
            }
        }

        return blurredMatrix;
    }

    public static boolean isWithinShape(BlockPos pos, int dx, int dz, int radius, float curvature, double frequency) {
        return isWithinShape(pos.getX(), pos.getZ(), dx, dz, radius, curvature, frequency);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isWithinShape(int x, int z, int dx, int dz, int radius, float curvature, double frequency) {
        double distanceSq = dx * dx + dz * dz;
        if (distanceSq > radius * radius) {
            return false;
        }

        if (curvature == 0) {
            return true;
        }

        double distanceFalloff = Math.sqrt(distanceSq) / radius;

        @SuppressWarnings("removal")
        double noiseValue = Biome.BIOME_INFO_NOISE.getValue((double) x * frequency, (double) z * frequency, false);
        double normalizedNoise = (noiseValue + 1.0) / 2.0;

        return normalizedNoise > distanceFalloff;
    }
}
