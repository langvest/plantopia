package by.langvest.plantopia.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class PlantopiaProportionConfig {
    public static final Codec<PlantopiaProportionConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FloatProvider.CODEC.fieldOf("ratio").forGetter(it -> it.ratio),
        IntProvider.CODEC.optionalFieldOf("min").forGetter(it -> it.min),
        IntProvider.CODEC.optionalFieldOf("max").forGetter(it -> it.max)
    ).apply(instance, PlantopiaProportionConfig::new));

    protected final FloatProvider ratio;
    protected final Optional<IntProvider> min;
    protected final Optional<IntProvider> max;

    protected PlantopiaProportionConfig(FloatProvider ratio, Optional<IntProvider> min, Optional<IntProvider> max) {
        this.ratio = ratio;
        this.min = min;
        this.max = max;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PlantopiaProportionConfig of(FloatProvider ratio) {
        return new PlantopiaProportionConfig(ratio, Optional.empty(), Optional.empty());
    }

    public static @NotNull PlantopiaProportionConfig of(FloatProvider ratio, IntProvider min) {
        return new PlantopiaProportionConfig(ratio, Optional.of(min), Optional.empty());
    }

    public static @NotNull PlantopiaProportionConfig of(FloatProvider ratio, IntProvider min, IntProvider max) {
        return new PlantopiaProportionConfig(ratio, Optional.of(min), Optional.of(max));
    }

    public int getValue(RandomSource random, int baseValue) {
        return Math.round(baseValue * ratio.sample(random));
    }

    public int getMin(RandomSource random) {
        return min.map(provider -> provider.sample(random)).orElse(0);
    }

    public int getMax(RandomSource random) {
        return max.map(provider -> provider.sample(random)).orElse(Integer.MAX_VALUE);
    }

    public int getClampedValue(RandomSource random, int baseValue, Function<Integer, Integer> refiner) {
        int value = getValue(random, baseValue);
        int min = getMin(random);
        int max = getMax(random);
        return Mth.clamp(refiner.apply(value), min, max);
    }

    public int getClampedValue(RandomSource random, int baseValue) {
        return getClampedValue(random, baseValue, value -> value);
    }
}
