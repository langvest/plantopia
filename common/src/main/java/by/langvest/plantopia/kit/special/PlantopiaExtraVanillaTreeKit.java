package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class PlantopiaExtraVanillaTreeKit extends PlantopiaKit {
    public final PlantopiaTreePlantKit plant;

    protected final Supplier<Block> log;
    protected final Supplier<Block> strippedLog;

    public PlantopiaExtraVanillaTreeKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> strippedLog,
        PlantopiaTreeKitConfiguration config
    ) {
        this.log = log;
        this.strippedLog = strippedLog;
        this.plant = new PlantopiaTreePlantKit(baseName, config);
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.balksFromLogs(plant.balk.get(), log.get());
        bridge.balksFromLogs(plant.strippedBalk.get(), strippedLog.get());
    }
}
