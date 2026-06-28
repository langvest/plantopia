package by.langvest.plantopia.client;

import by.langvest.plantopia.client.color.PlantopiaBlockColors;
import by.langvest.plantopia.client.color.PlantopiaItemColors;
import by.langvest.plantopia.client.particle.PlantopiaParticleProviders;
import by.langvest.plantopia.client.property.PlantopiaItemProperties;
import by.langvest.plantopia.client.render.*;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;

public final class PlantopiaClient {
    public static void init(Platform platform) {
        PlantopiaClient.addListeners(platform);
    }

    @SuppressWarnings("DuplicatedCode")
    private static void addListeners(Platform platform) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        // Colors
        globalEventEmitter.subscribe(PlantopiaBlockColors::setup);
        globalEventEmitter.subscribe(PlantopiaItemColors::setup);

        // Block rendering
        globalEventEmitter.subscribe(PlantopiaBlockRenderLayers::setup);
        globalEventEmitter.subscribe(PlantopiaBlockEntityRenderers::setup);

        // Entity rendering
        globalEventEmitter.subscribe(PlantopiaEntityRenderers::setup);
        globalEventEmitter.subscribe(PlantopiaEntityLayerDefinitions::setup);

        // Item rendering
        globalEventEmitter.subscribe(PlantopiaItemRenderers::setup);
        globalEventEmitter.subscribe(PlantopiaItemProperties::setup);

        // Particles
        globalEventEmitter.subscribe(PlantopiaParticleProviders::setup);
    }
}
