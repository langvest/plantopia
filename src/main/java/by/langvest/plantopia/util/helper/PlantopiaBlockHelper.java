package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.block.PlantopiaBaseBlockPosGetter;
import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaBlockHelper {
	public static @NotNull BlockState copySnowyAboveFrom(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
		boolean isSnowyAbove = level.getBlockState(pos.above()).is(Blocks.SNOW);

		return state.setValue(BlockStateProperties.SNOWY, isSnowyAbove);
	}

	public static BlockPos getBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
		var block = state.getBlock();

		if(block instanceof PlantopiaBaseBlockPosGetter baseBlockPosGetter) {
			return baseBlockPosGetter.getBaseBlockPos(state, pos);
		}

		if(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
			var half = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF);
			if(half == DoubleBlockHalf.UPPER) return pos.below(1);
			return pos;
		}

		if(state.hasProperty(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF)) {
			var half = state.getValue(PlantopiaBlockStateProperties.TRIPLE_BLOCK_HALF);
			if(half == PlantopiaTripleBlockHalf.UPPER) return pos.below(2);
			if(half == PlantopiaTripleBlockHalf.CENTRAL) return pos.below(1);
			return pos;
		}

		return pos;
	}
}
