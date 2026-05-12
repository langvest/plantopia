package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.PlantopiaBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlantopiaFrozenReedBlockEntity extends BlockEntity {
	public PlantopiaFrozenReedBlockEntity(BlockPos pos, BlockState state) {
		super(PlantopiaBlockEntityTypes.FROZEN_REED.get(), pos, state);
	}

	public Block getPlantBlock() {
		return PlantopiaBlocks.REED.get();
	}
}
