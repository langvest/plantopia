package by.langvest.plantopia.worldgen.biome;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.worldgen.region.special.PlantopiaRegion;
import by.langvest.toolkit.registry.RegistryObject;
import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.hexToInt;
import static by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes.calculateSkyColor;

public class PlantopiaBiomeDeclaration {
    private boolean hasPrecipitation;
    private @Nullable Float temperature;
    private Biome.TemperatureModifier temperatureModifier;
    private @Nullable Float downfall;
    private List<RegistryObject<PlantopiaRegion>> regions;

    private Function<Biome.BiomeBuilder, Biome.BiomeBuilder> modifyBiome;
    private Function<BiomeSpecialEffects.Builder, BiomeSpecialEffects.Builder> modifySpecialEffects;
    private Function<BiomeGenerationSettings.Builder, BiomeGenerationSettings.Builder> modifyGeneration;
    private Function<MobSpawnSettings.Builder, MobSpawnSettings.Builder> modifySpawn;

    @Contract(pure = true)
    protected PlantopiaBiomeDeclaration(@NotNull Builder builder) {
        this.hasPrecipitation = builder.hasPrecipitation;
        this.temperature = builder.temperature;
        this.temperatureModifier = builder.temperatureModifier;
        this.downfall = builder.downfall;
        this.regions = builder.regions;

        this.modifyBiome = builder.modifyBiome;
        this.modifySpecialEffects = builder.modifySpecialEffects;
        this.modifyGeneration = builder.modifyGeneration;
        this.modifySpawn = builder.modifySpawn;
    }

    @Contract(value = " -> new", pure = true)
    public static PlantopiaBiomeDeclaration.@NotNull Builder builder() {
        return new PlantopiaBiomeDeclaration.Builder();
    }

    public Biome getBiome(@NotNull BootstapContext<Biome> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var configuredCarvers = context.lookup(Registries.CONFIGURED_CARVER);

        var spawnBuilder = new MobSpawnSettings.Builder();
        var generationBuilder = new BiomeGenerationSettings.Builder(placedFeatures, configuredCarvers);
        var specialEffectsBuilder = new BiomeSpecialEffects.Builder();
        var biomeBuilder = new Biome.BiomeBuilder();

        return modifyBiome.apply(
            biomeBuilder
                .specialEffects(modifySpecialEffects.apply(specialEffectsBuilder).build())
                .mobSpawnSettings(modifySpawn.apply(spawnBuilder).build())
                .generationSettings(modifyGeneration.apply(generationBuilder).build())
        ).build();
    }

    public static class Builder {
        private boolean hasPrecipitation = true;
        private @Nullable Float temperature;
        private Biome.TemperatureModifier temperatureModifier = Biome.TemperatureModifier.NONE;
        private @Nullable Float downfall;
        private List<RegistryObject<PlantopiaRegion>> regions = Lists.newArrayList();

        private Function<Biome.BiomeBuilder, Biome.BiomeBuilder> modifyBiome = biomeBuilder -> biomeBuilder;
        private Function<BiomeSpecialEffects.Builder, BiomeSpecialEffects.Builder> modifySpecialEffects = specialEffectsBuilder -> specialEffectsBuilder.waterColor(4159204).waterFogColor(329011).fogColor(12638463);
        private Function<BiomeGenerationSettings.Builder, BiomeGenerationSettings.Builder> modifyGeneration = generationBuilder -> generationBuilder;
        private Function<MobSpawnSettings.Builder, MobSpawnSettings.Builder> modifySpawn = spawnBuilder -> spawnBuilder;

        public PlantopiaBiomeDeclaration build() {
            return new PlantopiaBiomeDeclaration(this);
        }

        public Builder apply(@NotNull Consumer<Builder> consumer) {
            consumer.accept(this);
            return this;
        }

        public Builder applyBiome(@NotNull Consumer<Biome.BiomeBuilder> consumer) {
            var prevModifyBiome = this.modifyBiome;
            this.modifyBiome = biomeBuilder -> {
                var builder = prevModifyBiome.apply(biomeBuilder);
                consumer.accept(builder);
                return builder;
            };
            return this;
        }

