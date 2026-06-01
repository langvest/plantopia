package by.langvest.plantopia.mixin.block.special;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TurtleEggBlock.class)
public abstract class PlantopiaTurtleEggBlockMixin {
    @Inject(
        method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getStateForPlacement(@NotNull BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        var clickedPos = context.getClickedPos();
        var level = context.getLevel();
        var fluidState = level.getFluidState(clickedPos);
        var posBelow = clickedPos.below();
        var stateBelow = level.getBlockState(posBelow);

        if (!fluidState.isEmpty() || !stateBelow.isFaceSturdy(level, posBelow, Direction.UP, SupportType.FULL)) {
            cir.setReturnValue(null);
        }
    }
}
