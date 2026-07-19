package by.langvest.plantopia.worldgen.util;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaDipType implements StringRepresentable {
    SURFACE_ONLY("SURFACE_ONLY"),
    SURFACE_OR_CAVE("SURFACE_OR_CAVE"),
    CAVE_ONLY("CAVE_ONLY");

    public static final Codec<PlantopiaDipType> CODEC = StringRepresentable.fromEnum(PlantopiaDipType::values);
    private final String serializationKey;

    PlantopiaDipType(String serializationKey) {
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
