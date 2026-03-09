package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaHangingSeaMossBlock extends GrowingPlantHeadBlock {
	private static final double GROW_PER_TICK_PROBABILITY = 0.1D;
	private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PlantopiaHangingSeaMossBlock(Properties properties) {
		super(properties, Direction.DOWN, SHAPE, true, GROW_PER_TICK_PROBABILITY);
		registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(state.getValue(AGE) >= MAX_AGE) return;
		BlockPos nextPos = pos.relative(growthDirection);
		if(!net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, nextPos, level.getBlockState(nextPos),random.nextDouble() < GROW_PER_TICK_PROBABILITY)) return;
		if(!canGrowInto(level.getBlockState(nextPos))) return;
		level.setBlockAndUpdate(nextPos, copyWaterloggedFrom(level, nextPos, getGrowIntoState(state, level.random)));
		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, nextPos, level.getBlockState(nextPos));
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState state) {
		return state.getValue(AGE) < MAX_AGE && state.getValue(WATERLOGGED);
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		int amountToGrow = this.getBlocksToGrowWhenBonemealed(random);
		BlockPos nextPos = pos.relative(growthDirection);
		int nextAge = Math.min(MAX_AGE, state.getValue(AGE) + 1);
		for(int i = 0; i < amountToGrow && canGrowInto(level.getBlockState(nextPos)); i++) {
			level.setBlockAndUpdate(nextPos, copyWaterloggedFrom(level, nextPos, state.setValue(AGE, nextAge)));
			nextPos = nextPos.relative(growthDirection);
			nextAge = Math.min(MAX_AGE, nextAge + 1);
		}
	}

	@Override
	protected boolean canAttachTo(@NotNull BlockState state) {
		return super.canAttachTo(state) && !state.is(Blocks.MAGMA_BLOCK);
	}

	@Override
	protected @NotNull BlockState updateBodyAfterConvertedFromHead(@NotNull BlockState headState, @NotNull BlockState bodyState) {
		return bodyState.setValue(WATERLOGGED, headState.getValue(WATERLOGGED));
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
		return copyWaterloggedFrom(context.getLevel(), context.getClickedPos(), state);
	}

	@Override
	protected int getBlocksToGrowWhenBonemealed(@NotNull RandomSource random) {
		return random.nextInt(3) + 1;
	}

	@Override
	protected boolean canGrowInto(@NotNull BlockState state) {
		return state.isAir() || (state.is(Blocks.WATER) && state.getFluidState().isSource());
	}

	@Override
	protected @NotNull Block getBodyBlock() {
		return PlantopiaBlocks.HANGING_SEA_MOSS_PLANT.get();
	}
}