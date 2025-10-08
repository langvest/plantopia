package by.langvest.plantopia.handler;

import by.langvest.toolkit.event.RegisterColorsEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaRegisterColorsHandler {
	@SubscribeEvent
	public static void onRegister(@NotNull RegisterColorHandlersEvent.Block event) {
		var globalEventEmitter = EventEmitter.getDefaultInstance();
		var blockColors = event.getBlockColors();

		globalEventEmitter.emit(new RegisterColorsEvent.Block(blockColors));
	}

	@SubscribeEvent
	public static void onRegister(@NotNull RegisterColorHandlersEvent.Item event) {
		var globalEventEmitter = EventEmitter.getDefaultInstance();
		var itemColors = event.getItemColors();
		var blockColors = event.getBlockColors();

		globalEventEmitter.emit(new RegisterColorsEvent.Item(itemColors, blockColors));
	}
}
