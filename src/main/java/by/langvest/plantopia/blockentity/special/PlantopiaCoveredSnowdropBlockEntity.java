package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.PlantopiaBlockEntities;
import by.langvest.plantopia.block.special.PlantopiaCoveredSnowdropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
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

	public Block getFlowerBlock() {
		return PlantopiaBlocks.SNOWDROP.get();
	}
}
