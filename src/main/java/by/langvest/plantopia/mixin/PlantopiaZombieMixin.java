package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.entity.goal.PlantopiaZombieFindHogweedGoal;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class PlantopiaZombieMixin {
	@Unique
	private int plantopia$tickCount = 0;

	@Inject(
		method = "registerGoals()V",
		at = @At("TAIL")
	)
	private void registerGoals(CallbackInfo ci) {
		Zombie mob = (Zombie)(Object)this;

		mob.goalSelector.addGoal(1, new PlantopiaZombieFindHogweedGoal(mob, 1.0D));
	}

	@Inject(
		method = "tick()V",
		at = @At("HEAD")
	)
	private void tick(CallbackInfo ci) {
		plantopia$tickCount++;

		Zombie mob = (Zombie)(Object)this;
		var level = mob.level();
		var random = mob.getRandom();

		if(level.isClientSide() && plantopia$isTreating() && plantopia$tickCount % 40 == 0) {
			var particleAmount = Mth.nextInt(random, 2, 5);

			for(int i = 0; i < particleAmount; i++) {
				double x = mob.getX() + 0.0D + Mth.nextDouble(random, -0.4D, 0.4D);
				double y = mob.getY() + 1.0D + Mth.nextDouble(random, -0.8D, 0.8D);
				double z = mob.getZ() + 0.0D + Mth.nextDouble(random, -0.4D, 0.4D);

				level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
			}
		}
	}

	@Unique
	private boolean plantopia$isTreating() {
		Zombie mob = (Zombie)(Object)this;
		var level = mob.level();
		var state = level.getBlockState(mob.blockPosition());

		return mob.getHealth() < mob.getMaxHealth() && state.is(PlantopiaBlocks.HOGWEED.get());
	}
}
