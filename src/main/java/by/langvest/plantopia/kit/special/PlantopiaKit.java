package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.Plantopia;

public abstract class PlantopiaKit {
    protected PlantopiaKit() {
        var platform = Plantopia.getPlatform();
        var workScheduler = platform.getWorkScheduler();

        if (platform.isDatagen()) {
            workScheduler.enqueueWork("block_tag_datagen", this::addBlockTags);
            workScheduler.enqueueWork("item_tag_datagen", this::addItemTags);
            workScheduler.enqueueWork("recipe_datagen", this::addRecipes);
            workScheduler.enqueueWork("block_loot_table_datagen", this::addBlockLootTables);
        }

        if (platform.isClient()) {
            workScheduler.enqueueWork("client_setup", this::onClientSetup);
        }
    }

    protected void addBlockTags() {}

    protected void addItemTags() {}

    protected void addRecipes() {}

    protected void addBlockLootTables() {}

    protected void onClientSetup() {}
}
