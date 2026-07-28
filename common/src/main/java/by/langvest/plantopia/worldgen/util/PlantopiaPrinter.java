package by.langvest.plantopia.worldgen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedListInt;

@ParametersAreNonnullByDefault
public class PlantopiaPrinter {
    protected static final IntProvider DEFAULT_HANGING_LEAVES_DEPTH = weightedListInt(values -> values
        .add(ConstantInt.of(2), 2)
        .add(ConstantInt.of(1), 1)
    );

    public static boolean printRow(BlockPos center, RandomSource random, Layer layer) {
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
        Layer provide(int row);
    }

    @FunctionalInterface
    public interface LayerFilter {
        boolean test(BlockPos pos, RandomSource random, int realDx, int realDz, int tempDx, int tempDz, int range);
    }

    @FunctionalInterface
    public interface LayerPlacer {
        boolean place(BlockPos pos, RandomSource random, int realDx, int realDz, int tempDx, int tempDz, int range);
    }

    @FunctionalInterface
    public interface LayerModifier {
        @Nullable BlockState apply(LayerContext context, BlockState state, BlockPos pos, RandomSource random, int realDx, int realDz, int tempDx, int tempDz, int range);
    }

    public record Layer(
        int range,
        boolean doubleAxis,
        LayerFilter filter,
        LayerPlacer placer
    ) {}

    public abstract static class LayerContext {
        public abstract BlockState getState(BlockPos pos, RandomSource random);

        public abstract boolean setBlock(BlockPos pos, BlockState state);
    }

    @Contract(pure = true)
    public static @NotNull LayerFilter declineFilter() {
        return (pos, random, realDx, realDz, tempDx, tempDz, range) -> false;
    }

    @Contract(pure = true)
    public static @NotNull LayerFilter templateFilter(PlantopiaTemplate template) {
        return (pos, random, realDx, realDz, tempDx, tempDz, range) ->
            template.test(random, tempDx, tempDz, range);
    }

    @Contract(pure = true)
    public static @NotNull LayerPlacer declinePlacer() {
        return (pos, random, realDx, realDz, tempDx, tempDz, range) -> false;
    }

    @Contract(pure = true)
    public static @NotNull LayerPlacer pipelinePlacer(LayerContext context, List<LayerModifier> pipeline) {
        return (pos, random, realDx, realDz, tempDx, tempDz, range) -> {
            var state = context.getState(pos, random);

            for (var modifier : pipeline) {
                state = modifier.apply(context, state, pos, random, realDx, realDz, tempDx, tempDz, range);
                if (state == null) return false;
            }

            if (state == null) return false;
            return context.setBlock(pos, state);
        };
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier revealMushroomInsides() {
        return (context, state, pos, random, realDx, realDz, tempDx, tempDz, range) -> {
            if (state.getBlock() instanceof HugeMushroomBlock) {
                if (tempDx > 0) state = state.setValue(BlockStateProperties.WEST, false);
                if (tempDx < 0) state = state.setValue(BlockStateProperties.EAST, false);
                if (tempDz > 0) state = state.setValue(BlockStateProperties.NORTH, false);
                if (tempDz < 0) state = state.setValue(BlockStateProperties.SOUTH, false);
            }
            return state;
        };
    }

    @FunctionalInterface
    public interface HangingLeafProvider {
        @Nullable BlockState provide(LayerContext context, BlockState state, BlockPos pos, RandomSource random, int currentDepth, int totalDepth);
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier placeHangingLeaves(float chance, IntProvider depth, HangingLeafProvider hangingLeafProvider) {
        return (context, state, pos, random, realDx, realDz, tempDx, tempDz, range) -> {
            if (random.nextFloat() < chance) {
                var mutablePos = pos.mutable();
                int totalDepth = depth.sample(random);

                for (int currentDepth = 1; currentDepth <= totalDepth; currentDepth++) {
                    mutablePos.move(Direction.DOWN);
                    var hangingState = hangingLeafProvider.provide(context, state, mutablePos, random, currentDepth, totalDepth);
                    if (hangingState != null) {
                        context.setBlock(mutablePos, hangingState);
                    }
                }
            }
            return state;
        };
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier placeHangingLeaves(float chance) {
        return placeHangingLeaves(chance, DEFAULT_HANGING_LEAVES_DEPTH, (context, state, pos, random, currentDepth, totalDepth) -> context.getState(pos, random));
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier filteredByTemplate(PlantopiaTemplate template, LayerModifier modifier) {
        return (context, state, pos, random, realDx, realDz, tempDx, tempDz, range) -> {
            if (template.test(random, tempDx, tempDz, range)) {
                return modifier.apply(context, state, pos, random, realDx, realDz, tempDx, tempDz, range);
            }
            return state;
        };
    }
}
