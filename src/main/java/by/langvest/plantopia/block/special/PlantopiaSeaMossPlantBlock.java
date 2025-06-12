package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaSeaMoss;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PlantopiaSeaMossPlantBlock extends GrowingPlantBodyBlock implements PlantopiaSeaMoss {
	public PlantopiaSeaMossPlantBlock(Properties properties) {
		super(properties, Direction.DOWN, SHAPE, true);
		registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));

	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}

	@Override
	protected @NotNull BlockState updateHeadAfterConvertedFromBody(@NotNull BlockState headState, @NotNull BlockState bodyState) {
		return bodyState.setValue(WATERLOGGED, headState.getValue(WATERLOGGED));
	}

	@Override
	protected boolean canAttachTo(@NotNull BlockState state) {
		return super.canAttachTo(state) && !state.is(Blocks.MAGMA_BLOCK);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		if(state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
		return super.getFluidState(state);
	}

	@Override
	protected @NotNull GrowingPlantHeadBlock getHeadBlock() {
		return (GrowingPlantHeadBlock)PlantopiaBlocks.SEA_MOSS.get();
	}
}