        public Builder applySpecialEffects(@NotNull Consumer<BiomeSpecialEffects.Builder> consumer) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> {
                var builder = prevModifySpecialEffects.apply(specialEffectsBuilder);
                consumer.accept(builder);
                return builder;
            };
            return this;
        }

        public Builder applyGeneration(@NotNull Consumer<BiomeGenerationSettings.Builder> consumer) {
            var prevModifyGeneration = this.modifyGeneration;
            this.modifyGeneration = generationBuilder -> {
                var builder = prevModifyGeneration.apply(generationBuilder);
                consumer.accept(builder);
                return builder;
            };
            return this;
        }

        public Builder applySpawn(@NotNull Consumer<MobSpawnSettings.Builder> consumer) {
            var prevModifySpawn = this.modifySpawn;
            this.modifySpawn = spawnBuilder -> {
                var builder = prevModifySpawn.apply(spawnBuilder);
                consumer.accept(builder);
                return builder;
            };
            return this;
        }

        /* REGION *****************************************************************************************************/

        @SafeVarargs
        public final Builder region(RegistryObject<PlantopiaRegion>... regions) {
            this.regions.addAll(List.of(regions));
            return this;
        }

        /* BIOME SETTINGS *********************************************************************************************/

        public Builder hasPrecipitation(boolean hasPrecipitation) {
            this.hasPrecipitation = hasPrecipitation;
            var prevModifyBiome = this.modifyBiome;
            this.modifyBiome = biomeBuilder -> prevModifyBiome.apply(biomeBuilder).hasPrecipitation(hasPrecipitation);
            return this;
        }

        public Builder temperature(float temperature) {
            this.temperature = temperature;
            var prevModifyBiome = this.modifyBiome;
            this.modifyBiome = biomeBuilder -> prevModifyBiome.apply(biomeBuilder).temperature(temperature);
            return this.skyColor(calculateSkyColor(temperature));
        }

        public Builder downfall(float downfall) {
            this.downfall = downfall;
            var prevModifyBiome = this.modifyBiome;
            this.modifyBiome = biomeBuilder -> prevModifyBiome.apply(biomeBuilder).downfall(downfall);
            return this;
        }

        public Builder temperatureAdjustment(Biome.TemperatureModifier temperatureModifier) {
            this.temperatureModifier = temperatureModifier;
            var prevModifyBiome = this.modifyBiome;
            this.modifyBiome = biomeBuilder -> prevModifyBiome.apply(biomeBuilder).temperatureAdjustment(temperatureModifier);
            return this;
        }

        /* SPECIAL EFFECTS ********************************************************************************************/

