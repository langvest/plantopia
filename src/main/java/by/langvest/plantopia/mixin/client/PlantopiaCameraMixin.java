package by.langvest.plantopia.mixin.client;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.client.Camera;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Camera.class)
public abstract class PlantopiaCameraMixin {
	@Redirect(
		method = "getFluidInCamera()Lnet/minecraft/world/level/material/FogType;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
		)
	)
	private boolean getFluidInCamera$is(BlockState state, @NotNull Block block) {
		if(block instanceof PowderSnowBlock) {
			return state.is(block) || state.getBlock() instanceof PlantopiaQuicksandBlock;
		}

		return state.is(block);
	}
}
