package by.langvest.plantopia.client;

import by.langvest.plantopia.client.color.PlantopiaBlockColors;
import by.langvest.plantopia.client.color.PlantopiaItemColors;
import by.langvest.plantopia.client.particle.PlantopiaParticleProviders;
import by.langvest.plantopia.client.property.PlantopiaItemProperties;
import by.langvest.plantopia.client.render.PlantopiaBlockEntityRenderers;
import by.langvest.plantopia.client.render.PlantopiaBlockRenderLayers;
import by.langvest.plantopia.client.render.PlantopiaItemRenderers;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaClient {
    public static void init(Platform platform) {
        var globalEmitter = EventEmitter.getDefaultInstance();

        PlantopiaClient.addListeners(globalEmitter);
    }

    @SuppressWarnings("DuplicatedCode")
    private static void addListeners(@NotNull EventEmitter emitter) {
        // Colors
        emitter.subscribe(PlantopiaBlockColors::setup);
        emitter.subscribe(PlantopiaItemColors::setup);

        // Block rendering
        emitter.subscribe(PlantopiaBlockRenderLayers::setup);
        emitter.subscribe(PlantopiaBlockEntityRenderers::setup);

        // Item rendering
        emitter.subscribe(PlantopiaItemRenderers::setup);
        emitter.subscribe(PlantopiaItemProperties::setup);

        // Particles
        emitter.subscribe(PlantopiaParticleProviders::setup);
    }
}
