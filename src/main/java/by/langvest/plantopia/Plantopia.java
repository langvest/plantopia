package by.langvest.plantopia;

import by.langvest.plantopia.adv.trigger.PlantopiaAdvancementTriggers;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaCompats;
import by.langvest.plantopia.block.entity.PlantopiaBlockEntities;
import by.langvest.plantopia.client.color.PlantopiaColors;
import by.langvest.plantopia.client.render.PlantopiaRenderTypes;
import by.langvest.plantopia.entity.PlantopiaEntities;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod(Plantopia.MOD_ID)
public final class Plantopia {
	public static final String MOD_ID = "plantopia";

	@SuppressWarnings("unused")
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public Plantopia(@NotNull FMLJavaModLoadingContext context) {
		IEventBus bus = context.getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);

		PlantopiaParticleTypes.setup(bus);
		PlantopiaCreativeModeTabs.setup(bus);
		PlantopiaBlocks.setup(bus);
		PlantopiaFeatureTypes.setup(bus);
		PlantopiaTreeDecoratorTypes.setup(bus);
		PlantopiaItems.setup(bus);
		PlantopiaBlockEntities.setup(bus);
		PlantopiaEntities.setup(bus);
		PlantopiaSoundEvents.setup(bus);
	}

	private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
		event.enqueueWork(PlantopiaCompats::setup);
		event.enqueueWork(PlantopiaAdvancementTriggers::setup);
	}

	private void clientSetup(final @NotNull FMLClientSetupEvent event) {
		event.enqueueWork(PlantopiaRenderTypes::setup);
	}

	private void loadComplete(final @NotNull FMLLoadCompleteEvent event) {
		event.enqueueWork(PlantopiaColors::setup);
	}
}