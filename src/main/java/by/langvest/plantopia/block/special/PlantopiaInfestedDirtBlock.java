package by.langvest.plantopia.block.special;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class PlantopiaInfestedDirtBlock extends Block {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_25;

	public PlantopiaInfestedDirtBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(AGE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
