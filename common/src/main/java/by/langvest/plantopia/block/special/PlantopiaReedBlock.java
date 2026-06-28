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

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaReedBlock extends PlantopiaWaterloggedDoublePlantBlock implements PlantopiaFreezableBlock {
	public PlantopiaReedBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return super.mayPlaceOn(state, level, pos)
			|| state.is(BlockTags.SAND)
			|| state.is(Blocks.GRAVEL)
			|| state.is(Blocks.SUSPICIOUS_GRAVEL)
			|| state.is(Blocks.CLAY);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		var posBelow = pos.below();
		var stateBelow = level.getBlockState(posBelow);

		if(state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			return stateBelow.is(this) || stateBelow.is(PlantopiaBlocks.FROZEN_REED.get());
		}

		return mayGrowOn(stateBelow, level, posBelow);
	}

	@Override
	public @NotNull BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if(state.getValue(HALF) == DoubleBlockHalf.UPPER && facing == Direction.DOWN && state.canSurvive(level, pos)) {
			return state;
		}

		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	@Override
	public boolean shouldIce(BlockState state, LevelReader level, BlockPos pos, boolean mustBeAtEdge) {
		return state.getValue(HALF) == DoubleBlockHalf.LOWER
			&& state.getValue(WATERLOGGED);
	}

	@Override
	public boolean shouldSnow(BlockState state, LevelReader level, BlockPos pos) {
		return false;
	}

	@Override
	public void freezeAt(BlockState state, BlockState freezingState, LevelAccessor level, BlockPos pos, int flags) {
		if(freezingState.is(Blocks.ICE)) {
			level.setBlock(pos, PlantopiaBlocks.FROZEN_REED.get().defaultBlockState(), flags);
		}
	}
}
