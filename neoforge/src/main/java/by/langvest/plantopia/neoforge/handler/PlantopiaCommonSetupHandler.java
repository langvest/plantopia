package by.langvest.plantopia.neoforge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PlantopiaCommonSetupHandler {
    @SubscribeEvent
    public static void commonSetup(@NotNull FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            var globalEventEmitter = EventEmitter.getDefaultInstance();

            globalEventEmitter.emit(new LifecycleEvent.CommonSetupEvent());
        });
    }
}
