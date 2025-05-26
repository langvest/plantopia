package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaOffsettableBlock;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

	private boolean canPlaceQuarterColumnAt(@NotNull BlockPlaceContext context, @NotNull BlockPos pos, BlockPos ignoredPos) {
		Level level = context.getLevel();
		BlockPos posAbove1 = pos.above(1);
		BlockPos posAbove2 = pos.above(2);

		return (ignoredPos == pos || level.getBlockState(pos).canBeReplaced(context))
			&& (ignoredPos == posAbove1 || level.getBlockState(posAbove1).canBeReplaced(context))
			&& (ignoredPos == posAbove2 || level.getBlockState(posAbove2).canBeReplaced(context));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		if(pos.getY() > level.getMaxBuildHeight() - 3) return null;
		PlantopiaQuarter quarter = getQuarterByPlacementDirection(context.getHorizontalDirection());
		BlockState newState = defaultBlockState().setValue(QUARTER, quarter);
		BlockPos baseBlockPos = getBaseBlockPos(newState, pos);

		boolean canPlaceAllQuarterColumns = canPlaceQuarterColumnAt(context, baseBlockPos, pos)
			&& canPlaceQuarterColumnAt(context, baseBlockPos.north(), pos)
			&& canPlaceQuarterColumnAt(context, baseBlockPos.north().east(), pos)
			&& canPlaceQuarterColumnAt(context, baseBlockPos.east(), pos);

		return canPlaceAllQuarterColumns ? newState : null;
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		PlantopiaTripleBlockHalf half = state.getValue(HALF);
		if(half == PlantopiaTripleBlockHalf.LOWER) {
			BlockPos baseBlockPos = getBaseBlockPos(state, pos);
			return super.canSurvive(state, level, baseBlockPos)
				&& super.canSurvive(state, level, baseBlockPos.north())
				&& super.canSurvive(state, level, baseBlockPos.north().east())
				&& super.canSurvive(state, level, baseBlockPos.east());
		}
		BlockState stateBelow = level.getBlockState(pos.below());
		return stateBelow.is(this) && stateBelow.getValue(HALF) != PlantopiaTripleBlockHalf.UPPER;
	}

	public static void placeAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, int flags) {
		BlockPos posAbove1 = pos.above(1);
		BlockPos posAbove2 = pos.above(2);
		placeSliceAt(level, pos, state.setValue(HALF, PlantopiaTripleBlockHalf.LOWER), flags);
		placeSliceAt(level, posAbove1, state.setValue(HALF, PlantopiaTripleBlockHalf.CENTRAL), flags);
		placeSliceAt(level, posAbove2, state.setValue(HALF, PlantopiaTripleBlockHalf.UPPER), flags);
	}

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

	private static void placeSliceAt(@NotNull LevelAccessor level, @NotNull BlockPos southWestPos, @NotNull BlockState state, int flags, BlockPos ignoredPos) {
		BlockPos westNorthPos = southWestPos.north();
		BlockPos northEastPos = westNorthPos.east();
		BlockPos eastSouthPos = northEastPos.south();
		if(ignoredPos != southWestPos) level.setBlock(southWestPos, copyWaterloggedFrom(level, southWestPos, state.setValue(QUARTER, PlantopiaQuarter.SOUTH_WEST)), flags);
		if(ignoredPos != westNorthPos) level.setBlock(westNorthPos, copyWaterloggedFrom(level, westNorthPos, state.setValue(QUARTER, PlantopiaQuarter.WEST_NORTH)), flags);
		if(ignoredPos != northEastPos) level.setBlock(northEastPos, copyWaterloggedFrom(level, northEastPos, state.setValue(QUARTER, PlantopiaQuarter.NORTH_EAST)), flags);
		if(ignoredPos != eastSouthPos) level.setBlock(eastSouthPos, copyWaterloggedFrom(level, eastSouthPos, state.setValue(QUARTER, PlantopiaQuarter.EAST_SOUTH)), flags);
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		PlantopiaTripleBlockHalf half = state.getValue(HALF);
		PlantopiaQuarter quarter = state.getValue(QUARTER);
		Direction leftDirection = quarter.getLeftDirection().getOpposite();
		Direction rightDirection = quarter.getRightDirection().getOpposite();
		if(half != PlantopiaTripleBlockHalf.UPPER && direction == Direction.UP && (!neighborState.is(this) || neighborState.getValue(HALF) == half)) return Blocks.AIR.defaultBlockState();
		if((direction == leftDirection || direction == rightDirection) && (!neighborState.is(this) || neighborState.getValue(QUARTER) == quarter)) return Blocks.AIR.defaultBlockState();
		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	// @Override
	// public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
	// 	if(!level.isClientSide()) {
	// 		if(player.isCreative()) {
	// 			preventCreativeDropFromBasePart(level, pos, state, player);
	// 		} else {
	// 			dropResources(state, level, pos, null, player, player.getMainHandItem());
	// 		}
	// 	}
	// 	super.playerWillDestroy(level, pos, state, player);
	// }

	protected static void preventCreativeDropFromBasePart(Level level, BlockPos pos, @NotNull BlockState state, Player player) {
		PlantopiaTripleBlockHalf half = state.getValue(HALF);

		if(half == PlantopiaTripleBlockHalf.LOWER) return;
		BlockPos baseBlockPos = getBaseBlockPos(state, pos);
		BlockState baseBlockState = level.getBlockState(baseBlockPos);
		if(!baseBlockState.is(state.getBlock())) return;
		if(baseBlockState.getValue(HALF) != PlantopiaTripleBlockHalf.LOWER) return;
		if(baseBlockState.getValue(QUARTER) != PlantopiaQuarter.SOUTH_WEST) return;
		level.setBlock(baseBlockPos, getFluidBlockState(level, pos), 35);
		level.setBlock(baseBlockPos.north(), getFluidBlockState(level, pos), 35);
		level.setBlock(baseBlockPos, getFluidBlockState(level, pos), 35);
		level.setBlock(baseBlockPos, getFluidBlockState(level, pos), 35);
		level.levelEvent(player, 2001, baseBlockPos, Block.getId(baseBlockState));
	}

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
			double d0 = Mth.clamp(((double)((float)(seed & 15L) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
			double d1 = Mth.clamp(((double)((float)(seed >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D, -maxHorizontalOffset, maxHorizontalOffset);
			return new Vec3(d0, 0.0D, d1);
		};

		return Optional.of(offsetFunction);
	}
}