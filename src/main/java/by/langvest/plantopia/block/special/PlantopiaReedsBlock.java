package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

public class PlantopiaReedsBlock extends PlantopiaWaterloggedDoublePlantBlock implements PlantopiaFreezableBlock {
	public PlantopiaReedsBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return super.mayPlaceOn(state, level, pos)
			|| state.is(BlockTags.SAND)
			|| state.is(Blocks.GRAVEL)
			|| state.is(Blocks.SUSPICIOUS_GRAVEL)
			|| state.is(Blocks.CLAY);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var posBelow = pos.below();
		var stateBelow = level.getBlockState(posBelow);

		if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			return stateBelow.is(this) || stateBelow.is(PlantopiaBlocks.ICY_REEDS.get());
		}

		return mayGrowOn(stateBelow, level, posBelow);
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
		if(state.getValue(HALF) == DoubleBlockHalf.UPPER && facing == Direction.DOWN && state.canSurvive(level, pos)) {
			return state;
		}

		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	@Override
	public boolean shouldIce(@NotNull BlockState state, LevelReader level, BlockPos pos, boolean mustBeAtEdge) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER
			&& state.getValue(WATERLOGGED);
	}

	@Override
	public boolean shouldSnow(BlockState state, LevelReader level, BlockPos pos) {
		return false;
	}

	@Override
	public void freezeAt(BlockState state, @NotNull BlockState freezingState, LevelAccessor level, BlockPos pos, int flags) {
		if(freezingState.is(Blocks.ICE)) {
			level.setBlock(pos, PlantopiaBlocks.ICY_REEDS.get().defaultBlockState(), flags);
		}
	}
}