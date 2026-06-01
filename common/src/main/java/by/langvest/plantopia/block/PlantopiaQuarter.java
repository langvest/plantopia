package by.langvest.plantopia.block;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PlantopiaQuarter implements StringRepresentable {
    SOUTH_WEST(Direction.SOUTH, Direction.WEST),
    WEST_NORTH(Direction.WEST, Direction.NORTH),
    NORTH_EAST(Direction.NORTH, Direction.EAST),
    EAST_SOUTH(Direction.EAST, Direction.SOUTH);

    private final Direction leftDirection;
    private final Direction rightDirection;

    PlantopiaQuarter(Direction leftDirection, Direction rightDirection) {
        this.leftDirection = leftDirection;
        this.rightDirection = rightDirection;
    }

    public Direction getLeftDirection() {
        return leftDirection;
    }

    public Direction getRightDirection() {
        return rightDirection;
    }

    public @NotNull String toString() {
        return getSerializedName();
    }

    public @NotNull String getSerializedName() {
        return switch (this) {
            case SOUTH_WEST -> "south_west";
            case WEST_NORTH -> "west_north";
            case NORTH_EAST -> "north_east";
            case EAST_SOUTH -> "east_south";
        };
    }
}
