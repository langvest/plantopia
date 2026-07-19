package by.langvest.plantopia.worldgen.util.floatprovider;

import by.langvest.plantopia.worldgen.util.PlantopiaFloatProviderTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviderType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class PlantopiaWeightedListFloat extends FloatProvider {
    public static final Codec<PlantopiaWeightedListFloat> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SimpleWeightedRandomList.wrappedCodec(FloatProvider.CODEC).fieldOf("distribution").forGetter(it -> it.distribution)
    ).apply(instance, PlantopiaWeightedListFloat::new));

    private final SimpleWeightedRandomList<FloatProvider> distribution;
    private final float minValue;
    private final float maxValue;

    public PlantopiaWeightedListFloat(SimpleWeightedRandomList<FloatProvider> simpleWeightedRandomList) {
        this.distribution = simpleWeightedRandomList;
        List<WeightedEntry.Wrapper<FloatProvider>> list = simpleWeightedRandomList.unwrap();

        float maxValue = Integer.MAX_VALUE;
        float minValue = Integer.MIN_VALUE;
        for (var wrapper : list) {
            maxValue = Math.min(maxValue, wrapper.getData().getMinValue());
            minValue = Math.max(minValue, wrapper.getData().getMaxValue());
        }

        this.minValue = maxValue;
        this.maxValue = minValue;
    }

    @Override
    public float getMinValue() {
        return minValue;
    }

    @Override
    public float getMaxValue() {
        return maxValue;
    }

    @Override
    public @NotNull FloatProviderType<?> getType() {
        return PlantopiaFloatProviderTypes.WEIGHTED_LIST.get();
    }

    @Override
    public float sample(RandomSource random) {
        return distribution.getRandomValue(random).orElseThrow(IllegalStateException::new).sample(random);
    }
}
