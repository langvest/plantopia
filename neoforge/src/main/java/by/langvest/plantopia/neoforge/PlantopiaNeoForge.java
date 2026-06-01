package by.langvest.plantopia.neoforge;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.neoforge.NeoForgePlatform;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Plantopia.MOD_ID)
public final class PlantopiaNeoForge {
    public PlantopiaNeoForge() {
        var platform = new NeoForgePlatform(Plantopia.MOD_ID, FMLJavaModLoadingContext.get());
        Plantopia.init(platform);
    }
}
