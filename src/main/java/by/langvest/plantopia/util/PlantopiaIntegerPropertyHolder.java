package by.langvest.plantopia.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record PlantopiaIntegerPropertyHolder(String name, int min, int max) {
    public static final Codec<PlantopiaIntegerPropertyHolder> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        Codec.STRING.fieldOf("Name").forGetter(PlantopiaIntegerPropertyHolder::name),
        ExtraCodecs.POSITIVE_INT.fieldOf("Min").forGetter(PlantopiaIntegerPropertyHolder::min),
        ExtraCodecs.POSITIVE_INT.fieldOf("Max").forGetter(PlantopiaIntegerPropertyHolder::max)
    ).apply(instance, PlantopiaIntegerPropertyHolder::new));

    @Contract("_ -> new")
    public static @NotNull PlantopiaIntegerPropertyHolder of(@NotNull IntegerProperty property) {
        return new PlantopiaIntegerPropertyHolder(property.getName(), property.min, property.max);
    }
}
