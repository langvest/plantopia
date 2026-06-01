package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.*;

@ParametersAreNonnullByDefault
public class PlantopiaWaterloggedHangingMossBlock extends PlantopiaHangingMossBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PlantopiaWaterloggedHangingMossBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TIP, true).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }

        return super.getFluidState(state);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var newState = super.getStateForPlacement(context);
        if (newState == null) return null;
        return copyWaterloggedFrom(context.getLevel(), context.getClickedPos(), newState);
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return super.canGrowInto(state) || isWaterSourceBlock(state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        scheduleWaterTick(state, level, pos);
        return super.updateShape(state, direction, facingState, level, pos, facingPos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }
}
