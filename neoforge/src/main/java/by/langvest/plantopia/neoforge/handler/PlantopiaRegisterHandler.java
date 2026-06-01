package by.langvest.plantopia.neoforge.handler;

import by.langvest.toolkit.platform.EventEmitter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PlantopiaRegisterHandler {
    @SubscribeEvent
    public static void handleRegister(@NotNull RegisterEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new by.langvest.toolkit.event.RegisterEvent() {
            @Override
            public <T> void register(ResourceKey<Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier) {
                event.register(registryKey, identifier, supplier);
            }
        });
    }
}
