package by.langvest.plantopia.handler;

import by.langvest.plantopia.handler.special.PlantopiaEntityHandler;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.platform.EventEmitter;

public class PlantopiaEventHandlers {
    public static void setup(LifecycleEvent.CommonSetupEvent event) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        globalEventEmitter.subscribe(PlantopiaEntityHandler::onEntitySpawn);
    }
}
