package by.langvest.plantopia.forge;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.forge.ForgePlatform;
import net.minecraftforge.fml.common.Mod;

@Mod(Plantopia.MOD_ID)
public final class PlantopiaForge {
    public PlantopiaForge() {
        var platform = new ForgePlatform(Plantopia.MOD_ID);
        Plantopia.init(platform);
    }
}
