package by.langvest.plantopia.block.special;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public class PlantopiaDirectionalPillarBlock extends DirectionalBlock {
	public PlantopiaDirectionalPillarBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		Direction facing = context.getClickedFace();
		return defaultBlockState().setValue(FACING, facing);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
