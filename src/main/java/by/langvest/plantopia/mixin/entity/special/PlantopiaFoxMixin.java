package by.langvest.plantopia.mixin.entity.special;

import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import net.minecraft.world.entity.animal.Fox;
import org.jetbrains.annotations.Contract;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.entity.animal.Fox$SleepGoal")
public abstract class PlantopiaFoxMixin {
	@Contract(pure = true)
	@Redirect(
		method = "canSleep()Z",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/animal/Fox;isInPowderSnow:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean isInPowderSnow(Fox fox) {
		if(fox instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
			return fox.isInPowderSnow || quicksandExtension.plantopia$isInQuicksand();
		}

		return fox.isInPowderSnow;
	}
}
