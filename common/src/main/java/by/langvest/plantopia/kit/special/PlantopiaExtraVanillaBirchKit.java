package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaExtraVanillaBirchKit extends PlantopiaExtraVanillaTreeKit {
    public PlantopiaExtraVanillaBirchKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> strippedLog,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, log, strippedLog, config);
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);
    }
}
