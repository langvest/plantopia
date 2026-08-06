package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class PlantopiaExtraVanillaTreeKit extends PlantopiaKit {
    public final PlantopiaTreePlantKit plant;

    protected final Supplier<Block> planks;
    protected final Supplier<Block> log;
    protected final Supplier<Block> strippedLog;

    public PlantopiaExtraVanillaTreeKit(
        String baseName,
        Supplier<Block> planks,
        Supplier<Block> log,
        Supplier<Block> wood,
        Supplier<Block> strippedLog,
        Supplier<Block> strippedWood,
        PlantopiaTreeKitConfiguration config
    ) {
        this.planks = planks;
        this.log = log;
        this.strippedLog = strippedLog;
        this.plant = new PlantopiaTreePlantKit(baseName, wood, strippedWood, config);
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.planksFromBalks(planks.get(), plant.balksItemTag);
        bridge.balksFromLogs(plant.balk.get(), log.get());
        bridge.balksFromLogs(plant.strippedBalk.get(), strippedLog.get());
    }
}
