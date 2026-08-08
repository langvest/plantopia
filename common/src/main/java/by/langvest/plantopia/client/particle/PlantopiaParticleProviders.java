package by.langvest.plantopia.client.particle;

import by.langvest.plantopia.client.particle.special.*;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.toolkit.event.client.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;

public class PlantopiaParticleProviders {
    public static void setup(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(PlantopiaParticleTypes.FLUFFY_DANDELION_SEED.get(), PlantopiaFluffyDandelionSeedParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.QUICKSAND.get(), PlantopiaQuicksandParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.WITCHY_TOADSTOOL_SPORE.get(), PlantopiaWitchyToadstoolSporeParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.BREAKING_ITEM.get(), PlantopiaBreakingItemParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.YELLOW_MAPLE_LEAVES.get(), PlantopiaMapleLeafParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.ORANGE_MAPLE_LEAVES.get(), PlantopiaMapleLeafParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.RED_MAPLE_LEAVES.get(), PlantopiaMapleLeafParticle.Provider::new);
        event.registerSpriteSet(PlantopiaParticleTypes.JACARANDA_LEAVES.get(), PlantopiaJacarandaLeafParticle::createParticle);
    }
}
