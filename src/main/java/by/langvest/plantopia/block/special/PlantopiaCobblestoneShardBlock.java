package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaCobblestoneShardBlock extends Block implements SimpleWaterloggedBlock {
	public static final int MIN_SHARDS = 1;
	public static final int MAX_SHARDS = 4;
	public static final IntegerProperty AMOUNT = PlantopiaBlockStateProperties.SHARDS;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape ONE_AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
	protected static final VoxelShape TWO_AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
	protected static final VoxelShape THREE_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	protected static final VoxelShape FOUR_AABB = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 5.0D, 15.0D);

	public PlantopiaCobblestoneShardBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(AMOUNT, MIN_SHARDS).setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockPos posBelow = pos.below();
		BlockState state = level.getBlockState(pos);
		if(state.is(this)) {
			BlockState stateBelow = level.getBlockState(posBelow);
			if(!stateBelow.isFaceSturdy(level, posBelow, Direction.UP)) return null;
			return state.setValue(AMOUNT, Math.min(MAX_SHARDS, state.getValue(AMOUNT) + 1));
		}
		if(!Block.canSupportCenter(level, posBelow, Direction.UP)) return null;
		BlockState newState = super.getStateForPlacement(context);
		if(newState == null) return null;
		return copyWaterloggedFrom(level, pos, newState);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		BlockPos posBelow = pos.below();
		int amount = state.getValue(AMOUNT);
		if(amount == MIN_SHARDS) return Block.canSupportCenter(level, posBelow, Direction.UP);
		BlockState stateBelow = level.getBlockState(posBelow);
		return stateBelow.isFaceSturdy(level, posBelow, Direction.UP);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
		if(!context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(AMOUNT) < MAX_SHARDS) return true;
		return super.canBeReplaced(state, context);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		if(direction == Direction.DOWN && !canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
		if(state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		int amount = state.getValue(AMOUNT);
		if(amount == 1) return ONE_AABB;
		if(amount == 2) return TWO_AABB;
		if(amount == 3) return THREE_AABB;
		return FOUR_AABB;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(AMOUNT, WATERLOGGED);
	}
}