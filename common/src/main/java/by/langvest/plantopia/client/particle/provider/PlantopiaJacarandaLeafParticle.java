package by.langvest.plantopia.client.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @see CherryParticle
 */
@ParametersAreNonnullByDefault
public class PlantopiaJacarandaLeafParticle extends CherryParticle {
    protected PlantopiaJacarandaLeafParticle(ClientLevel level, double x, double y, double z, SpriteSet sprite) {
        super(level, x, y, z, sprite);
    }

    @Contract(pure = true)
    public static <T extends ParticleOptions> @NotNull ParticleProvider<T> createParticle(SpriteSet sprite) {
        return (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> new PlantopiaJacarandaLeafParticle(level, x, y, z, sprite);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PlantopiaMapleLeafParticle(level, sprite, x, y, z);
        }
    }
}
