package by.langvest.plantopia.block.entity.special;

import by.langvest.plantopia.block.entity.PlantopiaBlockEntities;
import by.langvest.plantopia.block.special.PlantopiaCoveredSnowdropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlantopiaCoveredSnowdropBlockEntity extends BlockEntity {
	public PlantopiaCoveredSnowdropBlockEntity(BlockPos pos, BlockState state) {
		super(PlantopiaBlockEntities.COVERED_SNOWDROP.get(), pos, state);
	}

	public boolean skipFlowerRendering() {
		var state = getBlockState();
		PlantopiaCoveredSnowdropBlock block = (PlantopiaCoveredSnowdropBlock)state.getBlock();
		return block.skipFlowerRendering(state);
	}
}
