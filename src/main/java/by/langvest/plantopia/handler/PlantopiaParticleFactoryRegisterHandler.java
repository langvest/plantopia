package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.particle.special.PlantopiaBreakingItemParticle;
import by.langvest.plantopia.particle.special.PlantopiaFluffyDandelionSeedParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaParticleFactoryRegisterHandler {
	@SubscribeEvent
	public static void registerParticleFactories(@NotNull RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(PlantopiaParticleTypes.FLUFFY_DANDELION_SEED.get(), PlantopiaFluffyDandelionSeedParticle.Provider::new);
		event.registerSpriteSet(PlantopiaParticleTypes.BREAKING_ITEM.get(), PlantopiaBreakingItemParticle.PlantopiaProvider::new);
	}
}
