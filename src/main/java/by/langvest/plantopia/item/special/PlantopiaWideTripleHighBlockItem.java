package by.langvest.plantopia.item.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.getFluidBlockState;

public class PlantopiaWideTripleHighBlockItem extends BlockItem {
	public PlantopiaWideTripleHighBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	protected boolean placeBlock(@NotNull BlockPlaceContext context, @NotNull BlockState state) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockPos basePos = getBaseBlockPos(state, pos);
		BlockPos basePosAbove1 = basePos.above(1);
		BlockPos basePosAbove2 = basePos.above(2);
		clearSliceAt(level, basePos, 27, pos);
		clearSliceAt(level, basePosAbove1, 27, pos);
		clearSliceAt(level, basePosAbove2, 27, pos);
		return super.placeBlock(context, state);
	}

	private static BlockPos getBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
		if(!state.hasProperty(PlantopiaBlockStateProperties.QUARTER)) return pos;
		return switch(state.getValue(PlantopiaBlockStateProperties.QUARTER)) {
			case SOUTH_WEST -> pos;
			case WEST_NORTH -> pos.south();
			case NORTH_EAST -> pos.south().west();
			case EAST_SOUTH -> pos.west();
		};
	}

	private static void clearSliceAt(@NotNull LevelAccessor level, @NotNull BlockPos southWestPos, int flags, BlockPos ignoredPos) {
		BlockPos westNorthPos = southWestPos.north();
		BlockPos northEastPos = westNorthPos.east();
		BlockPos eastSouthPos = northEastPos.south();
		if(ignoredPos != southWestPos) level.setBlock(southWestPos, getFluidBlockState(level, southWestPos), flags);
		if(ignoredPos != westNorthPos) level.setBlock(westNorthPos, getFluidBlockState(level, westNorthPos), flags);
		if(ignoredPos != northEastPos) level.setBlock(northEastPos, getFluidBlockState(level, northEastPos), flags);
		if(ignoredPos != eastSouthPos) level.setBlock(eastSouthPos, getFluidBlockState(level, eastSouthPos), flags);
	}
}