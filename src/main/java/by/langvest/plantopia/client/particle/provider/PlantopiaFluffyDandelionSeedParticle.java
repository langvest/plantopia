package by.langvest.plantopia.client.particle.provider;

import by.langvest.plantopia.particle.PlantopiaParticleGroups;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlantopiaFluffyDandelionSeedParticle extends TextureSheetParticle {
	protected int disappearanceTime = 20;

	PlantopiaFluffyDandelionSeedParticle(ClientLevel level, SpriteSet sprite, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.pickSprite(sprite);
		this.quadSize = 0.15F;
		this.lifetime = 100 + (int)Math.floor(Math.random() * 50);
		this.hasPhysics = true;
		this.gravity = -0.0188F;
		this.setSize(quadSize * 0.85F, 0.11F);
		double xzFactor = 1.8D;
		double yFactor = 1.3D;
		this.xd = xSpeed + (Math.random() * xzFactor - xzFactor / 2) * 0.4F;
		this.yd = ySpeed + (Math.random() * yFactor - yFactor / 3) * 0.4F;
		this.zd = zSpeed + (Math.random() * xzFactor - xzFactor / 2) * 0.4F;
		double d0 = (Math.random() + Math.random() + 1.0D) * 0.15F;
		double d1 = Math.sqrt(xd * xd + yd * yd + zd * zd);
		this.xd = xd / d1 * d0 * 0.244F;
		this.yd = yd / d1 * d0 * 0.228F + 0.038F;
		this.zd = zd / d1 * d0 * 0.244F;
	}

	@Override
	public void tick() {
		if(age >= lifetime - disappearanceTime) {
			setAlpha(alpha - 1.F / disappearanceTime);
		}

		double x_before = x;
		double y_before = y;
		double z_before = z;

		super.tick();

		boolean collided_x = x_before == x;
		boolean collided_z = z_before == z;
		boolean collided_y = y_before == y;

		if(onGround || collided_x || collided_z || collided_y) {
			xd = 0.0D;
			yd = 0.0D;
			zd = 0.0D;
			gravity = 0.0F;
		}
	}

	@Override
	public @NotNull Optional<ParticleGroup> getParticleGroup() {
		return Optional.of(PlantopiaParticleGroups.FLUFFY_DANDELION_SEED);
	}

	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new PlantopiaFluffyDandelionSeedParticle(level, sprite, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}