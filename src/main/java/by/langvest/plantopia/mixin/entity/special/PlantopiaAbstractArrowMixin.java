package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class PlantopiaAbstractArrowMixin {
	@Redirect(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
		)
	)
	private boolean tick$is(BlockState state, @NotNull Block block) {
		if(block instanceof PowderSnowBlock) {
			return state.is(block) || state.getBlock() instanceof PlantopiaQuicksandBlock;
		}

		return state.is(block);
	}
}
