package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent.*;

public abstract class PlantopiaKit {
    protected PlantopiaKit() {
        var platform = Plantopia.getPlatform();
        var eventEmitter = platform.getEventEmitter();
        var workScheduler = platform.getWorkScheduler();

        if (platform.isDatagen()) {
            workScheduler.enqueueWork("block_tag_datagen", () -> eventEmitter.emit(BlockTagEvent.create(this::addBlockTags)));
            workScheduler.enqueueWork("item_tag_datagen", () -> eventEmitter.emit(ItemTagEvent.create(this::addItemTags)));
            workScheduler.enqueueWork("recipe_datagen", () -> eventEmitter.emit(RecipeEvent.create(this::addRecipes)));
            workScheduler.enqueueWork("block_loot_table_datagen", () -> eventEmitter.emit(BlockLootTableEvent.create(this::addBlockLootTables)));
        }

        if (platform.isClient()) {
            workScheduler.enqueueWork("client_setup", this::onClientSetup);
        }
    }

    protected void addBlockTags(BlockTagEvent.Bridge bridge) {}

    protected void addItemTags(ItemTagEvent.Bridge bridge) {}

    protected void addRecipes(RecipeEvent.Bridge bridge) {}

    protected void addBlockLootTables(BlockLootTableEvent.Bridge bridge) {}

    protected void onClientSetup() {}
}
