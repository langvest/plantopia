package by.langvest.plantopia.worldgen.placement;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PlantopiaMultiNoiseConfig(List<Entry> entries) {
    public static final Codec<PlantopiaMultiNoiseConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Entry.CODEC.listOf().fieldOf("entries").forGetter(it -> it.entries)
    ).apply(instance, PlantopiaMultiNoiseConfig::new));

    public PlantopiaMultiNoiseConfig(@NotNull List<Entry> entries) {
        this.entries = entries;

        if (entries.isEmpty()) {
            throw new IllegalArgumentException("Plantopia multi-noise entries cannot be empty!");
        }
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaMultiNoiseConfig of(Entry... entries) {
        return new PlantopiaMultiNoiseConfig(List.of(entries));
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public double getValue(@NotNull BlockPos pos) {
        return getValue(pos.getX(), pos.getZ());
    }

    public double getValue(double x, double z) {
        double sum = 0;

        for (var entry : entries) {
            double value = entry.noiseConfig.getValue(x, z);
            sum += value * entry.weight;
        }

        return Mth.clamp(sum, -1.0, 1.0);
    }

    public record Entry(PlantopiaNoiseConfig noiseConfig, double weight) {
        public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlantopiaNoiseConfig.CODEC.fieldOf("noise_config").forGetter(it -> it.noiseConfig),
            Codec.DOUBLE.fieldOf("weight").forGetter(it -> it.weight)
        ).apply(instance, Entry::new));
    }

    public static class Builder {
        private final List<Entry> entries = Lists.newArrayList();

        public Builder add(PlantopiaNoiseConfig config, double weight) {
            this.entries.add(new Entry(config, weight));
            return this;
        }

        @Contract(" -> new")
        public PlantopiaMultiNoiseConfig build() {
            return new PlantopiaMultiNoiseConfig(entries);
        }
    }
}
