package by.langvest.plantopia.fabric;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.fabric.handler.PlantopiaClientSetupHandler;
import by.langvest.plantopia.fabric.handler.PlantopiaCommonSetupHandler;
import by.langvest.plantopia.fabric.handler.PlantopiaTerraBlenderSetupHandler;
import by.langvest.toolkit.fabric.FabricPlatform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import terrablender.api.TerraBlenderApi;

public class PlantopiaFabric implements ModInitializer, ClientModInitializer, TerraBlenderApi {
    static {
        var platform = new FabricPlatform(Plantopia.MOD_ID);
        Plantopia.init(platform);
    }

    @Override
    public void onInitialize() {
        PlantopiaCommonSetupHandler.setup();
    }

    @Override
    public void onInitializeClient() {
        PlantopiaClientSetupHandler.setup();
    }

    @Override
    public void onTerraBlenderInitialized() {
        PlantopiaTerraBlenderSetupHandler.setup();
    }
}
