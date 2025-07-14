package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.special.PlantopiaDuckweedBlock;
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
	private static void addGrowthParticles(@NotNull LevelAccessor level, BlockPos pos, int data, CallbackInfo ci) {
		var state = level.getBlockState(pos);

		if(data == 0) {
			data = 15;
		}

		if(!state.isAir()) {
			double d0;
			double d1;
			Predicate<BlockPos> predicate;

			if(state.getBlock() instanceof PlantopiaDuckweedBlock) {
				data *= 3;
				d0 = 2.5D;
				d1 = 0.3D;

				predicate = (candidatePos) -> level.getFluidState(candidatePos.below()).isSourceOfType(Fluids.WATER);
			} else {
				return;
			}

			level.addParticle(ParticleTypes.HAPPY_VILLAGER, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
			var random = level.getRandom();

			for(int i = 0; i < data; ++i) {
				double d2 = random.nextGaussian() * 0.02D;
				double d3 = random.nextGaussian() * 0.02D;
				double d4 = random.nextGaussian() * 0.02D;
				double d5 = 0.5D - d0;
				double d6 = (double)pos.getX() + d5 + random.nextDouble() * d0 * 2.0D;
				double d7 = (double)pos.getY() + random.nextDouble() * d1;
				double d8 = (double)pos.getZ() + d5 + random.nextDouble() * d0 * 2.0D;

				var candidatePos = BlockPos.containing(d6, d7, d8);

				if(predicate.test(candidatePos)) {
					level.addParticle(ParticleTypes.HAPPY_VILLAGER, d6, d7, d8, d2, d3, d4);
				}
			}

			ci.cancel();
		}
	}
}
