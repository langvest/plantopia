package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaFreezableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SnowAndFreezeFeature;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SnowAndFreezeFeature.class)
public abstract class PlantopiaSnowAndFreezeFeatureMixin {
    @Redirect(
        method = "place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        )
    )
    public boolean place$setBlock(@NotNull WorldGenLevel level, BlockPos pos, BlockState state, int flags) {
        var targetState = level.getBlockState(pos);

        if(targetState.getBlock() instanceof PlantopiaFreezableBlock freezableBlock) {
            freezableBlock.freezeAt(targetState, state, level, pos, flags);
            return true;
        }

        return level.setBlock(pos, state, flags);
    }
}
