package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.datagen.adv.PlantopiaAdvancementProvider;
import by.langvest.plantopia.datagen.lang.PlantopiaLanguageProvider;
import by.langvest.plantopia.datagen.registry.PlantopiaRegistryProvider;
import by.langvest.plantopia.datagen.loot.PlantopiaLootTableProvider;
import by.langvest.plantopia.datagen.model.PlantopiaBlockStateProvider;
import by.langvest.plantopia.datagen.model.PlantopiaItemModelProvider;
import by.langvest.plantopia.datagen.recipe.PlantopiaRecipeProvider;
import by.langvest.plantopia.datagen.sound.PlantopiaSoundProvider;
import by.langvest.plantopia.datagen.tag.PlantopiaBiomeTagProvider;
import by.langvest.plantopia.datagen.tag.PlantopiaBlockTagProvider;
import by.langvest.plantopia.datagen.tag.PlantopiaEntityTypeTagProvider;
import by.langvest.plantopia.datagen.tag.PlantopiaItemTagProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlantopiaDataGenerationHandler {
	@SubscribeEvent
	public static void gatherData(@NotNull GatherDataEvent event) {
		var generator = event.getGenerator();
		var existingFileHelper = event.getExistingFileHelper();
		var output = generator.getPackOutput();
		
		var registryProvider = new PlantopiaRegistryProvider(output, event.getLookupProvider());
		generator.addProvider(event.includeServer(), registryProvider);
		
		var registryLookup = registryProvider.getRegistryProvider();

		generator.addProvider(event.includeServer(), new PlantopiaRecipeProvider(output));
		generator.addProvider(event.includeServer(), new PlantopiaLootTableProvider(output));
		generator.addProvider(event.includeServer(), new PlantopiaEntityTypeTagProvider(output, registryLookup, existingFileHelper));
		generator.addProvider(event.includeServer(), new PlantopiaAdvancementProvider(output, registryLookup, existingFileHelper));
		generator.addProvider(event.includeServer(), new PlantopiaBiomeTagProvider(output, registryLookup, existingFileHelper));
		generator.addProvider(event.includeServer(), new PlantopiaBlockTagProvider(output, registryLookup, existingFileHelper));
		generator.addProvider(event.includeServer(), new PlantopiaItemTagProvider(output, registryLookup, existingFileHelper));

		generator.addProvider(event.includeClient(), new PlantopiaBlockStateProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new PlantopiaItemModelProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new PlantopiaSoundProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new PlantopiaLanguageProvider(output));
	}
}
