package by.langvest.plantopia;

import by.langvest.plantopia.client.color.PlantopiaBlockColors;
import by.langvest.plantopia.client.color.PlantopiaItemColors;
import by.langvest.toolkit.forge.ForgePlatform;
import by.langvest.plantopia.adv.trigger.PlantopiaAdvancementTriggers;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.compat.PlantopiaCompats;
import by.langvest.plantopia.block.entity.PlantopiaBlockEntities;
import by.langvest.plantopia.client.render.PlantopiaBlockRenderLayers;
import by.langvest.plantopia.entity.PlantopiaEntities;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod(Plantopia.MOD_ID)
public final class Plantopia {
	public static final String MOD_ID = "plantopia";
	private static Platform platform;

	public Plantopia(@NotNull FMLJavaModLoadingContext context) {
		Plantopia.init(new ForgePlatform(Plantopia.MOD_ID, context));
	}

	public static void init(Platform platform) {
		var globalEventEmitter = EventEmitter.getDefaultInstance();

		Plantopia.initPlatform(platform);
		Plantopia.initRegistries(globalEventEmitter);
		Plantopia.initCommon(globalEventEmitter);
		Plantopia.initClient(globalEventEmitter);
	}

	public static Platform getPlatform() {
		return Objects.requireNonNull(platform);
	}

	private static void initPlatform(Platform platform) {
		Plantopia.platform = platform;
	}

	private static void initRegistries(@NotNull EventEmitter eventEmitter) {
		eventEmitter.subscribe(PlantopiaParticleTypes::setup);
		eventEmitter.subscribe(PlantopiaCreativeModeTabs::setup);
		eventEmitter.subscribe(PlantopiaBlocks::setup);
		eventEmitter.subscribe(PlantopiaFeatureTypes::setup);
		eventEmitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
		eventEmitter.subscribe(PlantopiaItems::setup);
		eventEmitter.subscribe(PlantopiaEntities::setup);
		eventEmitter.subscribe(PlantopiaBlockEntities::setup);
		eventEmitter.subscribe(PlantopiaSoundEvents::setup);
	}

	private static void initCommon(@NotNull EventEmitter eventEmitter) {
		eventEmitter.subscribe(PlantopiaCompats::setup);
		eventEmitter.subscribe(PlantopiaAdvancementTriggers::setup);
	}

	private static void initClient(@NotNull EventEmitter eventEmitter) {
		eventEmitter.subscribe(PlantopiaBlockColors::setup);
		eventEmitter.subscribe(PlantopiaItemColors::setup);
		eventEmitter.subscribe(PlantopiaBlockRenderLayers::setup);
	}
}