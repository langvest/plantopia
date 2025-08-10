package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.entity.PlantopiaBlockEntities;
import by.langvest.plantopia.block.entity.render.PlantopiaCobblestoneShardPetBlockEntityRenderer;
import by.langvest.plantopia.block.entity.render.PlantopiaCoveredSnowdropBlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaBlockEntityRendererRegisterHandler {
	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.@NotNull RegisterRenderers event) {
		event.registerBlockEntityRenderer(PlantopiaBlockEntities.COBBLESTONE_SHARD_PET.get(), PlantopiaCobblestoneShardPetBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(PlantopiaBlockEntities.COVERED_SNOWDROP.get(), PlantopiaCoveredSnowdropBlockEntityRenderer::new);
	}
}
