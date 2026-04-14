package by.langvest.plantopia.item.special;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class PlantopiaIceCrustBlockItem extends BlockItem {
	public PlantopiaIceCrustBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	/**
	 * Called when this item is used when targeting a Block
	 */
	@Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		var level = context.getLevel();
		var replaceContext = new BlockPlaceContext(context);

		BlockState stateToInspect = null;

		var targetPos = context.getClickedPos();
		var targetState = level.getBlockState(targetPos);

		if (canPlaceInto(targetState, replaceContext)) {
			stateToInspect = targetState;
		} else {
			var attachedPos = targetPos.relative(context.getClickedFace());
			var attachedState = level.getBlockState(attachedPos);

			if (canPlaceInto(attachedState, replaceContext)) {
				stateToInspect = attachedState;
			}
		}

		if (stateToInspect != null && stateToInspect.getFluidState().isSourceOfType(Fluids.WATER)) {
			return InteractionResult.PASS;
		}

		return super.useOn(context);
	}

	private boolean canPlaceInto(@NotNull BlockState state, BlockPlaceContext context) {
		return state.is(getBlock()) || state.canBeReplaced(context);
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
