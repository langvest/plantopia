package by.langvest.plantopia.worldgen.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaSelectionType implements StringRepresentable {
    INCLUDE("INCLUDE"),
    EXCLUDE("EXCLUDE");

    public static final Codec<PlantopiaSelectionType> CODEC = StringRepresentable.fromEnum(PlantopiaSelectionType::values);
    private final String serializationKey;

    PlantopiaSelectionType(String serializationKey) {
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
