package by.langvest.plantopia.handler;

import by.langvest.toolkit.event.RegisterCommandsEvent;
import by.langvest.toolkit.platform.EventEmitter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlantopiaCommandRegisterHandler {
	@SubscribeEvent
	public static void handleRegister(@NotNull net.minecraftforge.event.RegisterCommandsEvent event) {
		var globalEmitter = EventEmitter.getDefaultInstance();
		var dispatcher = event.getDispatcher();

		globalEmitter.emit(new RegisterCommandsEvent() {
			@Override
			public void register(LiteralArgumentBuilder<CommandSourceStack> command) {
				dispatcher.register(command);
			}
		});
	}
}
