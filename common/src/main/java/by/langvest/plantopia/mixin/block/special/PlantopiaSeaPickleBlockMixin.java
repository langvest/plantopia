package by.langvest.plantopia.mixin.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SeaPickleBlock.class)
public abstract class PlantopiaSeaPickleBlockMixin {
    @Inject(
        method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At(
            value = "RETURN",
            ordinal = 0
        ),
        cancellable = true
    )
    private void getSameStateForPlacement(@NotNull BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos posBelow = pos.below();
        BlockState stateBelow = level.getBlockState(posBelow);
        if (!stateBelow.isFaceSturdy(level, posBelow, Direction.UP)) cir.setReturnValue(null);
    }

    @Inject(
        method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
        at = @At(
            value = "RETURN",
            ordinal = 1
        ),
        cancellable = true
    )
    private void getNewStateForPlacement(@NotNull BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockPos posBelow = pos.below();
        if (!Block.canSupportCenter(level, posBelow, Direction.UP)) cir.setReturnValue(null);
    }
}