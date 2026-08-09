package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class PlantopiaFluidHelper {
    public static @NotNull BlockState getFluidBlockState(LevelAccessor level, BlockPos pos) {
        return level.getFluidState(pos).createLegacyBlock().getBlock().defaultBlockState();
    }

    public static BlockState getWaterloggedState(BlockState state) {
        if (state.isAir()) return Blocks.WATER.defaultBlockState();

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            return state.setValue(BlockStateProperties.WATERLOGGED, true);
        }

        return state;
    }

    public static BlockState copyWaterloggedFrom(LevelAccessor level, BlockPos pos, BlockState state) {
        var fluidState = level.getFluidState(pos);

        if (fluidState.isSourceOfType(Fluids.WATER)) {
            return getWaterloggedState(state);
        }

        return state;
    }



    public static BlockState copyWaterloggedFrom(LevelSimulatedReader level, BlockPos pos, BlockState state) {
        if (level.isFluidAtPosition(pos, fluidState -> fluidState.isSourceOfType(Fluids.WATER))) {
            return getWaterloggedState(state);
        }

        return state;
    }

    public static boolean isWaterSourceBlock(BlockState state) {
        return state.is(Blocks.WATER) && state.getFluidState().isSource();
    }

    public static boolean isWaterlogged(BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            return state.getValue(BlockStateProperties.WATERLOGGED);
        }

        return state.getFluidState().isSourceOfType(Fluids.WATER);
    }

    public static void scheduleWaterTickIfNeeded(BlockState state, LevelAccessor level, BlockPos pos) {
        if (isWaterlogged(state)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
    }
}
