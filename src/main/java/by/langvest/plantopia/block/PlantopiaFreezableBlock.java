package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface PlantopiaFreezableBlock {
    boolean shouldIce(BlockState state, LevelReader level, BlockPos pos, boolean mustBeAtEdge);

    boolean shouldSnow(BlockState state, LevelReader level, BlockPos pos);

    void freezeAt(BlockState state, BlockState freezingState, LevelAccessor level, BlockPos pos, int flags);
}
