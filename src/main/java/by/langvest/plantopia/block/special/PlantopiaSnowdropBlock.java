package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PlantopiaSnowdropBlock extends FlowerBlock {
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
			return PlantopiaBlocks.COVERED_SNOWDROP.get().defaultBlockState();
		}

		return super.getStateForPlacement(context);
	}
}
