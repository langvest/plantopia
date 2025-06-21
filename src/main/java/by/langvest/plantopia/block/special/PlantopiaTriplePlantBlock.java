package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBaseBlockPosGetter;
import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

public class PlantopiaTriplePlantBlock extends BushBlock implements PlantopiaBaseBlockPosGetter {
	public static final EnumProperty<PlantopiaTripleBlockHalf> HALF = PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF;

	public PlantopiaTriplePlantBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(HALF, PlantopiaTripleBlockHalf.LOWER));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var pos = context.getClickedPos();
		var level = context.getLevel();

		if(pos.getY() > level.getMaxBuildHeight() - 3) return null;

		if(!level.getBlockState(pos.above(1)).canBeReplaced(context)) return null;
		if(!level.getBlockState(pos.above(2)).canBeReplaced(context)) return null;

		return super.getStateForPlacement(context);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var half = state.getValue(HALF);

		if(half == PlantopiaTripleBlockHalf.LOWER) {
			return super.canSurvive(state, level, pos);
		}

		var stateBelow = level.getBlockState(pos.below());

		return stateBelow.is(this) && stateBelow.getValue(HALF) != PlantopiaTripleBlockHalf.UPPER;
	}

	public static void placeAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, int flags) {
		var posAbove1 = pos.above(1);
		var posAbove2 = pos.above(2);

		level.setBlock(pos, copyWaterloggedFrom(level, pos, state.setValue(HALF, PlantopiaTripleBlockHalf.LOWER)), flags);
		level.setBlock(posAbove1, copyWaterloggedFrom(level, posAbove1, state.setValue(HALF, PlantopiaTripleBlockHalf.CENTRAL)), flags);
		level.setBlock(posAbove2, copyWaterloggedFrom(level, posAbove2, state.setValue(HALF, PlantopiaTripleBlockHalf.UPPER)), flags);
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
		var posAbove1 = pos.above(1);
		var posAbove2 = pos.above(2);

		level.setBlock(posAbove1, copyWaterloggedFrom(level, posAbove1, defaultBlockState().setValue(HALF, PlantopiaTripleBlockHalf.CENTRAL)), 3);
		level.setBlock(posAbove2, copyWaterloggedFrom(level, posAbove2, defaultBlockState().setValue(HALF, PlantopiaTripleBlockHalf.UPPER)), 3);
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

		if(half != PlantopiaTripleBlockHalf.UPPER && facing == Direction.UP && (!neighborState.is(this) || neighborState.getValue(HALF) == half)) {
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
				preventCreativeDropFromBottomPart(level, pos, state, player);
			} else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}

		super.playerWillDestroy(level, pos, state, player);
	}

	protected void preventCreativeDropFromBottomPart(Level level, BlockPos pos, @NotNull BlockState state, Player player) {
		var baseBlockPos = getBaseBlockPos(state, pos);

		preventCreativeDropFromPos(level, baseBlockPos, state, player, pos);
	}

	protected static void preventCreativeDropFromPos(Level level, BlockPos pos, @NotNull BlockState originalState, Player player, BlockPos skippedPos) {
		if(pos == skippedPos) return;

		var state = level.getBlockState(pos);

		if(!state.is(originalState.getBlock())) return;
		if(state.getValue(HALF) != PlantopiaTripleBlockHalf.LOWER) return;

		level.setBlock(pos, getFluidBlockState(level, pos), 35);
		level.levelEvent(player, 2001, pos, Block.getId(state));
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
		builder.add(HALF);
	}

	@Override
	public BlockPos getBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
		return switch(state.getValue(HALF)) {
			case UPPER -> pos.below(2);
			case CENTRAL -> pos.below(1);
			case LOWER -> pos;
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
}