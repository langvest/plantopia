package by.langvest.plantopia;

import by.langvest.toolkit.forge.ForgePlatform;
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
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod(Plantopia.MOD_ID)
public final class Plantopia {
	public static final String MOD_ID = "plantopia";
	public static Platform platform;

	public static Platform getPlatform() {
		return Objects.requireNonNull(platform);
	}

	public Plantopia(@NotNull FMLJavaModLoadingContext context) {
		platform = new ForgePlatform(MOD_ID, context);

		IEventBus bus = context.getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		bus.addListener(this::loadComplete);

		var globalEventEmitter = EventEmitter.getDefaultInstance();

		globalEventEmitter.subscribe(PlantopiaParticleTypes::setup);
		globalEventEmitter.subscribe(PlantopiaCreativeModeTabs::setup);
		globalEventEmitter.subscribe(PlantopiaBlocks::setup);
		globalEventEmitter.subscribe(PlantopiaFeatureTypes::setup);
		globalEventEmitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
		globalEventEmitter.subscribe(PlantopiaItems::setup);
		globalEventEmitter.subscribe(PlantopiaEntities::setup);
		globalEventEmitter.subscribe(PlantopiaBlockEntities::setup);
		globalEventEmitter.subscribe(PlantopiaSoundEvents::setup);
	}

	private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
		// тоже потом переписать на eventEmitter
		event.enqueueWork(PlantopiaCompats::setup);
		event.enqueueWork(PlantopiaAdvancementTriggers::setup);
	}

	private void clientSetup(final @NotNull FMLClientSetupEvent event) {
		// тоже потом переписать на eventEmitter
		event.enqueueWork(PlantopiaRenderTypes::setup);
	}

	private void loadComplete(final @NotNull FMLLoadCompleteEvent event) {
		// тоже потом переписать на eventEmitter
		event.enqueueWork(PlantopiaColors::setup);
	}
}