package by.langvest.plantopia.mixin.item.special;

import by.langvest.plantopia.entity.PlantopiaBoatLike;
import by.langvest.plantopia.item.special.PlantopiaBoatItem;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoatItem.class)
public abstract class PlantopiaBoatItemMixin {
	@Redirect(
		method = "use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResultHolder;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/vehicle/Boat;setVariant(Lnet/minecraft/world/entity/vehicle/Boat$Type;)V"
		)
	)
	private void use$setVariant(Boat boat, Boat.Type variant) {
		BoatItem boatItem = (BoatItem) (Object) this;

		if (boatItem instanceof PlantopiaBoatItem boatItemLike && boat instanceof PlantopiaBoatLike boatLike) {
			boatLike.setBoatType(boatItemLike.getBoatType());
			return;
		}

		boat.setVariant(variant);
	}
}
