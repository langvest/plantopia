package by.langvest.plantopia.neoforge.datagen.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class PlantopiaLootTableProvider extends LootTableProvider {
    public PlantopiaLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
            new LootTableProvider.SubProviderEntry(PlantopiaBlockLootTableSubProvider::new, LootContextParamSets.BLOCK)
        ));
    }
}
