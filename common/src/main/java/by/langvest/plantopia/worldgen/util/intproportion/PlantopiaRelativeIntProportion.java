package by.langvest.plantopia.worldgen.util.intproportion;

import by.langvest.plantopia.worldgen.util.PlantopiaIntProportionType;
import by.langvest.plantopia.worldgen.util.PlantopiaIntProportionTypes;
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
public class PlantopiaRelativeIntProportion extends PlantopiaIntProportion {
    public static final Codec<PlantopiaRelativeIntProportion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        FloatProvider.CODEC.fieldOf("ratio").forGetter(it -> it.ratio),
        IntProvider.CODEC.optionalFieldOf("min").forGetter(it -> it.min),
        IntProvider.CODEC.optionalFieldOf("max").forGetter(it -> it.max)
    ).apply(instance, PlantopiaRelativeIntProportion::new));

    protected final FloatProvider ratio;
    protected final Optional<IntProvider> min;
    protected final Optional<IntProvider> max;

    protected PlantopiaRelativeIntProportion(FloatProvider ratio, Optional<IntProvider> min, Optional<IntProvider> max) {
        this.ratio = ratio;
        this.min = min;
        this.max = max;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PlantopiaRelativeIntProportion of(FloatProvider ratio) {
        return new PlantopiaRelativeIntProportion(ratio, Optional.empty(), Optional.empty());
    }

    public static @NotNull PlantopiaRelativeIntProportion of(FloatProvider ratio, IntProvider min) {
        return new PlantopiaRelativeIntProportion(ratio, Optional.of(min), Optional.empty());
    }

    public static @NotNull PlantopiaRelativeIntProportion of(FloatProvider ratio, IntProvider min, IntProvider max) {
        return new PlantopiaRelativeIntProportion(ratio, Optional.of(min), Optional.of(max));
    }

    @Override
    public PlantopiaIntProportionType<?> type() {
        return PlantopiaIntProportionTypes.RELATIVE.get();
    }

    public int getRawValue(RandomSource random, int baseValue) {
        return Math.round(baseValue * ratio.sample(random));
    }

    public int getMinValue(RandomSource random, int baseValue) {
        return min.map(provider -> provider.sample(random)).orElse(0);
    }

    public int getMaxValue(RandomSource random, int baseValue) {
        return max.map(provider -> provider.sample(random)).orElse(Integer.MAX_VALUE);
    }

    @Override
    public int sample(RandomSource random, int baseValue, Function<Integer, Integer> refiner) {
        int raw = getRawValue(random, baseValue);
        int min = getMinValue(random, baseValue);
        int max = getMaxValue(random, baseValue);
        return Mth.clamp(refiner.apply(raw), min, max);
    }
}
