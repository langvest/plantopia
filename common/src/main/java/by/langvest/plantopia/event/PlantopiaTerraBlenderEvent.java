package by.langvest.plantopia.event;

import by.langvest.toolkit.event.Event;
import by.langvest.toolkit.registry.Registry;
import org.jetbrains.annotations.NotNull;
import terrablender.api.SurfaceRuleManager;

public abstract class PlantopiaTerraBlenderEvent extends Event {
    public abstract static class Region extends PlantopiaTerraBlenderEvent {
        public abstract void register(terrablender.api.Region region);

        public void registerAll(@NotNull Registry<terrablender.api.Region> registry) {
            for (var registryObject : registry) {
                register(registryObject.get());
            }
        }
    }

    public abstract static class SurfaceRules extends PlantopiaTerraBlenderEvent {
        public abstract void register(SurfaceRuleManager.RuleCategory category, net.minecraft.world.level.levelgen.SurfaceRules.RuleSource rules);
    }
}
