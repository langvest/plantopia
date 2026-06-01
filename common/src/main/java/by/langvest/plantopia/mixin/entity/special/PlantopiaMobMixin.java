package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.Contract;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mob.class)
public abstract class PlantopiaMobMixin {
    @Contract(pure = true)
    @Redirect(
        method = "isSunBurnTick()Z",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/Mob;isInPowderSnow:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean isInPowderSnow(Mob mob) {
        if (mob instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
            return mob.isInPowderSnow || quicksandExtension.plantopia$isInQuicksand() || quicksandExtension.plantopia$wasInQuicksand();
        }

        return mob.isInPowderSnow;
    }
}
