package by.langvest.plantopia.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public interface PlantopiaBalkLikeBlock {
    DirectionProperty FACING = BlockStateProperties.FACING;
    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
}
