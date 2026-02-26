package by.langvest.plantopia.worldgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaThresholdActivationType implements StringRepresentable {
    ABOVE("ABOVE"),
    BELOW("BELOW");

    public static final Codec<PlantopiaThresholdActivationType> CODEC = StringRepresentable.fromEnum(PlantopiaThresholdActivationType::values);
    private final String serializationKey;

    PlantopiaThresholdActivationType(String serializationKey) {
        this.serializationKey = serializationKey;
    }

    public String getSerializationKey() {
        return serializationKey;
    }

    @Override
    public @NotNull String getSerializedName() {
        return getSerializationKey();
    }
}
