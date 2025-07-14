package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;

public interface PlantopiaFloweringWaterlilyBlock {
	Block getWaterlilyBlock();

	Block getOriginBlock();

	default @NotNull InteractionResult useShears(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
		var itemInHand = player.getItemInHand(hand);

		if(!itemInHand.canPerformAction(ToolActions.SHEARS_CARVE)) {
			return InteractionResult.PASS;
		}

		if(!level.isClientSide()) {
			Direction up = Direction.UP;

			level.playSound(null, pos, SoundEvents.MOOSHROOM_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, getOriginBlock().defaultBlockState(), 11);

			ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.2D, (double)pos.getZ() + 0.5D, getWaterlilyBlock().asItem().getDefaultInstance());
			itementity.setDeltaMovement(0.05D * (double)up.getStepX() + level.random.nextDouble() * 0.02D, 0.1D, 0.05D * (double)up.getStepZ() + level.random.nextDouble() * 0.02D);
			level.addFreshEntity(itementity);
			itemInHand.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(hand));
			level.gameEvent(player, GameEvent.SHEAR, pos);
			player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
