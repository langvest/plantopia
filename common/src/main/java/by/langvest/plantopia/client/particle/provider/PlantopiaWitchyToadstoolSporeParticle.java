package by.langvest.plantopia.client.particle.provider;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaWitchyToadstoolSporeParticle extends TextureSheetParticle {
    protected float xFactor;
    protected float zFactor;
    protected float quadSizeDecrement;

    PlantopiaWitchyToadstoolSporeParticle(ClientLevel level, SpriteSet sprite, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.pickSprite(sprite);
        this.xFactor = (float) Mth.nextDouble(random, -0.075D, 0.075D);
        this.zFactor = (float) Mth.nextDouble(random, -0.075D, 0.075D);
        this.quadSize = 0.095F;
        this.lifetime = 6 + (int) Math.floor(random.nextDouble() * 8);
        this.hasPhysics = false;
        this.gravity = -0.25F;
        this.xd = xSpeed + this.xFactor * 3.0F;
        this.yd = ySpeed + 0.1F;
        this.zd = zSpeed + this.zFactor * 3.0F;
        this.quadSizeDecrement = this.quadSize * 0.65F / this.lifetime;
    }

    @Override
    public void tick() {
        if (gravity <= 0) {
            gravity += 0.1F;
        }

        if (age > 1 && quadSize >= quadSizeDecrement) {
            quadSize -= quadSizeDecrement;
        }

        xd -= xFactor;
        zd -= zFactor;

        if (random.nextInt(6) == 0 || Math.abs(xd) >= 0.2F) {
            xFactor *= -1;
            xd *= xd > 0 ? 0.15F : -0.15F;
        }

        if (random.nextInt(6) == 0 || Math.abs(zd) >= 0.2F) {
            zFactor *= -1;
            zd *= zd > 0 ? 0.15F : -0.15F;
        }

        super.tick();
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getLightColor(float partialTick) {
        var pos = BlockPos.containing(x, y, z);

        if (!level.hasChunkAt(pos)) return 0;

        var state = level.getBlockState(pos);

        if (state.emissiveRendering(level, pos)) {
            return 15728880;
        }

        int minLightLevel = 3;
        int skyBrightness = Math.max(level.getBrightness(LightLayer.SKY, pos), minLightLevel);
        int blockBrightness = Math.max(level.getBrightness(LightLayer.BLOCK, pos), minLightLevel);
        int lightEmission = state.getLightEmission();

        if (blockBrightness < lightEmission) {
            blockBrightness = lightEmission;
        }

        return skyBrightness << 20 | blockBrightness << 4;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PlantopiaWitchyToadstoolSporeParticle(level, sprite, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
