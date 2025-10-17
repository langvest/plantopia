package by.langvest.plantopia.handler;

import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaRegisterHandler {
	@SubscribeEvent
	public static void handleRegister(@NotNull net.minecraftforge.registries.RegisterEvent event) {
		var globalEmitter = EventEmitter.getDefaultInstance();

		globalEmitter.emit(new RegisterEvent() {
			@Override
			public <T> void register(ResourceKey<Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier) {
				event.register(registryKey, identifier, supplier);
			}
		});
	}
}
