package by.langvest.plantopia.mixin.client.render.entity;

import by.langvest.plantopia.extension.PlantopiaZombieQuicksandExtension;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractZombieRenderer.class)
public abstract class PlantopiaAbstractZombieRendererMixin {
    @Inject(
        method = "isShaking(Lnet/minecraft/world/entity/monster/Zombie;)Z",
        at = @At("RETURN"),
        cancellable = true
    )
    private void isShaking(Zombie zombie, CallbackInfoReturnable<Boolean> cir) {
        if (zombie instanceof PlantopiaZombieQuicksandExtension zombieQuicksandExtension) {
            cir.setReturnValue(cir.getReturnValue() || zombieQuicksandExtension.plantopia$isInQuicksandConverting());
        }
    }
}
