package by.langvest.plantopia.blockentity.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.PlantopiaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PlantopiaIcyReedsBlockEntity extends BlockEntity {
	public PlantopiaIcyReedsBlockEntity(BlockPos pos, BlockState state) {
		super(PlantopiaBlockEntities.ICY_REEDS.get(), pos, state);
	}

	public Block getPlantBlock() {
		return PlantopiaBlocks.REEDS.get();
	}
}
