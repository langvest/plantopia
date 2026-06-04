package by.langvest.plantopia.fabric;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.fabric.handler.PlantopiaClientSetupHandler;
import by.langvest.plantopia.fabric.handler.PlantopiaCommonSetupHandler;
import by.langvest.toolkit.fabric.FabricPlatform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class PlantopiaFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitialize() {
        var platform = new FabricPlatform(Plantopia.MOD_ID);
        Plantopia.init(platform);
        PlantopiaCommonSetupHandler.init(platform);
    }

    @Override
    public void onInitializeClient() {
        PlantopiaClientSetupHandler.init();
    }
}
