package by.langvest.plantopia.mixin;

import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Contract;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlantopiaPlayerMixin {
	@Contract(pure = true)
	@Redirect(
		method = "aiStep()V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/player/Player;isInPowderSnow:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean aiStep$isInPowderSnow(Player player) {
		if(player instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
			return player.isInPowderSnow || quicksandExtension.plantopia$isInQuicksand();
		}

		return player.isInPowderSnow;
	}

	@Contract(pure = true)
	@Redirect(
		method = "setEntityOnShoulder(Lnet/minecraft/nbt/CompoundTag;)Z",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/entity/player/Player;isInPowderSnow:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean setEntityOnShoulder$isInPowderSnow(Player player) {
		if(player instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
			return player.isInPowderSnow || quicksandExtension.plantopia$isInQuicksand();
		}

		return player.isInPowderSnow;
	}
}
