package by.langvest.plantopia.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaFluidHelper {
	public static @NotNull BlockState getFluidBlockState(@NotNull LevelAccessor level, BlockPos pos) {
		return level.getFluidState(pos).createLegacyBlock().getBlock().defaultBlockState();
	}

	public static @NotNull BlockState copyWaterloggedFrom(LevelAccessor level, BlockPos pos, @NotNull BlockState state) {
		if(!state.hasProperty(BlockStateProperties.WATERLOGGED)) return state;
		return state.setValue(BlockStateProperties.WATERLOGGED, level.getFluidState(pos).isSourceOfType(Fluids.WATER));
	}
}