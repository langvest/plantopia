package by.langvest.plantopia.block.special;

import by.langvest.plantopia.util.helper.PlantopiaFluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

public class PlantopiaWaterloggedDoublePlantBlock extends DoublePlantBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PlantopiaWaterloggedDoublePlantBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED);
	}

	protected boolean mayGrowOn(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var lowerFluidState = level.getFluidState(pos.above(1));
		var upperFluidState = level.getFluidState(pos.above(2));

		return mayPlaceOn(state, level, pos)
			&& (lowerFluidState.isSourceOfType(Fluids.WATER) || lowerFluidState.isEmpty())
			&& upperFluidState.isEmpty();
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		if(state.getValue(HALF) == DoubleBlockHalf.UPPER) return super.canSurvive(state, level, pos);

		var posBelow = pos.below();
		var stateBelow = level.getBlockState(posBelow);

		return mayGrowOn(stateBelow, level, posBelow);
	}

	@Override
	public boolean canPlaceLiquid(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid) {
		return SimpleWaterloggedBlock.super.canPlaceLiquid(level, pos, state, fluid) && state.getValue(HALF) == DoubleBlockHalf.LOWER;
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
		if(!canSurvive(state, level, pos)) return getFluidBlockState(level, pos);

		BlockState newState = super.updateShape(state, facing, facingState, level, pos, facingPos);

		if(newState.is(this) && newState.getValue(WATERLOGGED)) {
			level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return newState;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		if(state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
		return super.getFluidState(state);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		if(state == null) return null;
		return PlantopiaFluidHelper.copyWaterloggedFrom(context.getLevel(), context.getClickedPos(), state);
	}
}