package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.util.PlantopiaTagSet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PlantopiaPlacedFeatureDeclaration {
    private final Function<BootstapContext<PlacedFeature>, Holder<ConfiguredFeature<?, ?>>> featureFactory;
    private final Function<BootstapContext<PlacedFeature>, List<PlacementModifier>> modifiersFactory;
    private final Function<PlantopiaTagSet<Biome>, PlantopiaTagSet<Biome>> biomesFactory;
    private final GenerationStep.Decoration generationStep;

    @Contract(pure = true)
    protected PlantopiaPlacedFeatureDeclaration(@NotNull Builder builder) {
        this.featureFactory = builder.featureFactory;
        this.modifiersFactory = builder.modifiersFactory;
        this.biomesFactory = Objects.requireNonNullElse(builder.biomesFactory, tagSet -> tagSet);
        this.generationStep = Objects.requireNonNullElse(builder.generationStep, GenerationStep.Decoration.VEGETAL_DECORATION);
    }

    @Contract(value = " -> new", pure = true)
    public static PlantopiaPlacedFeatureDeclaration.@NotNull Builder builder() {
        return new PlantopiaPlacedFeatureDeclaration.Builder();
    }

    public PlacedFeature getPlacedFeature(BootstapContext<PlacedFeature> context) {
        Objects.requireNonNull(featureFactory);
        Objects.requireNonNull(modifiersFactory);

        var feature = featureFactory.apply(context);
        var modifiers = modifiersFactory.apply(context);

        return new PlacedFeature(feature, modifiers);
    }

    public PlantopiaTagSet<Biome> getBiomeTagSet() {
        Objects.requireNonNull(biomesFactory);

        return biomesFactory.apply(PlantopiaTagSet.newTagSet());
    }

    public GenerationStep.Decoration getGenerationStep() {
        Objects.requireNonNull(generationStep);

        return generationStep;
    }

    public static class Builder {
        private Function<BootstapContext<PlacedFeature>, Holder<ConfiguredFeature<?, ?>>> featureFactory;
        private Function<BootstapContext<PlacedFeature>, List<PlacementModifier>> modifiersFactory = context -> List.of();
        private Function<PlantopiaTagSet<Biome>, PlantopiaTagSet<Biome>> biomesFactory;
        private GenerationStep.Decoration generationStep;

        public PlantopiaPlacedFeatureDeclaration build() {
            return new PlantopiaPlacedFeatureDeclaration(this);
        }

        public Builder feature(Function<BootstapContext<PlacedFeature>, Holder<ConfiguredFeature<?, ?>>> featureFactory) {
            this.featureFactory = featureFactory;
            return this;
        }

        public Builder feature(ResourceKey<ConfiguredFeature<?, ?>> featureKey) {
            this.featureFactory = context -> {
                HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
                return features.getOrThrow(featureKey);
            };

            return this;
        }

        public Builder modifiers(Function<BootstapContext<PlacedFeature>, List<PlacementModifier>> modifiersFactory) {
            this.modifiersFactory = modifiersFactory;
            return this;
        }

        public Builder generationStep(GenerationStep.Decoration generationStep) {
            this.generationStep = generationStep;
            return this;
        }

        public Builder biomes(Function<PlantopiaTagSet<Biome>, PlantopiaTagSet<Biome>> biomesFactory) {
            this.biomesFactory = biomesFactory;
            return this;
        }

    }
}
