package by.langvest.plantopia.mixin.entity;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PowderSnowBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityType.class)
public abstract class PlantopiaEntityTypeMixin {
    @Redirect(
        method = "isBlockDangerous(Lnet/minecraft/world/level/block/state/BlockState;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean isBlockDangerous$is(BlockState state, @NotNull Block block) {
        if (block instanceof PowderSnowBlock) {
            return state.is(block) || state.getBlock() instanceof PlantopiaQuicksandBlock;
        }

        return state.is(block);
    }
}
