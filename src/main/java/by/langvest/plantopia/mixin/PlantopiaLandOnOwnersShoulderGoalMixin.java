package by.langvest.plantopia.mixin;

import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
import org.jetbrains.annotations.Contract;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LandOnOwnersShoulderGoal.class)
public abstract class PlantopiaLandOnOwnersShoulderGoalMixin {
	@Contract(pure = true)
	@Redirect(
		method = "canUse()Z",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/server/level/ServerPlayer;isInPowderSnow:Z",
			opcode = Opcodes.GETFIELD
		)
	)
	private boolean isInPowderSnow(ServerPlayer player) {
		if(player instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
			return player.isInPowderSnow || quicksandExtension.plantopia$isInQuicksand();
		}

		return player.isInPowderSnow;
	}
}
