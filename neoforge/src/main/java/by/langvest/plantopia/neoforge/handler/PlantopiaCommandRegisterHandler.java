package by.langvest.plantopia.neoforge.handler;

import by.langvest.toolkit.platform.EventEmitter;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlantopiaCommandRegisterHandler {
    @SubscribeEvent
    public static void handleRegister(@NotNull RegisterCommandsEvent event) {
        var globalEmitter = EventEmitter.getDefaultInstance();
        var dispatcher = event.getDispatcher();

        globalEmitter.emit(new by.langvest.toolkit.event.RegisterCommandsEvent() {
            @Override
            public void register(LiteralArgumentBuilder<CommandSourceStack> command) {
                dispatcher.register(command);
            }
        });
    }
}
