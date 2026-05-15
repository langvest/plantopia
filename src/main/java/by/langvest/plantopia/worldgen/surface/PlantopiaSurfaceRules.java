package by.langvest.plantopia.worldgen.surface;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.worldgen.surface.special.PlantopiaOverworldSurface;
import by.langvest.toolkit.event.LifecycleEvent;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;

/**
 * @see <a href="https://github.com/TheForsakenFurby/Surface-Rules-Guide-Minecraft-JE-1.18/blob/main/Guide.md">Surface Rules Guide</a>
 */
public class PlantopiaSurfaceRules {
    public static void setup(LifecycleEvent.CommonSetupEvent event) {
        var overworldSurface = new PlantopiaOverworldSurface();

        SurfaceRuleManager.addSurfaceRules(RuleCategory.OVERWORLD, Plantopia.MOD_ID, overworldSurface.makeRules());
    }
}
