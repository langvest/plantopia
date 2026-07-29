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
public class PlantopiaLayerUtils {
    protected static final IntProvider DEFAULT_HANGING_LEAVES_DEPTH = weightedListInt(values -> values
        .add(ConstantInt.of(2), 2)
        .add(ConstantInt.of(1), 1)
    );

    public static boolean never(BlockPos pos, RandomSource random, int realDx, int realDz, int tempDx, int tempDz, int range) {
        return false;
    }

    @FunctionalInterface
    public interface LayerBlockProvider {
        BlockState get(RandomSource random, BlockPos pos);
    }

    @FunctionalInterface
    public interface LayerBlockSetter {
        boolean set(BlockPos pos, BlockState state);
    }

    @FunctionalInterface
    public interface LayerModifier {
        @Nullable BlockState apply(BlockState state, BlockPos pos, RandomSource random, LayerBlockProvider provider, LayerBlockSetter setter, int realDx, int realDz, int tempDx, int tempDz, int range);
    }

    @Contract(pure = true)
    public static @NotNull PlantopiaLayer.LayerFilter templateFilter(PlantopiaTemplate template) {
        return (pos, random, realDx, realDz, tempDx, tempDz, range) ->
            template.test(random, tempDx, tempDz, range);
    }

    @Contract(pure = true)
    public static @NotNull PlantopiaLayer.LayerPlacer pipelinePlacer(LayerBlockProvider provider, LayerBlockSetter setter, List<LayerModifier> pipeline) {
        return (pos, random, realDx, realDz, tempDx, tempDz, range) -> {
            var state = provider.get(random, pos);

            for (var modifier : pipeline) {
                state = modifier.apply(state, pos, random, provider, setter, realDx, realDz, tempDx, tempDz, range);
                if (state == null) return false;
            }

            if (state == null) return false;
            return setter.set(pos, state);
        };
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier revealMushroomInsides() {
        return (state, pos, random, provider, setter, realDx, realDz, tempDx, tempDz, range) -> {
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
        @Nullable BlockState provide(BlockState state, BlockPos pos, RandomSource random, LayerBlockProvider provider, int currentDepth, int totalDepth);
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier placeHangingLeaves(float chance, IntProvider depth, HangingLeafProvider hangingLeafProvider) {
        return (state, pos, random, provider, setter, realDx, realDz, tempDx, tempDz, range) -> {
            if (random.nextFloat() < chance) {
                var mutablePos = pos.mutable();
                int totalDepth = depth.sample(random);

                for (int currentDepth = 1; currentDepth <= totalDepth; currentDepth++) {
                    mutablePos.move(Direction.DOWN);
                    var hangingState = hangingLeafProvider.provide(state, mutablePos, random, provider, currentDepth, totalDepth);
                    if (hangingState != null) {
                        setter.set(mutablePos, hangingState);
                    }
                }
            }
            return state;
        };
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier placeHangingLeaves(float chance) {
        return placeHangingLeaves(chance, DEFAULT_HANGING_LEAVES_DEPTH, ( state, pos, random, provider, currentDepth, totalDepth) -> provider.get(random, pos));
    }

    @Contract(pure = true)
    public static @NotNull LayerModifier filteredByTemplate(PlantopiaTemplate template, LayerModifier modifier) {
        return ( state, pos, random, provider, setter, realDx, realDz, tempDx, tempDz, range) -> {
            if (template.test(random, tempDx, tempDz, range)) {
                return modifier.apply(state, pos, random, provider, setter, realDx, realDz, tempDx, tempDz, range);
            }
            return state;
        };
    }
}
