package by.langvest.plantopia.worldgen.placement.special;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaNoiseActivationType implements StringRepresentable {
    ABOVE("ABOVE"),
    BELOW("BELOW");

    public static final Codec<PlantopiaNoiseActivationType> CODEC = StringRepresentable.fromEnum(PlantopiaNoiseActivationType::values);
    private final String serializationKey;

    PlantopiaNoiseActivationType(String serializationKey) {
        this.serializationKey = serializationKey;
    }

    public String getSerializationKey() {
        return serializationKey;
    }

    @Override
    public @NotNull String getSerializedName() {
        return serializationKey;
    }
}
