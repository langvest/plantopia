package by.langvest.plantopia.worldgen.util.intproportion;

import by.langvest.plantopia.worldgen.util.PlantopiaIntProportionType;
import by.langvest.plantopia.worldgen.util.PlantopiaIntProportionTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class PlantopiaFixedIntProportion extends PlantopiaIntProportion {
    public static final Codec<PlantopiaFixedIntProportion> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        IntProvider.CODEC.fieldOf("value").forGetter(it -> it.value)
    ).apply(instance, PlantopiaFixedIntProportion::new));

    protected final IntProvider value;

    protected PlantopiaFixedIntProportion(IntProvider value) {
        this.value = value;
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull PlantopiaFixedIntProportion of(IntProvider value) {
        return new PlantopiaFixedIntProportion(value);
    }

    @Override
    public PlantopiaIntProportionType<?> type() {
        return PlantopiaIntProportionTypes.FIXED.get();
    }

    @Override
    public int sample(RandomSource random, int baseValue, Function<Integer, Integer> refiner) {
        return refiner.apply(value.sample(random));
    }
}
