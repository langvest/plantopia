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
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.recipe.PlantopiaRecipeSerializers;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.worldgen.region.PlantopiaRegions;
import by.langvest.plantopia.worldgen.surface.PlantopiaSurfaceRules;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockStateProviderTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaBlockPlacerTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaTreeDecoratorTypes;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorTypes;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Plantopia {
    public static final String MOD_ID = "plantopia";
    private static Platform platform;

    public static void init(Platform platform) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        Plantopia.injectPlatform(platform);
        Plantopia.addListeners(globalEventEmitter);

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
    private static void addListeners(@NotNull EventEmitter eventEmitter) {
        // Registries
        eventEmitter.subscribe(PlantopiaBlocks::setup);
        eventEmitter.subscribe(PlantopiaBlockEntityTypes::setup);
        eventEmitter.subscribe(PlantopiaItems::setup);
        eventEmitter.subscribe(PlantopiaKits::setup);
        eventEmitter.subscribe(PlantopiaCreativeModeTabs::setup);
        eventEmitter.subscribe(PlantopiaEntityTypes::setup);
        eventEmitter.subscribe(PlantopiaBoatTypes::setup);
        eventEmitter.subscribe(PlantopiaParticleTypes::setup);
        eventEmitter.subscribe(PlantopiaSoundEvents::setup);
        eventEmitter.subscribe(PlantopiaFeatureTypes::setup);
        eventEmitter.subscribe(PlantopiaBlockPlacerTypes::setup);
        eventEmitter.subscribe(PlantopiaVerticalAnchorTypes::setup);
        eventEmitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
        eventEmitter.subscribe(PlantopiaBlockStateProviderTypes::setup);
        eventEmitter.subscribe(PlantopiaPlacementModifierTypes::setup);
        eventEmitter.subscribe(PlantopiaRecipeSerializers::setup);
        eventEmitter.subscribe(PlantopiaCriteriaTriggers::setup);

        // Common
        eventEmitter.subscribe(PlantopiaBoatType::setup);
        eventEmitter.subscribe(PlantopiaCompats::setup);
        eventEmitter.subscribe(PlantopiaRegions::setup);
        eventEmitter.subscribe(PlantopiaSurfaceRules::setup);
        eventEmitter.subscribe(PlantopiaCommands::setup);
    }
}
