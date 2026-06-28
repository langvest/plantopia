package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface PlantopiaBaseBlockPosGetter {
	BlockPos getBaseBlockPos(BlockState state, BlockPos pos);
}
