package by.langvest.plantopia.worldgen.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaThresholdType implements StringRepresentable {
    ABOVE("ABOVE"),
    BELOW("BELOW");

    public static final Codec<PlantopiaThresholdType> CODEC = StringRepresentable.fromEnum(PlantopiaThresholdType::values);
    private final String serializationKey;

    PlantopiaThresholdType(String serializationKey) {
        this.serializationKey = serializationKey;
    }

    public String getSerializationKey() {
        return serializationKey;
    }

    @Override
    public @NotNull String getSerializedName() {
        return getSerializationKey();
    }

    public boolean isAbove() {
        return this == ABOVE;
    }

    public boolean isBelow() {
        return this == BELOW;
    }
}
