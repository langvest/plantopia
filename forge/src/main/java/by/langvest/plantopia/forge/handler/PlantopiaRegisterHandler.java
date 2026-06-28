package by.langvest.plantopia.forge.handler;

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
public final class PlantopiaRegisterHandler {
    @SubscribeEvent
    public static void handleRegister(@NotNull net.minecraftforge.registries.RegisterEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.emit(new RegisterEvent() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier) {
                event.register(registryKey, identifier, supplier);
            }
        });
    }
}
