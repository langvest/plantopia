package by.langvest.plantopia.client;

import by.langvest.plantopia.client.color.PlantopiaBlockColors;
import by.langvest.plantopia.client.color.PlantopiaItemColors;
import by.langvest.plantopia.client.render.PlantopiaBlockEntityRenderers;
import by.langvest.plantopia.client.render.PlantopiaBlockRenderLayers;
import by.langvest.plantopia.client.render.PlantopiaItemRenderers;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaClient {
    public static void init(Platform platform) {
        var globalEmitter = EventEmitter.getDefaultInstance();

        PlantopiaClient.initClient(globalEmitter);
    }

    private static void initClient(@NotNull EventEmitter emitter) {
        emitter.subscribe(PlantopiaBlockColors::setup);
        emitter.subscribe(PlantopiaItemColors::setup);
        emitter.subscribe(PlantopiaBlockRenderLayers::setup);
        emitter.subscribe(PlantopiaItemRenderers::setup);
        emitter.subscribe(PlantopiaBlockEntityRenderers::setup);
    }
}
