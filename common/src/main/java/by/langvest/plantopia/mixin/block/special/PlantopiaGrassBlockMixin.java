package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GrassBlock.class)
public abstract class PlantopiaGrassBlockMixin {
    @Redirect(
        method = "performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At(
            ordinal = 0,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean performBonemeal$is$0(@NotNull BlockState candidateState, Block sameBlock) {
        return candidateState.is(sameBlock) || candidateState.is(PlantopiaBlockTags.BONEMEAL_SPREAD_ON);
    }

    @Redirect(
        method = "performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At(
            ordinal = 1,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean performBonemeal$is$1(@NotNull BlockState instance, Block block) {
        return instance.is(PlantopiaBlockTags.BONEMEAL_SPREAD_GROWABLE);
    }

    @Redirect(
        method = "performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
        at = @At(
            ordinal = 0,
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V")
    )
    private void performBonemeal$performBonemeal$0(@NotNull BonemealableBlock grass, ServerLevel level, RandomSource random, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof BonemealableBlock bonemealableBlock) {
            bonemealableBlock.performBonemeal(level, random, pos, state);
        }
    }
}
