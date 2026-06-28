package by.langvest.plantopia.mixin.level.pathfinder;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WalkNodeEvaluator.class)
public abstract class PlantopiaWalkNodeEvaluatorMixin {
    @Redirect(
        method = "getBlockPathTypeRaw(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/pathfinder/BlockPathTypes;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private static boolean getBlockPathTypeRaw$is(BlockState state, @NotNull Block block) {
        if (block instanceof PowderSnowBlock) {
            return state.is(block) || state.getBlock() instanceof PlantopiaQuicksandBlock;
        }

        return state.is(block);
    }
}
