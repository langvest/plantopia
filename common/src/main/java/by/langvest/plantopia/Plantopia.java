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
        emitter.subscribe(PlantopiaBlockEntityTypes::setup);
        emitter.subscribe(PlantopiaItems::setup);
        emitter.subscribe(PlantopiaKits::setup);
        emitter.subscribe(PlantopiaCreativeModeTabs::setup);
        emitter.subscribe(PlantopiaEntityTypes::setup);
        emitter.subscribe(PlantopiaBoatTypes::setup);
        emitter.subscribe(PlantopiaParticleTypes::setup);
        emitter.subscribe(PlantopiaSoundEvents::setup);
        emitter.subscribe(PlantopiaFeatureTypes::setup);
        emitter.subscribe(PlantopiaBlockPlacerTypes::setup);
        emitter.subscribe(PlantopiaVerticalAnchorTypes::setup);
        emitter.subscribe(PlantopiaTreeDecoratorTypes::setup);
        emitter.subscribe(PlantopiaBlockStateProviderTypes::setup);
        emitter.subscribe(PlantopiaPlacementModifierTypes::setup);
        emitter.subscribe(PlantopiaRecipeSerializers::setup);

        // Common
        emitter.subscribe(PlantopiaBoatType::setup);
        emitter.subscribe(PlantopiaCompats::setup);
        emitter.subscribe(PlantopiaRegions::setup);
        emitter.subscribe(PlantopiaSurfaceRules::setup);
        emitter.subscribe(PlantopiaCommands::setup);
        emitter.subscribe(PlantopiaCriteriaTriggers::setup);
    }
}
