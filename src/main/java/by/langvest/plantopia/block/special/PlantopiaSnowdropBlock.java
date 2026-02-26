package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PlantopiaSnowdropBlock extends FlowerBlock implements PlantopiaFreezableBlock, PlantopiaNaturalBlock {
	public PlantopiaSnowdropBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
		super(effectSupplier, effectDuration, properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
		return context.getItemInHand().is(Items.SNOW);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var level = context.getLevel();
		var clickedPos = context.getClickedPos();
		var clickedState = level.getBlockState(clickedPos);

		if(clickedState.is(Blocks.SNOW) && clickedState.getValue(SnowLayerBlock.LAYERS) == 1) {
			return getCoveredBlock().defaultBlockState();
		}

		return super.getStateForPlacement(context);
	}

	@Override
	public boolean shouldIce(BlockState state, LevelReader level, BlockPos pos, boolean mustBeAtEdge) {
		return false;
	}

	@Override
	public boolean shouldSnow(BlockState state, LevelReader level, BlockPos pos) {
		return true;
	}

	public Block getCoveredBlock() {
		return PlantopiaBlocks.COVERED_SNOWDROP.get();
	}

	@Override
	public void freezeAt(BlockState state, @NotNull BlockState freezingState, LevelAccessor level, BlockPos pos, int flags) {
		if(freezingState.is(Blocks.SNOW)) {
			var newState = getCoveredBlock().defaultBlockState()
				.setValue(PlantopiaCoveredSnowdropBlock.LAYERS, freezingState.getValue(SnowLayerBlock.LAYERS));

			level.setBlock(pos, newState, flags);
		}
	}

	@Override
	public boolean placeNaturallyAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, int flags) {
		var currentState = level.getBlockState(pos);

		if (currentState.is(Blocks.SNOW)) {
			var layers = currentState.getValue(SnowLayerBlock.LAYERS);
			var coveredState = getCoveredBlock().defaultBlockState().setValue(PlantopiaCoveredSnowdropBlock.LAYERS, layers);
			return level.setBlock(pos, coveredState, flags);
		}

		return level.setBlock(pos, state, flags);
	}
}
