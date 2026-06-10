package by.langvest.plantopia.fabric.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.event.PlantopiaTerraBlenderEvent;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class PlantopiaTerraBlenderSetupHandler {
    public static void setup() {
        var localEventEmitter = Plantopia.getPlatform().getEventEmitter();

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
    }
}
