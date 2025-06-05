package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.entity.PlantopiaEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaEntityRendererRegisterHandler {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event) {
		event.registerEntityRenderer(PlantopiaEntities.COBBLESTONE_SHARD.get(), ThrownItemRenderer::new);
	}
}
