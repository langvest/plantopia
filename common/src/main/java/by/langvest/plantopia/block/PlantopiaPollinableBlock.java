package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface PlantopiaPollinableBlock {
    boolean isValidPollinationTarget(Level level, BlockState state, BlockPos pos, LivingEntity entity);

    default void onPollinationStop(Level level, BlockState state, BlockPos pos, LivingEntity entity, boolean successfully) {
    }
}
