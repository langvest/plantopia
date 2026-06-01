package by.langvest.plantopia.neoforge.datagen.registry;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.damage.PlantopiaDamageTypes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.noise.PlantopiaNoises;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PlantopiaRegistryProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.BIOME, PlantopiaBiomes::bootstrap)
        .add(Registries.NOISE, PlantopiaNoises::bootstrap)
        .add(Registries.DAMAGE_TYPE, PlantopiaDamageTypes::bootstrap)
        .add(Registries.CONFIGURED_FEATURE, PlantopiaFeatures::bootstrap)
        .add(Registries.PLACED_FEATURE, PlantopiaPlacements::bootstrap);

    public PlantopiaRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(Plantopia.MOD_ID));
    }
}
