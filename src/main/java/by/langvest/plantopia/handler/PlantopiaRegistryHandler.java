package by.langvest.plantopia.handler;

import by.langvest.toolkit.event.RegistryEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaRegistryHandler {
	@SubscribeEvent
	public static void onRegister(@NotNull RegisterEvent superEvent) {
		var targetRegistryKey = superEvent.getRegistryKey();
		var globalEventEmitter = EventEmitter.getDefaultInstance();

		var registryEvent = new RegistryEvent((registryKey, identifier, supplier) -> {
			if(!registryKey.equals(targetRegistryKey)) return;

			superEvent.register(registryKey, identifier, supplier);
		});

		globalEventEmitter.emit(registryEvent);
	}
}
