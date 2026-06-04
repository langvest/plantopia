package by.langvest.plantopia.neoforge;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.neoforge.NeoForgePlatform;
import net.neoforged.fml.common.Mod;

@Mod(Plantopia.MOD_ID)
public final class PlantopiaNeoForge {
    public PlantopiaNeoForge() {
        var platform = new NeoForgePlatform(Plantopia.MOD_ID);
        Plantopia.init(platform);
    }
}
