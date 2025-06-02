package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaOffsettableBlock;
import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
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

public class PlantopiaWideTriplePlantBlock extends BushBlock implements PlantopiaOffsettableBlock {
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

	protected boolean canManuallyPlaceQuarterColumnAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos) {
		var skippedPos = context.getClickedPos();
		var level = context.getLevel();
		var posAbove1 = pos.above(1);
		var posAbove2 = pos.above(2);

		return (skippedPos == pos || level.getBlockState(pos).canBeReplaced(context))
			&& (skippedPos == posAbove1 || level.getBlockState(posAbove1).canBeReplaced(context))
			&& (skippedPos == posAbove2 || level.getBlockState(posAbove2).canBeReplaced(context));
	}

	protected boolean canNaturallyPlaceQuarterColumnAt(@NotNull BlockGetter level, @NotNull BlockPos pos) {
		var posAbove1 = pos.above(1);
		var posAbove2 = pos.above(2);

		return level.getBlockState(pos).canBeReplaced()
			&& level.getBlockState(posAbove1).canBeReplaced()
			&& level.getBlockState(posAbove2).canBeReplaced();
	}

	public boolean canManuallyPlaceAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos) {
		return canManuallyPlaceQuarterColumnAt(context, pos)
			&& canManuallyPlaceQuarterColumnAt(context, pos.north())
			&& canManuallyPlaceQuarterColumnAt(context, pos.north().east())
			&& canManuallyPlaceQuarterColumnAt(context, pos.east());
	}

	public boolean canNaturallyPlaceAt(@NotNull BlockGetter level, @NotNull BlockPos pos) {
		return canNaturallyPlaceQuarterColumnAt(level, pos)
			&& canNaturallyPlaceQuarterColumnAt(level, pos.north())
			&& canNaturallyPlaceQuarterColumnAt(level, pos.north().east())
			&& canNaturallyPlaceQuarterColumnAt(level, pos.east());
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

		return canManuallyPlaceAt(context, baseBlockPos) ? newState : null;
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

			return super.canSurvive(baseBlockState, level, baseBlockPos)
				&& super.canSurvive(northBlockState, level, northBlockPos)
				&& super.canSurvive(northEastBlockState, level, northEastBlockPos)
				&& super.canSurvive(eastBlockState, level, eastBlockPos);
		}

		var stateBelow = level.getBlockState(pos.below());

		return stateBelow.is(this) && stateBelow.getValue(HALF) != PlantopiaTripleBlockHalf.UPPER;
	}

	public static void placeAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, int flags) {
		BlockPos posAbove1 = pos.above(1);
		BlockPos posAbove2 = pos.above(2);
		placeSliceAt(level, pos, state.setValue(HALF, PlantopiaTripleBlockHalf.LOWER), flags);
		placeSliceAt(level, posAbove1, state.setValue(HALF, PlantopiaTripleBlockHalf.CENTRAL), flags);
		placeSliceAt(level, posAbove2, state.setValue(HALF, PlantopiaTripleBlockHalf.UPPER), flags);
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

	protected static void preventCreativeDropFromBottomParts(Level level, BlockPos pos, @NotNull BlockState state, Player player) {
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

	public static BlockPos getBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
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

	/**
	 * Return a random long to be passed to {@link net.minecraft.client.resources.model.BakedModel#getQuads}, used for
	 * random model rotations
	 */
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