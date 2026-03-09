package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaFluidHelper {
    public static @NotNull BlockState getFluidBlockState(@NotNull LevelAccessor level, BlockPos pos) {
        return level.getFluidState(pos).createLegacyBlock().getBlock().defaultBlockState();
    }

    public static @NotNull BlockState copyWaterloggedFrom(@NotNull LevelAccessor level, BlockPos pos, @NotNull BlockState state) {
        var fluidState = level.getFluidState(pos);

        if (state.isAir() && fluidState.isSourceOfType(Fluids.WATER)) {
            return Blocks.WATER.defaultBlockState();
        }

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            return state.setValue(BlockStateProperties.WATERLOGGED, fluidState.isSourceOfType(Fluids.WATER));
        }

        return state;
    }

    public static boolean isWaterSourceBlock(@NotNull BlockState state) {
        return state.is(Blocks.WATER) && state.getFluidState().isSource();
    }

    public static boolean isWaterlogged(@NotNull BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            return state.getValue(BlockStateProperties.WATERLOGGED);
        }

        return state.getFluidState().isSourceOfType(Fluids.WATER);
    }

    public static void scheduleWaterTick(BlockState state, @NotNull LevelAccessor level, BlockPos pos) {
        if (isWaterlogged(state)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
    }
}