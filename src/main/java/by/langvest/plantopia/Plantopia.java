package by.langvest.plantopia;

import by.langvest.plantopia.client.color.PlantopiaBlockColors;
import by.langvest.plantopia.client.color.PlantopiaItemColors;
import by.langvest.toolkit.forge.ForgePlatform;
import by.langvest.plantopia.adv.trigger.PlantopiaAdvancementTriggers;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaCompats;
import by.langvest.plantopia.block.entity.PlantopiaBlockEntities;
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
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod(Plantopia.MOD_ID)
public final class Plantopia {
	public static final String MOD_ID = "plantopia";
	private static Platform platform;

	public Plantopia(@NotNull FMLJavaModLoadingContext context) {
		var platform = new ForgePlatform(Plantopia.MOD_ID, context);

		Plantopia.init(platform);

		IEventBus bus = context.getModEventBus();
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
	}

	public static void init(Platform platform) {
		var globalEventEmitter = EventEmitter.getDefaultInstance();

		// Setup platform
		injectPlatform(platform);

		// Setup registries
		globalEventEmitter.subscribe(PlantopiaParticleTypes::setup);
		globalEventEmitter.subscribe(PlantopiaCreativeModeTabs::setup);
		globalEventEmitter.subscribe(PlantopiaBlocks::setup);
		globalEventEmitter.subscribe(PlantopiaFeatureTypes::setup);
		globalEventEmitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
		globalEventEmitter.subscribe(PlantopiaItems::setup);
		globalEventEmitter.subscribe(PlantopiaEntities::setup);
		globalEventEmitter.subscribe(PlantopiaBlockEntities::setup);
		globalEventEmitter.subscribe(PlantopiaSoundEvents::setup);

		// Setup colors
		globalEventEmitter.subscribe(PlantopiaBlockColors::setup);
		globalEventEmitter.subscribe(PlantopiaItemColors::setup);
	}

	public static Platform getPlatform() {
		return Objects.requireNonNull(platform);
	}

	private static void injectPlatform(Platform platform) {
		Plantopia.platform = platform;
	}

	private void commonSetup(final @NotNull FMLCommonSetupEvent event) {
		event.enqueueWork(PlantopiaCompats::setup);
		event.enqueueWork(PlantopiaAdvancementTriggers::setup);
	}

	private void clientSetup(final @NotNull FMLClientSetupEvent event) {
		event.enqueueWork(PlantopiaRenderTypes::setup);
	}
}