package by.langvest.plantopia.worldgen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record PlantopiaLayer(
    int range,
    boolean doubleAxis,
    LayerFilter filter,
    LayerPlacer placer
) {
    public static boolean printRow(BlockPos center, RandomSource random, PlantopiaLayer layer) {
        int range = layer.range();
        if (range < 0) return false;

        boolean doubleAxis = layer.doubleAxis();
        var filter = layer.filter();
        var placer = layer.placer();
        int bonusRange = doubleAxis ? 1 : 0;
        var mutablePos = new BlockPos.MutableBlockPos();
        boolean successfullyPlaced = false;

        for (int dx = -range; dx <= range + bonusRange; dx++) {
            for (int dz = -range; dz <= range + bonusRange; dz++) {
                int templateDx = dx;
                int templateDz = dz;

                if (doubleAxis) {
                    if (dx >= 1) templateDx = dx - 1;
                    if (dz >= 1) templateDz = dz - 1;
                }

                mutablePos.setWithOffset(center, dx, 0, dz);

                if (filter.test(mutablePos, random, dx, dz, templateDx, templateDz, range)) {
                    if (placer.place(mutablePos, random, dx, dz, templateDx, templateDz, range)) {
                        successfullyPlaced = true;
                    }
                }
            }
        }

        return successfullyPlaced;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean printBox(BlockPos ceiling, RandomSource random, int rows, LayerProvider layerProvider) {
        if (rows <= 0) return false;

        var mutablePos = new BlockPos.MutableBlockPos();
        boolean successfullyPlaced = false;

        for (int dy = 0; dy < rows; dy++) {
            mutablePos.setWithOffset(ceiling, 0, -dy, 0);
            var layer = layerProvider.provide(dy);

            if (printRow(mutablePos, random, layer)) {
                successfullyPlaced = true;
            }
        }

        return successfullyPlaced;
    }

    @FunctionalInterface
    public interface LayerProvider {
        PlantopiaLayer provide(int row);
    }

    @FunctionalInterface
    public interface LayerFilter {
        boolean test(BlockPos pos, RandomSource random, int realDx, int realDz, int tempDx, int tempDz, int range);
    }

    @FunctionalInterface
    public interface LayerPlacer {
        boolean place(BlockPos pos, RandomSource random, int realDx, int realDz, int tempDx, int tempDz, int range);
    }
}
