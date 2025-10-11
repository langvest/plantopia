package by.langvest.plantopia.client.particle.provider;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SnowflakeParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.*;

public class PlantopiaQuicksandParticle extends SnowflakeParticle {
	protected PlantopiaQuicksandParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprite) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet sprite) {
			this.sprite = sprite;
		}

		public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			var quicksandParticle = new PlantopiaQuicksandParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
			var dustColor = PlantopiaQuicksandBlock.DUST_COLOR;
			quicksandParticle.setColor(red(dustColor), green(dustColor), blue(dustColor));
			return quicksandParticle;
		}
	}
}
