package by.langvest.toolkit.event.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public abstract class RegisterParticleProvidersEvent extends ClientEvent {
    public abstract <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleProvider<T> provider);

    public abstract <T extends ParticleOptions> void registerSprite(ParticleType<T> particleType, ParticleProvider.Sprite<T> sprite);

    public abstract <T extends ParticleOptions> void registerSpriteSet(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> registration);
}
