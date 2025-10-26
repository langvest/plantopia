package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.*;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock.preventCreativeDropFromPos;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

public class PlantopiaWideTriplePlantBlock extends BushBlock implements PlantopiaOffsettableBlock, PlantopiaBaseBlockPosGetter, PlantopiaNaturalBlock {
	public static final EnumProperty<PlantopiaTripleBlockHalf> HALF = PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF;
	public static final EnumProperty<PlantopiaQuarter> QUARTER = PlantopiaBlockStateProperties.QUARTER;

	public PlantopiaWideTriplePlantBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(HALF, PlantopiaTripleBlockHalf.LOWER).setValue(QUARTER, PlantopiaQuarter.SOUTH_WEST));
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

	protected boolean canPlaceInto(@NotNull BlockGetter level, BlockPos pos, @Nullable BlockPlaceContext context) {
		var state = level.getBlockState(pos);
		return context != null ? state.canBeReplaced(context) : state.canBeReplaced();
	}

	protected boolean canPlaceQuarterColumnManuallyAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos) {
		var skippedPos = context.getClickedPos();
		var level = context.getLevel();
		var posAbove1 = pos.above(1);
		var posAbove2 = pos.above(2);

		return (skippedPos == pos || canPlaceInto(level, pos, context))
			&& (skippedPos == posAbove1 || canPlaceInto(level, posAbove1, context))
			&& (skippedPos == posAbove2 || canPlaceInto(level, posAbove2, context));
	}

	protected boolean canPlaceQuarterColumnNaturallyAt(@NotNull BlockGetter level, @NotNull BlockPos pos) {
		var posAbove1 = pos.above(1);
		var posAbove2 = pos.above(2);

		return canPlaceInto(level, pos, null)
			&& canPlaceInto(level, posAbove1, null)
			&& canPlaceInto(level, posAbove2, null);
	}

	public boolean canPlaceManuallyAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos) {
		return canPlaceQuarterColumnManuallyAt(context, pos)
			&& canPlaceQuarterColumnManuallyAt(context, pos.north())
			&& canPlaceQuarterColumnManuallyAt(context, pos.north().east())
			&& canPlaceQuarterColumnManuallyAt(context, pos.east());
	}

	@Override
	public boolean canPlaceNaturallyAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		return canPlaceQuarterColumnNaturallyAt(level, pos)
			&& canPlaceQuarterColumnNaturallyAt(level, pos.north())
			&& canPlaceQuarterColumnNaturallyAt(level, pos.north().east())
			&& canPlaceQuarterColumnNaturallyAt(level, pos.east());
	}

	@Override
	public void placeNaturallyAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, int flags) {
		placeAt(level, pos, state, flags);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var pos = context.getClickedPos();
		var level = context.getLevel();

		if(pos.getY() > level.getMaxBuildHeight() - 3) return null;

		var quarter = getQuarterByPlacementDirection(context.getHorizontalDirection());
		var newState = defaultBlockState().setValue(QUARTER, quarter);
		var baseBlockPos = getBaseBlockPos(newState, pos);

		return canPlaceManuallyAt(context, baseBlockPos) ? newState : null;
	}

	public static void placeAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, int flags) {
		BlockPos posAbove1 = pos.above(1);
		BlockPos posAbove2 = pos.above(2);
		placeSliceAt(level, pos, state.setValue(HALF, PlantopiaTripleBlockHalf.LOWER), flags);
		placeSliceAt(level, posAbove1, state.setValue(HALF, PlantopiaTripleBlockHalf.CENTRAL), flags);
		placeSliceAt(level, posAbove2, state.setValue(HALF, PlantopiaTripleBlockHalf.UPPER), flags);
	}

	private static void placeSliceAt(@NotNull LevelAccessor level, @NotNull BlockPos southWestPos, @NotNull BlockState state, int flags) {
		placeSliceAt(level, southWestPos, state, flags, null);
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

	public boolean isValidEnvironment(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		return super.canSurvive(state, level, pos);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var half = state.getValue(HALF);

		if(half == PlantopiaTripleBlockHalf.LOWER) {
			var baseBlockPos = getBaseBlockPos(state, pos);
			var northBlockPos = baseBlockPos.north();
			var northEastBlockPos = baseBlockPos.north().east();
			var eastBlockPos = baseBlockPos.east();

			var baseBlockState = level.getBlockState(baseBlockPos);
			var northBlockState = level.getBlockState(northBlockPos);
			var northEastBlockState = level.getBlockState(northEastBlockPos);
			var eastBlockState = level.getBlockState(eastBlockPos);

			return isValidEnvironment(baseBlockState, level, baseBlockPos)
				&& isValidEnvironment(northBlockState, level, northBlockPos)
				&& isValidEnvironment(northEastBlockState, level, northEastBlockPos)
				&& isValidEnvironment(eastBlockState, level, eastBlockPos);
		}

		var stateBelow = level.getBlockState(pos.below());

		return stateBelow.is(this) && stateBelow.getValue(HALF) != PlantopiaTripleBlockHalf.UPPER;
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
		BlockPos baseBlockPos = getBaseBlockPos(state, pos);
		BlockPos baseBlockPosAbove1 = baseBlockPos.above(1);
		BlockPos baseBlockPosAbove2 = baseBlockPos.above(2);
		placeSliceAt(level, baseBlockPos, state.setValue(HALF, PlantopiaTripleBlockHalf.LOWER), 3, pos);
		placeSliceAt(level, baseBlockPosAbove1, state.setValue(HALF, PlantopiaTripleBlockHalf.CENTRAL), 3, pos);
		placeSliceAt(level, baseBlockPosAbove2, state.setValue(HALF, PlantopiaTripleBlockHalf.UPPER), 3, pos);
	}

	/**
	 * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	 * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	 * returns its solidified counterpart.
	 * Note that this method should ideally consider only the specific facing passed in.
	 */
	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		var half = state.getValue(HALF);
		var quarter = state.getValue(QUARTER);
		var leftDirection = quarter.getLeftDirection().getOpposite();
		var rightDirection = quarter.getRightDirection().getOpposite();

		if(half != PlantopiaTripleBlockHalf.UPPER && facing == Direction.UP && (!neighborState.is(this) || neighborState.getValue(HALF) == half)) {
			return getFluidBlockState(level, pos);
		}

		if((facing == leftDirection || facing == rightDirection) && (!neighborState.is(this) || neighborState.getValue(QUARTER) == quarter)) {
			return getFluidBlockState(level, pos);
		}

		return super.updateShape(state, facing, neighborState, level, pos, neighborPos);
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

	protected void preventCreativeDropFromBottomParts(Level level, BlockPos pos, @NotNull BlockState state, Player player) {
		var baseBlockPos = getBaseBlockPos(state, pos);

		preventCreativeDropFromPos(level, baseBlockPos, state, player, pos);
		preventCreativeDropFromPos(level, baseBlockPos.north(), state, player, pos);
		preventCreativeDropFromPos(level, baseBlockPos.north().east(), state, player, pos);
		preventCreativeDropFromPos(level, baseBlockPos.east(), state, player, pos);
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
	public float getMaxHorizontalOffset() {
		return super.getMaxHorizontalOffset() * 2;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(HALF, QUARTER);
	}

	@Override
	public BlockPos getBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
		BlockPos lowerPos = switch(state.getValue(HALF)) {
			case UPPER -> pos.below(2);
			case CENTRAL -> pos.below(1);
			case LOWER -> pos;
		};

		return switch(state.getValue(QUARTER)) {
			case SOUTH_WEST -> lowerPos;
			case WEST_NORTH -> lowerPos.south();
			case NORTH_EAST -> lowerPos.south().west();
			case EAST_SOUTH -> lowerPos.west();
		};
	}

	@Override
	@SuppressWarnings("deprecation")
	public long getSeed(@NotNull BlockState state, @NotNull BlockPos pos) {
		return PlantopiaMathHelper.getSeed(getBaseBlockPos(state, pos));
	}

	public long getOffsetSeed(@NotNull BlockState state, @NotNull BlockPos pos) {
		return PlantopiaMathHelper.getSeed(getBaseBlockPos(state, pos).atY(0));
	}

	@Override
	public Optional<OffsetFunction> getOffsetFunction(Optional<BlockBehaviour.OffsetFunction> defaultOffsetFunction) {
		OffsetFunction offsetFunction = (state, level, pos) -> {
			long seed = getOffsetSeed(state, pos);
			float maxHorizontalOffset = getMaxHorizontalOffset();
			return PlantopiaMathHelper.getXZOffset(seed, maxHorizontalOffset);
		};

		return Optional.of(offsetFunction);
	}
}