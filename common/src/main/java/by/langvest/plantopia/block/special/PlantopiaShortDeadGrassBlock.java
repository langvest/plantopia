package by.langvest.plantopia.block.special;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaShortDeadGrassBlock extends PlantopiaShortBushBlock{
	public PlantopiaShortDeadGrassBlock(Properties properties, Supplier<Block> tallVariant) {
		super(properties, tallVariant);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(PlantopiaBlockTags.DEAD_GRASS_MAY_PLACE_ON);
	}
}
