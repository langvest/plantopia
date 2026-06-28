package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCoveredSnowdropBlock;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowLayerBlock.class)
public abstract class PlantopiaSnowLayerBlockMixin {
    @Inject(
        method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void getStateForPlacement(@NotNull BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        var clickedState = context.getLevel().getBlockState(context.getClickedPos());

        if (clickedState.is(PlantopiaBlocks.SNOWDROP.get())) {
            cir.setReturnValue(PlantopiaBlocks.COVERED_SNOWDROP.get().defaultBlockState());
        } else if (clickedState.is(PlantopiaBlocks.COVERED_SNOWDROP.get())) {
            int layers = clickedState.getValue(PlantopiaCoveredSnowdropBlock.LAYERS);
            int newLayers = Math.min(PlantopiaCoveredSnowdropBlock.MAX_HEIGHT, layers + 1);
            cir.setReturnValue(clickedState.setValue(PlantopiaCoveredSnowdropBlock.LAYERS, newLayers));
        }
    }

    @Redirect(
        method = "canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean canSurvive$is(BlockState state, @NotNull Block block) {
        if (block instanceof SnowLayerBlock) {
            return state.is(block) || state.is(PlantopiaBlocks.COVERED_SNOWDROP.get());
        }

        return state.is(block);
    }
}
