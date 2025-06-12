package by.langvest.plantopia.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface PlantopiaSeaMoss extends SimpleWaterloggedBlock {
	VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
	BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
}
