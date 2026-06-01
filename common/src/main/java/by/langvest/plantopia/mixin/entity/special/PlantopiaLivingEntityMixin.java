package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class PlantopiaLivingEntityMixin {
    @Redirect(
        method = "handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;onClimbable()Z"
        )
    )
    private boolean handleRelativeFrictionAndCalculateMovement(@NotNull LivingEntity livingEntity) {
        return livingEntity.onClimbable() || (livingEntity.getFeetBlockState().getBlock() instanceof PlantopiaQuicksandBlock && PlantopiaQuicksandBlock.canEntityWalkOnQuicksand(livingEntity));
    }
}
