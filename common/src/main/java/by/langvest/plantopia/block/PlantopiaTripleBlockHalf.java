package by.langvest.plantopia.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaTripleBlockHalf implements StringRepresentable {
    UPPER,
    CENTRAL,
    LOWER;

    public @NotNull String toString() {
        return getSerializedName();
    }

    public @NotNull String getSerializedName() {
        return switch (this) {
            case UPPER -> "upper";
            case CENTRAL -> "central";
            case LOWER -> "lower";
        };
    }
}
