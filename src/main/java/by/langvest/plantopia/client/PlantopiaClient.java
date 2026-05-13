package by.langvest.plantopia.client;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.client.color.PlantopiaBlockColors;
import by.langvest.plantopia.client.color.PlantopiaItemColors;
import by.langvest.plantopia.client.particle.PlantopiaParticleProviders;
import by.langvest.plantopia.client.property.PlantopiaItemProperties;
import by.langvest.plantopia.client.render.*;
import by.langvest.toolkit.event.LifecycleEvent;
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

        // Entity rendering
        emitter.subscribe(PlantopiaEntityRenderers::setup);
        emitter.subscribe(PlantopiaEntityLayerDefinitions::setup);

        // Item rendering
        emitter.subscribe(PlantopiaItemRenderers::setup);
        emitter.subscribe(PlantopiaItemProperties::setup);

        // Particles
        emitter.subscribe(PlantopiaParticleProviders::setup);

        // Other client work
        emitter.subscribe(PlantopiaClient::setup);
    }

    private static void setup(LifecycleEvent.ClientSetupEvent event) {
        var workScheduler = Plantopia.getPlatform().getWorkScheduler();

        workScheduler.executeWork("client_setup");
    }
}
