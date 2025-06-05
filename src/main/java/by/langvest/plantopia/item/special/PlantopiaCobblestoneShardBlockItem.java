package by.langvest.plantopia.item.special;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlantopiaCobblestoneShardBlockItem extends BlockItem {
	protected Block petBlock;

	public PlantopiaCobblestoneShardBlockItem(Block block, Block petBlock, Properties properties) {
		super(block, properties);
		this.petBlock = petBlock;
	}

	public Block getPetBlock() {
		return petBlock;
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(@NotNull BlockPlaceContext context) {
		var itemStack = context.getItemInHand();

		if(itemStack.hasCustomHoverName()) {
			var state = getPetBlock().getStateForPlacement(context);
			return state != null && canPlace(context, state) ? state : null;
		}

		return super.getPlacementState(context);
	}
}
