package by.langvest.plantopia.mixin.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SugarCaneBlock.class)
public abstract class PlantopiaSugarCaneBlockMixin extends Block {
	public PlantopiaSugarCaneBlockMixin(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		FluidState fluidState = level.getFluidState(pos);
		return fluidState.isEmpty() ? super.getStateForPlacement(context) : null;
	}
}