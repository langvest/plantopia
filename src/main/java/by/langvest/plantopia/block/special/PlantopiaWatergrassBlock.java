package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PlantopiaWatergrassBlock extends PlantopiaWaterloggedDoublePlantBlock {
	public PlantopiaWatergrassBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean isValidEnvironment(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		FluidState lowerFluidState = level.getFluidState(pos.above(1));
		FluidState upperFluidState = level.getFluidState(pos.above(2));
		return mayPlaceOn(state, level, pos)
			&& lowerFluidState.isSourceOfType(Fluids.WATER)
			&& upperFluidState.isEmpty();
	}
}