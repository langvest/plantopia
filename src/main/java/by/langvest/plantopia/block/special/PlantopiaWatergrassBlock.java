package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PlantopiaWatergrassBlock extends PlantopiaWaterloggedDoublePlantBlock {
	public PlantopiaWatergrassBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayGrowOn(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var lowerFluidState = level.getFluidState(pos.above(1));
		var upperFluidState = level.getFluidState(pos.above(2));

		return mayPlaceOn(state, level, pos)
			&& lowerFluidState.isSourceOfType(Fluids.WATER)
			&& upperFluidState.isEmpty();
	}
}