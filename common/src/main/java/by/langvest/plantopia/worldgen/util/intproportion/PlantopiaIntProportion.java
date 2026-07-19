package by.langvest.plantopia.worldgen.util.intproportion;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.util.PlantopiaIntProportionType;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public abstract class PlantopiaIntProportion {
    public static final Codec<PlantopiaIntProportion> CODEC = PlantopiaRegistries.INT_PROPORTION_TYPE.byNameCodec().dispatch(PlantopiaIntProportion::type, PlantopiaIntProportionType::codec);

    public abstract PlantopiaIntProportionType<?> type();

    public abstract int sample(RandomSource random, int baseValue, Function<Integer, Integer> refiner);

    public int sample(RandomSource random, int baseValue) {
        return sample(random, baseValue, value -> value);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PlantopiaRelativeIntProportion relative(FloatProvider ratio) {
        return new PlantopiaRelativeIntProportion(ratio, Optional.empty(), Optional.empty());
    }

    public static @NotNull PlantopiaRelativeIntProportion relative(FloatProvider ratio, IntProvider min) {
        return new PlantopiaRelativeIntProportion(ratio, Optional.of(min), Optional.empty());
    }

    public static @NotNull PlantopiaRelativeIntProportion relative(FloatProvider ratio, IntProvider min, IntProvider max) {
        return new PlantopiaRelativeIntProportion(ratio, Optional.of(min), Optional.of(max));
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PlantopiaFixedIntProportion fixed(IntProvider value) {
        return new PlantopiaFixedIntProportion(value);
    }
}
