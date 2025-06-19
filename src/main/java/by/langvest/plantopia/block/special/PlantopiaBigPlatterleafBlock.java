package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaQuarter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

public class PlantopiaBigPlatterleafBlock extends BushBlock {
	public static final EnumProperty<PlantopiaQuarter> QUARTER = PlantopiaBlockStateProperties.QUARTER;
	protected static final VoxelShape SOUTH_WEST_AABB = Block.box(2.0D, 0.0D, 0.0D, 16.0D, 2.0D, 14.0D);
	protected static final VoxelShape WEST_NORTH_AABB = Block.box(2.0D, 0.0D, 2.0D, 16.0D, 2.0D, 16.0D);
	protected static final VoxelShape NORTH_EAST_AABB = Block.box(0.0D, 0.0D, 2.0D, 14.0D, 2.0D, 16.0D);
	protected static final VoxelShape EAST_SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 14.0D, 2.0D, 14.0D);

	public PlantopiaBigPlatterleafBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(QUARTER, PlantopiaQuarter.SOUTH_WEST));
	}

	@Contract(pure = true)
	private PlantopiaQuarter getQuarterByPlacementDirection(@NotNull Direction direction) {
		return switch(direction) {
			case EAST -> PlantopiaQuarter.WEST_NORTH;
			case SOUTH -> PlantopiaQuarter.NORTH_EAST;
			case WEST -> PlantopiaQuarter.EAST_SOUTH;
			default -> PlantopiaQuarter.SOUTH_WEST;
		};
	}

	protected boolean canManuallyPlaceQuarterAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos) {
		var skippedPos = context.getClickedPos();
		var level = context.getLevel();

		return pos.equals(skippedPos) || level.getBlockState(pos).canBeReplaced(context);
	}

	protected boolean canNaturallyPlaceQuarterAt(@NotNull BlockGetter level, @NotNull BlockPos pos, BlockPos skippedPos) {
		return pos.equals(skippedPos) || level.getBlockState(pos).canBeReplaced();
	}

	public boolean canManuallyPlaceAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos) {
		return canManuallyPlaceQuarterAt(context, pos)
			&& canManuallyPlaceQuarterAt(context, pos.north())
			&& canManuallyPlaceQuarterAt(context, pos.north().east())
			&& canManuallyPlaceQuarterAt(context, pos.east());
	}

	public boolean canNaturallyPlaceAt(@NotNull BlockGetter level, @NotNull BlockPos pos, BlockPos skippedPos) {
		return canNaturallyPlaceQuarterAt(level, pos, skippedPos)
			&& canNaturallyPlaceQuarterAt(level, pos.north(), skippedPos)
			&& canNaturallyPlaceQuarterAt(level, pos.north().east(), skippedPos)
			&& canNaturallyPlaceQuarterAt(level, pos.east(), skippedPos);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var pos = context.getClickedPos();

		var quarter = getQuarterByPlacementDirection(context.getHorizontalDirection());
		var newState = defaultBlockState().setValue(QUARTER, quarter);
		var baseBlockPos = getBaseBlockPos(newState, pos);

		return canManuallyPlaceAt(context, baseBlockPos) ? newState : null;
	}

	public boolean placeAt(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, int flags, BlockPos skippedPos) {
		if(canNaturallyPlaceAt(level, pos, skippedPos)) {
			placeSliceAt(level, pos, state, flags, null);
			return true;
		}

		return false;
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
		BlockPos baseBlockPos = getBaseBlockPos(state, pos);
		placeSliceAt(level, baseBlockPos, state, 3, pos);
	}

	private static void placeSliceAt(@NotNull LevelAccessor level, @NotNull BlockPos southWestPos, @NotNull BlockState state, int flags, BlockPos skippedPos) {
		var westNorthPos = southWestPos.north();
		var northEastPos = westNorthPos.east();
		var eastSouthPos = northEastPos.south();

		if(skippedPos != southWestPos) level.setBlock(southWestPos, copyWaterloggedFrom(level, southWestPos, state.setValue(QUARTER, PlantopiaQuarter.SOUTH_WEST)), flags);
		if(skippedPos != westNorthPos) level.setBlock(westNorthPos, copyWaterloggedFrom(level, westNorthPos, state.setValue(QUARTER, PlantopiaQuarter.WEST_NORTH)), flags);
		if(skippedPos != northEastPos) level.setBlock(northEastPos, copyWaterloggedFrom(level, northEastPos, state.setValue(QUARTER, PlantopiaQuarter.NORTH_EAST)), flags);
		if(skippedPos != eastSouthPos) level.setBlock(eastSouthPos, copyWaterloggedFrom(level, eastSouthPos, state.setValue(QUARTER, PlantopiaQuarter.EAST_SOUTH)), flags);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific facing passed in.
	 */
	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		var quarter = state.getValue(QUARTER);
		var leftDirection = quarter.getLeftDirection().getOpposite();
		var rightDirection = quarter.getRightDirection().getOpposite();

		if((facing == leftDirection || facing == rightDirection) && (!neighborState.is(this) || neighborState.getValue(QUARTER) == quarter)) {
			return getFluidBlockState(level, pos);
		}

		return super.updateShape(state, facing, neighborState, level, pos, neighborPos);
	}

	/**
	 * Called after a player has successfully harvested this block. This method will only be called if the player has
	 * used the correct tool and drops should be spawned.
	 */
	@Override
	public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
		super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), blockEntity, tool);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(QUARTER);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return switch(state.getValue(QUARTER)) {
			case SOUTH_WEST -> SOUTH_WEST_AABB;
			case WEST_NORTH -> WEST_NORTH_AABB;
			case NORTH_EAST -> NORTH_EAST_AABB;
			case EAST_SOUTH -> EAST_SOUTH_AABB;
		};
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		var fluidState = level.getFluidState(pos);
		var fluidStateAbove = level.getFluidState(pos.above());

		return (fluidState.isSourceOfType(Fluids.WATER) || state.getBlock() instanceof IceBlock) && fluidStateAbove.isEmpty();
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
	 * this block
	 */
	@Override
	public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
		if(!level.isClientSide()) {
			if(player.isCreative()) {
				preventCreativeDropFromBottomParts(level, pos, state, player);
			} else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}

		super.playerWillDestroy(level, pos, state, player);
	}

	protected static void preventCreativeDropFromPos(Level level, BlockPos pos, @NotNull BlockState originalState, Player player, BlockPos skippedPos) {
		if(pos == skippedPos) return;

		var state = level.getBlockState(pos);

		if(!state.is(originalState.getBlock())) return;

		level.setBlock(pos, getFluidBlockState(level, pos), 35);
		level.levelEvent(player, 2001, pos, Block.getId(state));
	}

	protected static void preventCreativeDropFromBottomParts(Level level, BlockPos pos, @NotNull BlockState state, Player player) {
		var baseBlockPos = getBaseBlockPos(state, pos);

		preventCreativeDropFromPos(level, baseBlockPos, state, player, pos);
		preventCreativeDropFromPos(level, baseBlockPos.north(), state, player, pos);
		preventCreativeDropFromPos(level, baseBlockPos.north().east(), state, player, pos);
		preventCreativeDropFromPos(level, baseBlockPos.east(), state, player, pos);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var baseBlockPos = getBaseBlockPos(state, pos);
		var northBlockPos = baseBlockPos.north();
		var northEastBlockPos = baseBlockPos.north().east();
		var eastBlockPos = baseBlockPos.east();

		var baseBlockState = level.getBlockState(baseBlockPos);
		var northBlockState = level.getBlockState(northBlockPos);
		var northEastBlockState = level.getBlockState(northEastBlockPos);
		var eastBlockState = level.getBlockState(eastBlockPos);

		return super.canSurvive(baseBlockState, level, baseBlockPos)
			&& super.canSurvive(northBlockState, level, northBlockPos)
			&& super.canSurvive(northEastBlockState, level, northEastBlockPos)
			&& super.canSurvive(eastBlockState, level, eastBlockPos);
	}

	public static BlockPos getBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
		return switch(state.getValue(QUARTER)) {
			case SOUTH_WEST -> pos;
			case WEST_NORTH -> pos.south();
			case NORTH_EAST -> pos.south().west();
			case EAST_SOUTH -> pos.west();
		};
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.WATER;
	}
}
