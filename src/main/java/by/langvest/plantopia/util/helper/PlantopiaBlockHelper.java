package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaBlockHelper {
	public static @NotNull BlockState copySnowyAboveFrom(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
		boolean isSnowyAbove = level.getBlockState(pos.above()).is(Blocks.SNOW);

		return state.setValue(BlockStateProperties.SNOWY, isSnowyAbove);
	}
}
