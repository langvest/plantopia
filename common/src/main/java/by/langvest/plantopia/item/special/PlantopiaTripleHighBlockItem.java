package by.langvest.plantopia.item.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

@ParametersAreNonnullByDefault
public class PlantopiaTripleHighBlockItem extends BlockItem {
    public PlantopiaTripleHighBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos posAbove1 = pos.above(1);
        BlockPos posAbove2 = pos.above(2);
        level.setBlock(posAbove1, getFluidBlockState(level, posAbove1), 27);
        level.setBlock(posAbove2, getFluidBlockState(level, posAbove2), 27);
        return super.placeBlock(context, state);
    }
}
