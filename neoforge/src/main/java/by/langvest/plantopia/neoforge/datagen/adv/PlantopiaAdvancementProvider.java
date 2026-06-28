package by.langvest.plantopia.neoforge.datagen.adv;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlantopiaAdvancementProvider extends AdvancementProvider {
    public PlantopiaAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup, ExistingFileHelper existingFileHelper) {
        super(output, registryLookup, existingFileHelper, List.of(
            new PlantopiaAdvancementSubProvider()
        ));
    }
}
