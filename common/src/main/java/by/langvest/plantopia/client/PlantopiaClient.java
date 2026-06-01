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
        var globalEventEmitter = EventEmitter.getDefaultInstance();

        PlantopiaClient.addListeners(globalEventEmitter);
    }

    @SuppressWarnings("DuplicatedCode")
    private static void addListeners(@NotNull EventEmitter eventEmitter) {
        // Colors
        eventEmitter.subscribe(PlantopiaBlockColors::setup);
        eventEmitter.subscribe(PlantopiaItemColors::setup);

        // Block rendering
        eventEmitter.subscribe(PlantopiaBlockRenderLayers::setup);
        eventEmitter.subscribe(PlantopiaBlockEntityRenderers::setup);

        // Entity rendering
        eventEmitter.subscribe(PlantopiaEntityRenderers::setup);
        eventEmitter.subscribe(PlantopiaEntityLayerDefinitions::setup);

        // Item rendering
        eventEmitter.subscribe(PlantopiaItemRenderers::setup);
        eventEmitter.subscribe(PlantopiaItemProperties::setup);

        // Particles
        eventEmitter.subscribe(PlantopiaParticleProviders::setup);

        // Other client work
        eventEmitter.subscribe(PlantopiaClient::setup);
    }

    private static void setup(LifecycleEvent.ClientSetupEvent event) {
        var workScheduler = Plantopia.getPlatform().getWorkScheduler();

        workScheduler.executeWork("client_setup");
    }
}
