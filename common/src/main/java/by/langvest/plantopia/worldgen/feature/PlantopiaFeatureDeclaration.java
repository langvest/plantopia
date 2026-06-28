package by.langvest.plantopia.worldgen.feature;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class PlantopiaFeatureDeclaration {
    private final Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> featureFactory;

    @Contract(pure = true)
    protected PlantopiaFeatureDeclaration(@NotNull Builder builder) {
        this.featureFactory = builder.featureFactory;
    }

    @Contract(value = " -> new", pure = true)
    public static PlantopiaFeatureDeclaration.@NotNull Builder builder() {
        return new PlantopiaFeatureDeclaration.Builder();
    }

    public ConfiguredFeature<?, ?> getConfiguredFeature(BootstapContext<ConfiguredFeature<?, ?>> context) {
        Objects.requireNonNull(featureFactory);

        return featureFactory.apply(context);
    }

    public static class Builder {
        private Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> featureFactory;

        public PlantopiaFeatureDeclaration build() {
            return new PlantopiaFeatureDeclaration(this);
        }

        public Builder feature(Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> factory) {
            this.featureFactory = factory;
            return this;
        }
    }
}
