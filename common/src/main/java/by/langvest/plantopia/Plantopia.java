//
//                          _ooOoo_
//                         o8888888o
//                         88" . "88
//                         (| -_- |)
//                          O\ = /O
//                      ____/`---'\____
//                    .   ' \\| |// `.
//                     / \\||| 8 |||// \
//                   / _||||| -8- |||||- \
//                     | | \\\ 8 /// | |
//                   | \_| ''\-8-/'' | |
//                    \ .-\__ `8` ___/-. /
//                 ___`. .' /--8--\ `. . __
//              ."" '< `.___\_<8>_/___.' >'"".
//             | | : `- \`.;`\ 8 /`;.`/ - ` : | |
//               \ \ `-. \_ __\ /__ _/ .-` / /
//       ======`-.____`-.___\_____/___.-`____.-'======
//                          `=---='
//
//       .............................................
//                Buddha bless, never bug
//                   Buddha said:
//                         The office in the office building, the programmer in the office;
//                         The programmer writes the program and exchanges the program for wine.
//                         I only sit on the Internet when waking up, and come to sleep under the Internet when drunk;
//                         Drunk and sober day after day, online and offline year after year.
//                         I hope to die in the computer room and not bow to the boss;
//                         Mercedes-Benz BMW is the most interesting, bus programmer.
//                         People laugh at me crazy, I laugh at others watching porn;
//                         If you don’t see all the pretty girls, which one is the programmer?

package by.langvest.plantopia;

import by.langvest.plantopia.adv.PlantopiaCriteriaTriggers;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.blockentity.PlantopiaBlockEntityTypes;
import by.langvest.plantopia.client.PlantopiaClient;
import by.langvest.plantopia.command.PlantopiaCommands;
import by.langvest.plantopia.compat.PlantopiaCompats;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.entity.PlantopiaBoatTypes;
import by.langvest.plantopia.entity.PlantopiaEntityTypes;
import by.langvest.plantopia.handler.PlantopiaEventHandlers;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabContents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.worldgen.feature.*;
import by.langvest.plantopia.worldgen.region.PlantopiaRegions;
import by.langvest.plantopia.worldgen.surface.PlantopiaSurfaceRules;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaBlockStateProviderTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaIntProportionTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaVerticalAnchorTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaFloatProviderTypes;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Plantopia {
    public static final String MOD_ID = "plantopia";
    private static Platform platform;

    public static void init(Platform platform) {
        Plantopia.injectPlatform(platform);
        Plantopia.addListeners(platform);

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
    private static void addListeners(@NotNull Platform platform) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var localEventEmitter = platform.getEventEmitter();

        // Registries
        globalEventEmitter.subscribe(PlantopiaBlocks::setup);
        globalEventEmitter.subscribe(PlantopiaBlockEntityTypes::setup);
        globalEventEmitter.subscribe(PlantopiaItems::setup);
        globalEventEmitter.subscribe(PlantopiaKits::setup);
        globalEventEmitter.subscribe(PlantopiaCreativeModeTabs::setup);
        globalEventEmitter.subscribe(PlantopiaEntityTypes::setup);
        globalEventEmitter.subscribe(PlantopiaBoatTypes::setup);
        globalEventEmitter.subscribe(PlantopiaParticleTypes::setup);
        globalEventEmitter.subscribe(PlantopiaSoundEvents::setup);
        globalEventEmitter.subscribe(PlantopiaFeatureTypes::setup);
        globalEventEmitter.subscribe(PlantopiaBlockPlacerTypes::setup);
        globalEventEmitter.subscribe(PlantopiaVerticalAnchorTypes::setup);
        globalEventEmitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
        globalEventEmitter.subscribe(PlantopiaTrunkPlacerTypes::setup);
        globalEventEmitter.subscribe(PlantopiaFoliagePlacerTypes::setup);
        globalEventEmitter.subscribe(PlantopiaBlockStateProviderTypes::setup);
        globalEventEmitter.subscribe(PlantopiaPlacementModifierTypes::setup);
        globalEventEmitter.subscribe(PlantopiaRecipeSerializers::setup);
        globalEventEmitter.subscribe(PlantopiaCriteriaTriggers::setup);
        globalEventEmitter.subscribe(PlantopiaFloatProviderTypes::setup);
        globalEventEmitter.subscribe(PlantopiaIntProportionTypes::setup);

        // Common
        globalEventEmitter.subscribe(PlantopiaCreativeModeTabContents::setup);
        globalEventEmitter.subscribe(PlantopiaBoatType::setup);
        globalEventEmitter.subscribe(PlantopiaCompats::setup);
        globalEventEmitter.subscribe(PlantopiaCommands::setup);
        globalEventEmitter.subscribe(PlantopiaEventHandlers::setup);

        // TerraBlender
        localEventEmitter.subscribe(PlantopiaRegions::setup);
        localEventEmitter.subscribe(PlantopiaSurfaceRules::setup);
    }
}
