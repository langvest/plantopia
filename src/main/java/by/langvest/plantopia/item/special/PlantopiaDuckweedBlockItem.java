package by.langvest.plantopia.item.special;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class PlantopiaDuckweedBlockItem extends BlockItem {
	public PlantopiaDuckweedBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	/**
	 * Called when this item is used when targeting a Block
	 */
	@Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		var targetPos = context.getClickedPos();
		var targetState = context.getLevel().getBlockState(targetPos);

		if(targetState.is(getBlock())) {
			return super.useOn(context);
		}

		return InteractionResult.PASS;
	}

	/**
	 * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
	 * onItemUse.
	 */
	@Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		var blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		var blockHitResultAbove = blockHitResult.withPosition(blockHitResult.getBlockPos().above());
		var interactionResult = super.useOn(new UseOnContext(player, hand, blockHitResultAbove));
		return new InteractionResultHolder<>(interactionResult, player.getItemInHand(hand));
	}
}
