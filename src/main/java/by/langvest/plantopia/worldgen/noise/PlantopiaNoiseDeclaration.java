package by.langvest.plantopia.worldgen.noise;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Function;

public class PlantopiaNoiseDeclaration {
    private final Function<BootstapContext<NormalNoise.NoiseParameters>, NormalNoise.NoiseParameters> noiseFactory;

    @Contract(pure = true)
    protected PlantopiaNoiseDeclaration(@NotNull Builder builder) {
        this.noiseFactory = builder.noiseFactory;
    }

    @Contract(value = " -> new", pure = true)
    public static PlantopiaNoiseDeclaration.@NotNull Builder builder() {
        return new PlantopiaNoiseDeclaration.Builder();
    }

    public NormalNoise.NoiseParameters getNoise(BootstapContext<NormalNoise.NoiseParameters> context) {
        Objects.requireNonNull(noiseFactory);

        return noiseFactory.apply(context);
    }

    public static class Builder {
        private Function<BootstapContext<NormalNoise.NoiseParameters>, NormalNoise.NoiseParameters> noiseFactory;

        public PlantopiaNoiseDeclaration build() {
            return new PlantopiaNoiseDeclaration(this);
        }

        public Builder noise(Function<BootstapContext<NormalNoise.NoiseParameters>, NormalNoise.NoiseParameters> factory) {
            this.noiseFactory = factory;
            return this;
        }
    }
}
