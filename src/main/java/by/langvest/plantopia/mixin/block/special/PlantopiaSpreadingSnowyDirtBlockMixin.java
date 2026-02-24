package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpreadingSnowyDirtBlock.class)
public abstract class PlantopiaSpreadingSnowyDirtBlockMixin {
	@Redirect(
		method = "canBeGrass(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
		)
	)
	private static boolean canBeGrass$is(BlockState state, @NotNull Block block) {
		if(block instanceof SnowLayerBlock) {
			return state.is(block) || state.is(PlantopiaBlocks.COVERED_SNOWDROP.get());
		}

		return state.is(block);
	}
}
