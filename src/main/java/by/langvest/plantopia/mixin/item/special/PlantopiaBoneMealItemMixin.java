package by.langvest.plantopia.mixin.item.special;

import by.langvest.plantopia.block.special.PlantopiaAzollaBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(BoneMealItem.class)
public abstract class PlantopiaBoneMealItemMixin {
	@Inject(
		method = "addGrowthParticles(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;I)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void addGrowthParticles(@NotNull LevelAccessor level, BlockPos pos, int particleAmount, CallbackInfo ci) {
		var state = level.getBlockState(pos);

		if(particleAmount == 0) {
			particleAmount = 15;
		}

		if(!state.isAir()) {
			double dx;
			double dy;
			Predicate<BlockPos> predicate;

			if(state.getBlock() instanceof PlantopiaAzollaBlock) {
				particleAmount *= 3;
				dx = 2.5D;
				dy = 0.3D;

				predicate = (candidatePos) -> level.getFluidState(candidatePos.below()).isSourceOfType(Fluids.WATER);
			} else {
				return;
			}

			level.addParticle(ParticleTypes.HAPPY_VILLAGER, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D * dy, (double)pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			var random = level.getRandom();

			for(int i = 0; i < particleAmount; ++i) {
				double d2 = random.nextGaussian() * 0.02D;
				double d3 = random.nextGaussian() * 0.02D;
				double d4 = random.nextGaussian() * 0.02D;
				double d5 = 0.5D - dx;
				double d6 = (double)pos.getX() + d5 + random.nextDouble() * dx * 2.0D;
				double d7 = (double)pos.getY() + random.nextDouble() * dy;
				double d8 = (double)pos.getZ() + d5 + random.nextDouble() * dx * 2.0D;

				var candidatePos = BlockPos.containing(d6, d7, d8);

				if(predicate.test(candidatePos)) {
					level.addParticle(ParticleTypes.HAPPY_VILLAGER, d6, d7, d8, d2, d3, d4);
				}
			}

			ci.cancel();
		}
	}
}
