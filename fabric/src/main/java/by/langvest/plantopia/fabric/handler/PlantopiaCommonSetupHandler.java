package by.langvest.plantopia.fabric.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.event.game.EntitySpawnEvent;
import by.langvest.toolkit.platform.EventEmitter;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaCommonSetupHandler {
    public static void setup() {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var registryHelper = Plantopia.getPlatform().getRegistryHelper();

        globalEventEmitter.emit(new RegisterEvent() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier) {
                registryHelper.getKnownRegistryOrThrow(registryKey).register(identifier, supplier);
            }
        });

        globalEventEmitter.emit(new LifecycleEvent.CommonSetupEvent());

        PlantopiaPlacements.DECLARATION.forEach((placedFeatureKey, declaration) -> {
            var placedFeatureName = nameOf(placedFeatureKey);
            var biomeTagKey = PlantopiaBiomeTags.createBiomeHasFeatureTag(placedFeatureName);

            BiomeModifications.addFeature(
                BiomeSelectors.tag(biomeTagKey),
                declaration.getGenerationStep(),
                placedFeatureKey
            );
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, level) ->
            globalEventEmitter.emit(new EntitySpawnEvent() {
                @Override
                public Entity getEntity() {
                    return entity;
                }

                @Override
                public Level getLevel() {
                    return level;
                }
            })
        );
    }
}
