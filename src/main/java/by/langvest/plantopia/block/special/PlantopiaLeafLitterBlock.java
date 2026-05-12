package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaLeafLitterBlock extends BushBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	public static final int MIN_LEAFS = 1;
	public static final int MAX_LEAFS = 4;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty AMOUNT = PlantopiaBlockStateProperties.SEGMENT_AMOUNT;

	public PlantopiaLeafLitterBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, MIN_LEAFS));
	}

    @Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var posBelow = pos.below();
		var stateBelow = level.getBlockState(posBelow);

		return stateBelow.isFaceSturdy(level, posBelow, Direction.UP);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
		return !context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && state.getValue(AMOUNT) < MAX_LEAFS || super.canBeReplaced(state, context);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var state = context.getLevel().getBlockState(context.getClickedPos());

		if(state.is(this)) {
			return state.setValue(AMOUNT, Math.min(MAX_LEAFS, state.getValue(AMOUNT) + 1));
		}

		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING, AMOUNT);
	}
}
