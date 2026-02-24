package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaTriplePlantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DoublePlantBlock.class)
public abstract class PlantopiaDoublePlantBlockMixin implements BonemealableBlock {
	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		return state.is(Blocks.TALL_GRASS) || state.is(Blocks.LARGE_FERN);
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return true;
	}

	@Unique
	@Nullable
	private static PlantopiaTriplePlantBlock plantopia$getTriplePlantVersion(@NotNull BlockState state) {
		if(state.is(Blocks.TALL_GRASS)) return (PlantopiaTriplePlantBlock)PlantopiaBlocks.GIANT_GRASS.get();
		if(state.is(Blocks.LARGE_FERN)) return (PlantopiaTriplePlantBlock)PlantopiaBlocks.GIANT_FERN.get();
		return null;
	}

	@Unique
	private static BlockPos plantopia$getDoublePlantBaseBlockPos(@NotNull BlockState state, BlockPos pos) {
		if(!state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) return pos;
		return switch(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
			case UPPER -> pos.below();
			case LOWER -> pos;
		};
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		PlantopiaTriplePlantBlock triplePlantBlock = plantopia$getTriplePlantVersion(state);
		if(triplePlantBlock == null) return;
		BlockPos basePos = plantopia$getDoublePlantBaseBlockPos(state, pos);
		if(!level.isEmptyBlock(basePos.above(2))) return;
		if(!triplePlantBlock.defaultBlockState().canSurvive(level, basePos)) return;
		PlantopiaTriplePlantBlock.placeAt(level, basePos, triplePlantBlock.defaultBlockState(), 27);
	}
}