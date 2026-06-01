package by.langvest.plantopia.mixin.level.biome;

import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Biome.class)
public abstract class PlantopiaBiomeMixin {
    @Redirect(
        method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
        )
    )
    public Block shouldFreeze$getBlock(@NotNull BlockState state, LevelReader level, BlockPos pos, boolean mustBeAtEdge) {
        var block = state.getBlock();

        if (block instanceof PlantopiaFreezableBlock freezableBlock) {
            return freezableBlock.shouldIce(state, level, pos, mustBeAtEdge) ? Blocks.WATER : Blocks.AIR;
        }

        return block;
    }

    @Redirect(
        method = "shouldSnow(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"
        )
    )
    public boolean shouldSnow$isAir(@NotNull BlockState state, LevelReader level, BlockPos pos) {
        var block = state.getBlock();

        if (block instanceof PlantopiaFreezableBlock freezableBlock) {
            return freezableBlock.shouldSnow(state, level, pos);
        }

        return state.isAir() || state.getBlock() instanceof SnowLayerBlock;
    }
}
