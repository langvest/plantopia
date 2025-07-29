package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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

import java.util.Optional;

public class PlantopiaSeaweedBlock extends Block implements LiquidBlockContainer, BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 4.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

	public PlantopiaSeaweedBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var level = context.getLevel();
		var clickedPos = context.getClickedPos();
		var clickedFace = context.getClickedFace();

		var facing = clickedFace;

		if(clickedFace.getAxis().isVertical()) {
			facing = context.getHorizontalDirection().getOpposite();
		}

		if(clickedFace.getAxis().isHorizontal()) {
			var adjacentPos = clickedPos.relative(clickedFace.getOpposite());
			var adjacentState = level.getBlockState(adjacentPos);

			if(adjacentState.is(this)) {
				facing = adjacentState.getValue(FACING);
			}
		}

		var newState = defaultBlockState().setValue(FACING, facing);

		if(!newState.canSurvive(level, clickedPos)) {
			return null;
		}

		return newState;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}

	protected boolean canAttachTo(@NotNull BlockState state) {
		return !state.is(Blocks.MAGMA_BLOCK);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var fluidState = level.getFluidState(pos);

		if(!fluidState.isSourceOfType(Fluids.WATER)) {
			return false;
		}

		var facing = state.getValue(FACING);
		var adjacentPos = pos.relative(facing.getOpposite());
		var adjacentState = level.getBlockState(adjacentPos);

		if(adjacentState.is(this) && adjacentState.getValue(FACING) == facing) {
			return true;
		}

		return canAttachTo(adjacentState) && adjacentState.isFaceSturdy(level, pos, facing, SupportType.FULL);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		var newState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
		var facing = state.getValue(FACING);

		if(direction == facing.getOpposite() && !state.canSurvive(level, pos)) {
			level.scheduleTick(pos, this, 1);
		}

		if(!newState.isAir()) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return newState;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentState, @NotNull Direction direction) {
		return adjacentState.is(this) || super.skipRendering(state, adjacentState, direction);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return Fluids.WATER.getSource(false);
	}

	@Override
	public boolean canPlaceLiquid(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid) {
		return false;
	}

	@Override
	public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState) {
		return false;
	}

	@Override
	public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		var optionalPos = getLastConnectedBlockPos(level, pos, state);
		var direction = state.getValue(FACING);
		return optionalPos.isPresent() && canGrowInto(level.getBlockState(optionalPos.get().relative(direction)));
	}

	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return true;
	}

	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		var optionalPos = getLastConnectedBlockPos(level, pos, state);
		var direction = state.getValue(FACING);

		if(optionalPos.isPresent()) {
			var targetPos = optionalPos.get().relative(direction);
			var targetState = level.getBlockState(targetPos);

			if(canGrowInto(targetState)) {
				level.setBlockAndUpdate(targetPos, state);
			}
		}
	}

	protected boolean canGrowInto(@NotNull BlockState state) {
		return state.is(Blocks.WATER);
	}

	protected boolean isSameState(@NotNull BlockState state1, @NotNull BlockState state2) {
		if(!state1.is(this) || !state2.is(this)) {
			return false;
		}

		return state1.getValue(FACING) == state2.getValue(FACING);
	}

	protected Optional<BlockPos> getLastConnectedBlockPos(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
		var candidatePos = pos.mutable();
		var direction = state.getValue(FACING);

		while(true) {
			candidatePos.move(direction);

			if(!level.hasChunkAt(candidatePos)) {
				return Optional.empty();
			}

			if(!isSameState(level.getBlockState(candidatePos), state)) {
				return Optional.of(candidatePos.move(direction.getOpposite()).immutable());
			}
		}
	}
}
