package by.langvest.plantopia.handler;

import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaRegisterHandler {
	@SubscribeEvent
	public static void onRegister(@NotNull net.minecraftforge.registries.RegisterEvent event) {
		var globalEmitter = EventEmitter.getDefaultInstance();

		globalEmitter.emit(new RegisterEvent(event::register));
	}
}
