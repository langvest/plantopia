package by.langvest.plantopia.fabric.handler;

import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import by.langvest.toolkit.event.LifecycleEvent;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.platform.EventEmitter;
import by.langvest.toolkit.platform.Platform;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaCommonSetupHandler {
    public static void init(@NotNull Platform platform) {
        var globalEventEmitter = EventEmitter.getDefaultInstance();
        var registryHelper = platform.getRegistryHelper();

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
    }
}
