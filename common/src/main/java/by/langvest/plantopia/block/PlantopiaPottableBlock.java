package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public interface PlantopiaPottableBlock {
    BlockState updatePottedState(BlockState newPottedState, BlockState oldPottedState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult);
}
