package by.langvest.plantopia.mixin.block.special;

import by.langvest.plantopia.worldgen.feature.special.PlantopiaCactusColumnFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CactusBlock.class)
public abstract class PlantopiaCactusBlockMixin {
    @Redirect(
        method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        )
    )
    private boolean randomTick$setBlock(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state, int flags) {
        if (state.is(Blocks.CACTUS) && level.isEmptyBlock(pos)) {
            return this.plantopia$placeCactusWithDecoration(level, pos, state, flags);
        }

        return level.setBlock(pos, state, flags);
    }

    @Redirect(
        method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
        )
    )
    private boolean randomTick$setBlockAndUpdate(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.is(Blocks.CACTUS) && level.isEmptyBlock(pos)) {
            return this.plantopia$placeCactusWithDecoration(level, pos, state, Block.UPDATE_ALL);
        }

        return level.setBlockAndUpdate(pos, state);
    }

    @Unique
    private boolean plantopia$placeCactusWithDecoration(@NotNull ServerLevel level, BlockPos pos, BlockState state, int flags) {
        var random = level.random;
        boolean allowTopDecoration = random.nextBoolean();
        return PlantopiaCactusColumnFeature.placeCactusBlock(level, pos, state, flags, random, allowTopDecoration);
    }
}
