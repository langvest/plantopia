package by.langvest.plantopia;

import by.langvest.plantopia.adv.trigger.PlantopiaAdvancementTriggers;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.PlantopiaBlockEntities;
import by.langvest.plantopia.client.PlantopiaClient;
import by.langvest.plantopia.command.PlantopiaCommands;
import by.langvest.plantopia.compat.PlantopiaCompats;
import by.langvest.plantopia.entity.PlantopiaEntities;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockStateProviderTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.toolkit.forge.ForgePlatform;
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
        var globalEmitter = EventEmitter.getDefaultInstance();

        Plantopia.injectPlatform(platform);
        Plantopia.addListeners(globalEmitter);

        if (platform.isClient()) {
            PlantopiaClient.init(platform);
        }
    }

    public static Platform getPlatform() {
        return Objects.requireNonNull(platform);
    }

    private static void injectPlatform(Platform platform) {
        Plantopia.platform = platform;
    }

    @SuppressWarnings("DuplicatedCode")
    private static void addListeners(@NotNull EventEmitter emitter) {
        // Registries
        emitter.subscribe(PlantopiaBlocks::setup);
        emitter.subscribe(PlantopiaBlockEntities::setup);
        emitter.subscribe(PlantopiaItems::setup);
        emitter.subscribe(PlantopiaCreativeModeTabs::setup);
        emitter.subscribe(PlantopiaEntities::setup);
        emitter.subscribe(PlantopiaParticleTypes::setup);
        emitter.subscribe(PlantopiaSoundEvents::setup);
        emitter.subscribe(PlantopiaFeatureTypes::setup);
        emitter.subscribe(PlantopiaBlockPlacerTypes::setup);
        emitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
        emitter.subscribe(PlantopiaBlockStateProviderTypes::setup);
        emitter.subscribe(PlantopiaPlacementModifierTypes::setup);
        emitter.subscribe(PlantopiaRecipeSerializers::setup);

        // Common
        emitter.subscribe(PlantopiaCompats::setup);
        emitter.subscribe(PlantopiaCommands::setup);
        emitter.subscribe(PlantopiaAdvancementTriggers::setup);
    }
}