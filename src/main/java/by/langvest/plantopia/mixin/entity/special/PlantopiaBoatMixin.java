package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.entity.PlantopiaBoatLike;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Boat.class)
public abstract class PlantopiaBoatMixin {
    @Redirect(
        method = "checkFallDamage(DZLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/vehicle/Boat$Type;getPlanks()Lnet/minecraft/world/level/block/Block;"
        )
    )
    private @NotNull Block checkFallDamage$getPlanks(Boat.Type variant) {
        Boat boat = (Boat) (Object) this;

        if (boat instanceof PlantopiaBoatLike boatLike) {
            return boatLike.getDropPlanks();
        }

        return variant.getPlanks();
    }
}
