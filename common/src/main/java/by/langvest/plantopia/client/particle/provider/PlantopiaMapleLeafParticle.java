package by.langvest.plantopia.client.particle.provider;

import by.langvest.plantopia.particle.PlantopiaParticleGroups;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

/**
 * @see net.minecraft.client.particle.CherryParticle
 */
@ParametersAreNonnullByDefault
public class PlantopiaMapleLeafParticle extends TextureSheetParticle {
    private static final int SPRITE_COUNT = 8;
    private static final float ACCELERATION_SCALE = 0.0025F;
    private static final int INITIAL_LIFETIME = 300;
    private static final float GRAVITY = 7.5E-4F;
    private static final float MIN_SIZE = 0.11F;
    private static final float MAX_SIZE = 0.1265F;
    private static final float ROTATION_SPEED_DIVISOR = 20.0F;
    private static final float WIND_CURVE_SCALE = 60.0F;
    private static final double WIND_POWER = 1.25D;
    private static final double WIND_MAGNITUDE = 2.0D;
    private static final int REMOVAL_LIFETIME_THRESHOLD = 299;
    private static final double INITIAL_ROT_SPEED = 30.0D;
    private static final double INITIAL_SPIN_ACCEL = 5.0D;

    private float rotSpeed;
    private final float particleRandom;
    private final float spinAcceleration;

    PlantopiaMapleLeafParticle(ClientLevel level, SpriteSet sprite, double x, double y, double z) {
        super(level, x, y, z);
        this.setSprite(sprite.get(random.nextInt(SPRITE_COUNT), SPRITE_COUNT));
        this.rotSpeed = (float) Math.toRadians(random.nextBoolean() ? -INITIAL_ROT_SPEED : INITIAL_ROT_SPEED);
        this.particleRandom = random.nextFloat();
        this.spinAcceleration = (float) Math.toRadians(random.nextBoolean() ? -INITIAL_SPIN_ACCEL : INITIAL_SPIN_ACCEL);
        this.lifetime = INITIAL_LIFETIME;
        this.gravity = GRAVITY;
        float size = (random.nextBoolean() ? MIN_SIZE : MAX_SIZE);
        this.quadSize = size;
        this.setSize(size, size);
        this.friction = 1.0F;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (lifetime-- <= 0) {
            remove();
        }

        if (!removed) {
            float particleAge = INITIAL_LIFETIME - lifetime;
            float lifetimeProgress = Math.min(particleAge / INITIAL_LIFETIME, 1.0F);
            double windEffectX = Math.cos(Math.toRadians(particleRandom * WIND_CURVE_SCALE)) * WIND_MAGNITUDE * Math.pow(lifetimeProgress, WIND_POWER);
            double windEffectZ = Math.sin(Math.toRadians(particleRandom * WIND_CURVE_SCALE)) * WIND_MAGNITUDE * Math.pow(lifetimeProgress, WIND_POWER);
            xd += windEffectX * ACCELERATION_SCALE;
            zd += windEffectZ * ACCELERATION_SCALE;
            yd -= gravity;
            rotSpeed += spinAcceleration / ROTATION_SPEED_DIVISOR;
            oRoll = roll;
            roll += rotSpeed / ROTATION_SPEED_DIVISOR;
            move(xd, yd, zd);

            if (onGround || lifetime < REMOVAL_LIFETIME_THRESHOLD && (xd == 0.0D || zd == 0.0D)) {
                remove();
            }

            if (!removed) {
                xd *= friction;
                yd *= friction;
                zd *= friction;
            }
        }
    }

    @Override
    public @NotNull Optional<ParticleGroup> getParticleGroup() {
        return Optional.of(PlantopiaParticleGroups.MAPLE_LEAF);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
