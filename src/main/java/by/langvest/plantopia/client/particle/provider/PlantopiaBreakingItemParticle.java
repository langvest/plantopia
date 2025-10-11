package by.langvest.plantopia.client.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class PlantopiaBreakingItemParticle extends BreakingItemParticle {
	protected PlantopiaBreakingItemParticle(ClientLevel level, double x, double y, double z, ItemStack itemStack) {
		super(level, x, y, z, itemStack);
	}

	@OnlyIn(Dist.CLIENT)
	public static class PlantopiaProvider implements ParticleProvider<ItemParticleOption> {
		public PlantopiaProvider(@SuppressWarnings("unused") SpriteSet sprite) {}

		public Particle createParticle(@NotNull ItemParticleOption type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new PlantopiaBreakingItemParticle(level, x, y, z, type.getItem());
		}
	}
}
