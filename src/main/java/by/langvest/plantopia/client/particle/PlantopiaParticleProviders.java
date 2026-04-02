package by.langvest.plantopia.client.particle;

import by.langvest.plantopia.client.particle.provider.PlantopiaBreakingItemParticle;
import by.langvest.plantopia.client.particle.provider.PlantopiaFluffyDandelionSeedParticle;
import by.langvest.plantopia.client.particle.provider.PlantopiaQuicksandParticle;
import by.langvest.plantopia.client.particle.provider.PlantopiaWitchyToadstoolSporeParticle;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.toolkit.event.client.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;

public class PlantopiaParticleProviders {
    public static void setup(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PlantopiaParticleTypes.FLUFFY_DANDELION_SEED.get(), PlantopiaFluffyDandelionSeedParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.QUICKSAND.get(), PlantopiaQuicksandParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.WITCHY_TOADSTOOL_SPORE.get(), PlantopiaWitchyToadstoolSporeParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.BREAKING_ITEM.get(), PlantopiaBreakingItemParticle.PlantopiaProvider::new);
    }
}
