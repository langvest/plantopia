package by.langvest.plantopia.mixin.client.render.block;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlockRenderer.class)
public abstract class PlantopiaLiquidBlockRendererMixin {
    @Inject(
        method = "isFaceOccludedByState(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/Direction;FLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void isFaceOccludedByState(BlockGetter level, Direction face, float height, BlockPos pos, @NotNull BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(PlantopiaBlocks.QUICKSAND.get())) {
            cir.setReturnValue(true);
        }
    }
}
