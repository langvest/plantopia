package by.langvest.plantopia.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

public class PlantopiaTripleHighBlockItem extends BlockItem {
	public PlantopiaTripleHighBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockPos posAbove1 = pos.above(1);
		BlockPos posAbove2 = pos.above(2);
		level.setBlock(posAbove1, getFluidBlockState(level, posAbove1), 27);
		level.setBlock(posAbove2, getFluidBlockState(level, posAbove2), 27);
		return super.placeBlock(context, state);
	}
}