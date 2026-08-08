package by.langvest.plantopia.client.particle.special;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaBreakingItemParticle extends BreakingItemParticle {
    protected PlantopiaBreakingItemParticle(ClientLevel level, double x, double y, double z, ItemStack itemStack) {
        super(level, x, y, z, itemStack);
    }

    public static class Provider implements ParticleProvider<ItemParticleOption> {
        public Provider(@SuppressWarnings("unused") SpriteSet sprite) {
        }

        public Particle createParticle(ItemParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PlantopiaBreakingItemParticle(level, x, y, z, type.getItem());
        }
    }
}
