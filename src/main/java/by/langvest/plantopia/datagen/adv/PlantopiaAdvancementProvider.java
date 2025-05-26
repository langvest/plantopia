package by.langvest.plantopia.datagen.adv;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlantopiaAdvancementProvider extends ForgeAdvancementProvider {
	public PlantopiaAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, existingFileHelper, List.of(
			new PlantopiaAdvancements()
		));
	}
}