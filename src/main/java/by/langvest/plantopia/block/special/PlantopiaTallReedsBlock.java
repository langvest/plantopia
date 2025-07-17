package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaTallReedsBlock extends PlantopiaWaterloggedTriplePlantBlock {
	public PlantopiaTallReedsBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return super.mayPlaceOn(state, level, pos)
			|| state.is(BlockTags.SAND)
			|| state.is(Blocks.GRAVEL)
			|| state.is(Blocks.CLAY);
	}
}