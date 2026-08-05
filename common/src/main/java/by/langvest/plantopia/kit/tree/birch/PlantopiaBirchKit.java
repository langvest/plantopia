package by.langvest.plantopia.kit.tree.birch;

import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeKit;
import by.langvest.plantopia.kit.special.PlantopiaTreePlantKit;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaBirchKit extends PlantopiaAbstractTreeKit {
    public final PlantopiaTreePlantKit plant;

    public PlantopiaBirchKit(
        String baseName,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, config);

        this.plant = new PlantopiaTreePlantKit(baseName, config);
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.balksFromLogs(plant.balk.get(), Blocks.BIRCH_LOG);
        bridge.balksFromLogs(plant.strippedBalk.get(), Blocks.STRIPPED_BIRCH_LOG);
    }
}