        public Builder fogColor(int fogColor) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).fogColor(fogColor);
            return this;
        }

        public Builder fogColor(String fogColor) {
            return this.fogColor(hexToInt(fogColor));
        }

        public Builder waterColor(int waterColor) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).waterColor(waterColor);
            return this;
        }

        public Builder waterColor(String waterColor) {
            return this.waterColor(hexToInt(waterColor));
        }

        public Builder waterFogColor(int waterFogColor) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).waterFogColor(waterFogColor);
            return this;
        }

        public Builder waterFogColor(String waterFogColor) {
            return this.waterFogColor(hexToInt(waterFogColor));
        }

        public Builder skyColor(int skyColor) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).skyColor(skyColor);
            return this;
        }

        public Builder skyColor(String skyColor) {
            return this.skyColor(hexToInt(skyColor));
        }

        public Builder foliageColorOverride(int foliageColorOverride) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).foliageColorOverride(foliageColorOverride);
            return this;
        }

        public Builder foliageColorOverride(String foliageColorOverride) {
            return this.foliageColorOverride(hexToInt(foliageColorOverride));
        }

        public Builder grassColorOverride(int grassColorOverride) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).grassColorOverride(grassColorOverride);
            return this;
        }

        public Builder grassColorOverride(String grassColorOverride) {
            return this.grassColorOverride(hexToInt(grassColorOverride));
        }

        public Builder grassColorModifier(BiomeSpecialEffects.GrassColorModifier grassColorModifier) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).grassColorModifier(grassColorModifier);
            return this;
        }

        public Builder ambientParticle(AmbientParticleSettings ambientParticle) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).ambientParticle(ambientParticle);
            return this;
        }

        public Builder ambientLoopSound(Holder<SoundEvent> ambientLoopSound) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).ambientLoopSound(ambientLoopSound);
            return this;
        }

        public Builder ambientMoodSound(AmbientMoodSettings ambientMoodSound) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).ambientMoodSound(ambientMoodSound);
            return this;
        }

        public Builder ambientAdditionsSound(AmbientAdditionsSettings ambientAdditionsSound) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).ambientAdditionsSound(ambientAdditionsSound);
            return this;
        }

        public Builder backgroundMusic(@Nullable Music backgroundMusic) {
            var prevModifySpecialEffects = this.modifySpecialEffects;
            this.modifySpecialEffects = specialEffectsBuilder -> prevModifySpecialEffects.apply(specialEffectsBuilder).backgroundMusic(backgroundMusic);
            return this;
        }

        /* GENERATION *************************************************************************************************/

        public Builder addFeature(GenerationStep.Decoration decoration, @NotNull ResourceKey<PlacedFeature> placedFeature) {
            if (placedFeature.location().getNamespace().equals(Plantopia.MOD_ID)) {
                throw new IllegalArgumentException("Use biome modifications for Plantopia placed features!");
            }

            var prevModifyGeneration = this.modifyGeneration;
            this.modifyGeneration = generationBuilder -> prevModifyGeneration.apply(generationBuilder).addFeature(decoration, placedFeature);
            return this;
        }

        public Builder addCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> carver) {
            var prevModifyGeneration = this.modifyGeneration;
            this.modifyGeneration = generationBuilder -> prevModifyGeneration.apply(generationBuilder).addCarver(carving, carver);
            return this;
        }

        /* SPAWN ******************************************************************************************************/

        public Builder addSpawn(MobCategory classification, Supplier<MobSpawnSettings.SpawnerData> spawner) {
            var prevModifySpawn = this.modifySpawn;
            this.modifySpawn = spawnBuilder -> prevModifySpawn.apply(spawnBuilder).addSpawn(classification, spawner.get());
            return this;
        }

        public Builder addSpawn(MobCategory classification, MobSpawnSettings.SpawnerData spawner) {
            return this.addSpawn(classification, () -> spawner);
        }

        public Builder addSpawn(MobCategory classification, Supplier<EntityType<?>> entityType, Weight weight, int minCount, int maxCount) {
            return this.addSpawn(classification, () -> new MobSpawnSettings.SpawnerData(entityType.get(), weight, minCount, maxCount));
        }

        public Builder addSpawn(MobCategory classification, EntityType<?> entityType, Weight weight, int minCount, int maxCount) {
            return this.addSpawn(classification, () -> new MobSpawnSettings.SpawnerData(entityType, weight, minCount, maxCount));
        }

        public Builder addSpawn(MobCategory classification, Supplier<EntityType<?>> entityType, int weight, int minCount, int maxCount) {
            return this.addSpawn(classification, () -> new MobSpawnSettings.SpawnerData(entityType.get(), weight, minCount, maxCount));
        }

        public Builder addSpawn(MobCategory classification, EntityType<?> entityType, int weight, int minCount, int maxCount) {
            return this.addSpawn(classification, () -> new MobSpawnSettings.SpawnerData(entityType, weight, minCount, maxCount));
        }

        public Builder addMobCharge(Supplier<EntityType<?>> entityType, double charge, double energyBudget) {
            var prevModifySpawn = this.modifySpawn;
            this.modifySpawn = spawnBuilder -> prevModifySpawn.apply(spawnBuilder).addMobCharge(entityType.get(), charge, energyBudget);
            return this;
        }

        public Builder creatureGenerationProbability(float probability) {
            var prevModifySpawn = this.modifySpawn;
            this.modifySpawn = spawnBuilder -> prevModifySpawn.apply(spawnBuilder).creatureGenerationProbability(probability);
            return this;
        }
    }
}
