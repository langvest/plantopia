package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.RegisterRenderLayersEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PlantopiaClientSetupHandler {
	@SubscribeEvent
	@SuppressWarnings("removal")
	public static void clientSetup(@NotNull FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			var globalEventEmitter = EventEmitter.getDefaultInstance();

			globalEventEmitter.emit(new LifecycleEvent.ClientSetup());
			globalEventEmitter.emit(new RegisterRenderLayersEvent.Block(ItemBlockRenderTypes::setRenderLayer));
		});
	}
}
