package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaExtraVanillaBirchTreeKit extends PlantopiaExtraVanillaTreeKit {
    public PlantopiaExtraVanillaBirchTreeKit(
        String baseName,
        Supplier<Block> planks,
        Supplier<Block> log,
        Supplier<Block> wood,
        Supplier<Block> strippedLog,
        Supplier<Block> strippedWood,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, planks, log, wood, strippedLog, strippedWood, config);
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);
    }
}
