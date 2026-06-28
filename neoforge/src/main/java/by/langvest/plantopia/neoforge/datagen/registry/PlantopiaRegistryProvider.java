package by.langvest.plantopia.neoforge.datagen.registry;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.damage.PlantopiaDamageTypes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.noise.PlantopiaNoises;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaRegistryProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.BIOME, PlantopiaRegistryProvider::addBiomes)
        .add(Registries.NOISE, PlantopiaRegistryProvider::addNoises)
        .add(Registries.DAMAGE_TYPE, PlantopiaRegistryProvider::addDamageTypes)
        .add(Registries.CONFIGURED_FEATURE, PlantopiaRegistryProvider::addConfiguredFeatures)
        .add(Registries.PLACED_FEATURE, PlantopiaRegistryProvider::addPlacedFeatures)
        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, PlantopiaRegistryProvider::addBiomeModifiers);

    public PlantopiaRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(Plantopia.MOD_ID));
    }

    private static void addBiomes(BootstapContext<Biome> context) {
        PlantopiaBiomes.DECLARATION.forEach((key, declaration) -> {
            var biome = declaration.getBiome(context);
            context.register(key, biome);
        });
    }

    private static void addNoises(@NotNull BootstapContext<NormalNoise.NoiseParameters> context) {
        context.register(PlantopiaNoises.WEIGHTED, new NormalNoise.NoiseParameters(0, 1.0));
        context.register(PlantopiaNoises.MARSH, new NormalNoise.NoiseParameters(-1, 1.0));
    }

    private static void addDamageTypes(@NotNull BootstapContext<DamageType> context) {
        context.register(PlantopiaDamageTypes.THORNY_SHRUB, new DamageType("thornyShrub", 0.1F, DamageEffects.POKING));
        context.register(PlantopiaDamageTypes.QUICKSAND, new DamageType("quicksand", 0.0F));
    }

    private static void addConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
        PlantopiaFeatures.DECLARATION.forEach((key, declaration) -> {
            var configuredFeature = declaration.getConfiguredFeature(context);
            context.register(key, configuredFeature);
        });
    }

    private static void addPlacedFeatures(BootstapContext<PlacedFeature> context) {
        PlantopiaPlacements.DECLARATION.forEach((key, declaration) -> {
            var placedFeature = declaration.getPlacedFeature(context);
            context.register(key, placedFeature);
        });
    }

    private static void addBiomeModifiers(@NotNull BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        PlantopiaPlacements.DECLARATION.forEach((placedFeatureKey, declaration) -> {
            var biomeTagSet = declaration.getBiomeTagSet();

            if (biomeTagSet.isEmpty()) return;

            var placedFeatureName = nameOf(placedFeatureKey);
            var biomeModifierKey = createBiomeModifierKey("add_feature/" + placedFeatureName);
            var biomeTagKey = PlantopiaBiomeTags.createBiomeHasFeatureTag(placedFeatureName);

            context.register(biomeModifierKey, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(biomeTagKey),
                HolderSet.direct(placedFeatures.getOrThrow(placedFeatureKey)),
                declaration.getGenerationStep()
            ));
        });
    }

    /* HELPER METHODS *****************************************************************************************/

    private static @NotNull ResourceKey<BiomeModifier> createBiomeModifierKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, plantopia(name));
    }
}
