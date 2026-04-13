package by.langvest.plantopia.worldgen.feature.special;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaNaturalBlockColumnFeature extends Feature<BlockColumnConfiguration> {
    public PlantopiaNaturalBlockColumnFeature(Codec<BlockColumnConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<BlockColumnConfiguration> context) {
        var config = context.config();

        return place(
            context.level(),
            context.origin(),
            context.random(),
            config.direction(),
            config.allowedPlacement(),
            config.prioritizeTip(),
            Block.UPDATE_CLIENTS,
            config.layers()
        );
    }

    public static boolean place(
        WorldGenLevel level,
        BlockPos pos,
        RandomSource random,
        Direction direction,
        BlockPredicate allowedPlacement,
        boolean prioritizeTip,
        int flags,
        @NotNull List<BlockColumnConfiguration.Layer> layers
    ) {
        return place(
            level,
            pos,
            random,
            direction,
            allowedPlacement,
            prioritizeTip,
            flags,
            layers.size(),
            simpleHeightProvider(layers),
            simpleStateProvider(layers)
        );
    }

    public static boolean place(
        WorldGenLevel level,
        BlockPos pos,
        RandomSource random,
        Direction direction,
        BlockPredicate allowedPlacement,
        boolean prioritizeTip,
        int flags,
        int layerAmount,
        LayerHeightProvider heightProvider,
        LayerStateProvider stateProvider
    ) {
        return place(
            level,
            pos,
            random,
            direction,
            allowedPlacement,
            prioritizeTip,
            flags,
            layerAmount,
            heightProvider,
            stateProvider,
            simpleBlockPlacer()
        );
    }

    @Contract(pure = true)
    public static @NotNull LayerHeightProvider simpleHeightProvider(@NotNull List<BlockColumnConfiguration.Layer> layers) {
        return (level, pos, random, layerIndex, totalHeight) -> layers.get(layerIndex).height().sample(random);
    }

    @Contract(pure = true)
    public static @NotNull LayerStateProvider simpleStateProvider(@NotNull List<BlockColumnConfiguration.Layer> layers) {
        return (level, pos, random, layerIndex, blockIndex, layerHeight, totalHeight) -> layers.get(layerIndex).state().getState(random, pos);
    }

    @Contract(pure = true)
    public static @NotNull LayerBlockPlacer simpleBlockPlacer() {
        return (level, pos, state, flags, random, layerIndex, blockIndex, layerHeight, totalHeight) -> level.setBlock(pos, copyWaterloggedFrom(level, pos, state), flags);
    }

    public static boolean place(
        WorldGenLevel level,
        BlockPos pos,
        RandomSource random,
        Direction direction,
        BlockPredicate allowedPlacement,
        boolean prioritizeTip,
        int flags,
        int layerAmount,
        LayerHeightProvider heightProvider,
        LayerStateProvider stateProvider,
        LayerBlockPlacer blockPlacer
    ) {
        if (layerAmount == 0) return false;

        var testState = stateProvider.getState(level, pos, random);
        if (!testState.canSurvive(level, pos)) return false;

        int[] layerHeights = new int[layerAmount];
        int totalHeight = 0;

        for (int i = 0; i < layerAmount; i++) {
            int layerHeight = heightProvider.getHeight(level, pos, random, i, totalHeight);
            layerHeights[i] = layerHeight;
            totalHeight += layerHeight;
        }

        if (totalHeight == 0) return false;

        var checkPos = pos.mutable();

        for (int i = 0; i < totalHeight; i++) {
            if (!isFavorablePos(testState, level, checkPos, direction, allowedPlacement)) {
                truncate(layerHeights, totalHeight, i, prioritizeTip);
                break;
            }
            checkPos.move(direction);
        }

        var mutablePos = pos.mutable();

        for (int i = 0; i < layerAmount; i++) {
            int layerHeight = layerHeights[i];

            if (layerHeight != 0) {
                for (int j = 0; j < layerHeight; j++) {
                    var stateToPlace = stateProvider.getState(level, mutablePos, random, i, j, layerHeight, totalHeight);
                    blockPlacer.placeBlock(level, mutablePos, stateToPlace, flags, random, i, j, layerHeight, totalHeight);
                    mutablePos.move(direction);
                }
            }
        }

        return true;
    }

    public static boolean isFavorablePos(BlockState state, @NotNull WorldGenLevel level, BlockPos pos, Direction direction) {
        return isFavorablePos(state, level, pos, direction, null);
    }

    public static boolean isFavorablePos(BlockState state, @NotNull WorldGenLevel level, BlockPos pos, Direction direction, @Nullable BlockPredicate allowedPlacement) {
        if (allowedPlacement != null && !allowedPlacement.test(level, pos)) {
            return false;
        }

        return direction == Direction.DOWN || PlantopiaNaturalBlockFeature.isFavorablePos(state, level, pos);
    }

    private static void truncate(int[] layerHeights, int totalHeight, int currentHeight, boolean prioritizeTip) {
        int heightToTruncate = totalHeight - currentHeight;
        int iterationDirection = prioritizeTip ? 1 : -1;
        int startIndex = prioritizeTip ? 0 : layerHeights.length - 1;
        int endIndex = prioritizeTip ? layerHeights.length : -1;

        for (int layerIndex = startIndex; layerIndex != endIndex && heightToTruncate > 0; layerIndex += iterationDirection) {
            int currentLayerHeight = layerHeights[layerIndex];
            int truncatedAmount = Math.min(currentLayerHeight, heightToTruncate);
            heightToTruncate -= truncatedAmount;
            layerHeights[layerIndex] -= truncatedAmount;
        }
    }

    @FunctionalInterface
    public interface LayerBlockPlacer {
        void placeBlock(WorldGenLevel level, BlockPos pos, BlockState state, int flags, RandomSource random, int layerIndex, int blockIndex, int layerHeight, int totalHeight);
    }

    @FunctionalInterface
    public interface LayerStateProvider {
        BlockState getState(WorldGenLevel level, BlockPos pos, RandomSource random, int layerIndex, int blockIndex, int layerHeight, int totalHeight);

        default BlockState getState(WorldGenLevel level, BlockPos pos, RandomSource random) {
            return getState(level, pos, random, 0, 0, 0, 0);
        }
    }

    @FunctionalInterface
    public interface LayerHeightProvider {
        int getHeight(WorldGenLevel level, BlockPos pos, RandomSource random, int layerIndex, int totalHeight);
    }
}
