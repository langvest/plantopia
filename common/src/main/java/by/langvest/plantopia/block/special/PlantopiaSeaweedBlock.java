package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class PlantopiaSeaweedBlock extends Block implements LiquidBlockContainer, BonemealableBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PlantopiaSeaweedBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var clickedFace = context.getClickedFace();

        var facing = clickedFace;

        if (clickedFace.getAxis().isVertical()) {
            facing = context.getHorizontalDirection().getOpposite();
        } else {
            var adjacentPos = clickedPos.relative(clickedFace.getOpposite());
            var adjacentState = level.getBlockState(adjacentPos);

            if (adjacentState.is(this)) {
                facing = adjacentState.getValue(FACING);
            }
        }

        var newState = defaultBlockState().setValue(FACING, facing);

        if (!newState.canSurvive(level, clickedPos)) {
            return null;
        }

        return newState;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    protected boolean canAttachTo(BlockState state) {
        return !state.is(Blocks.MAGMA_BLOCK);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var fluidState = level.getFluidState(pos);

        if (!fluidState.isSourceOfType(Fluids.WATER)) {
            return false;
        }

        var facing = state.getValue(FACING);
        var adjacentPos = pos.relative(facing.getOpposite());
        var adjacentState = level.getBlockState(adjacentPos);

        if (adjacentState.is(this) && adjacentState.getValue(FACING) == facing) {
            return true;
        }

        return canAttachTo(adjacentState) && adjacentState.isFaceSturdy(level, pos, facing, SupportType.FULL);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        var newState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        var facing = state.getValue(FACING);

        if (direction == facing.getOpposite() && !state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        if (!newState.isAir()) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return newState;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        return adjacentState.is(this) || super.skipRendering(state, adjacentState, direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        var optionalPos = getLastConnectedBlockPos(level, pos, state);
        var direction = state.getValue(FACING);
        return optionalPos.isPresent() && canGrowInto(level.getBlockState(optionalPos.get().relative(direction)));
    }

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        var optionalPos = getLastConnectedBlockPos(level, pos, state);
        var direction = state.getValue(FACING);

        if (optionalPos.isPresent()) {
            var targetPos = optionalPos.get().relative(direction);
            var targetState = level.getBlockState(targetPos);

            if (canGrowInto(targetState)) {
                level.setBlockAndUpdate(targetPos, state);
            }
        }
    }

    protected boolean canGrowInto(BlockState state) {
        return state.is(Blocks.WATER);
    }

    protected boolean isSameState(BlockState state1, BlockState state2) {
        if (!state1.is(this) || !state2.is(this)) {
            return false;
        }

        return state1.getValue(FACING) == state2.getValue(FACING);
    }

    @SuppressWarnings("deprecation")
    protected Optional<BlockPos> getLastConnectedBlockPos(LevelReader level, BlockPos pos, BlockState state) {
        var candidatePos = pos.mutable();
        var direction = state.getValue(FACING);

        while (true) {
            candidatePos.move(direction);

            if (!level.hasChunkAt(candidatePos)) {
                return Optional.empty();
            }

            if (!isSameState(level.getBlockState(candidatePos), state)) {
                return Optional.of(candidatePos.move(direction.getOpposite()).immutable());
            }
        }
    }
}
