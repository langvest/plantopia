package by.langvest.plantopia.forge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.event.PlantopiaTerraBlenderEvent;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PlantopiaCommonSetupHandler {
    @SubscribeEvent
    public static void commonSetup(@NotNull FMLCommonSetupEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var localEventEmitter = Plantopia.getPlatform().getEventEmitter();

        event.enqueueWork(() -> {
            globalEventEmitter.emit(new LifecycleEvent.CommonSetupEvent());

            localEventEmitter.emit(new PlantopiaTerraBlenderEvent.Region() {
                @Override
                public void register(terrablender.api.Region region) {
                    Regions.register(region);
                }
            });

            localEventEmitter.emit(new PlantopiaTerraBlenderEvent.SurfaceRules() {
                @Override
                public void register(SurfaceRuleManager.RuleCategory category, net.minecraft.world.level.levelgen.SurfaceRules.RuleSource rules) {
                    SurfaceRuleManager.addSurfaceRules(category, Plantopia.MOD_ID, rules);
                }
            });
        });
    }
}
