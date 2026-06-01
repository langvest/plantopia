package by.langvest.plantopia.util.helper;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class PlantopiaShapeHelper {
    private PlantopiaShapeHelper() {}

    public static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        if (direction == Direction.NORTH) {
            return shape;
        }

        var result = Shapes.empty();

        for (AABB box : shape.toAabbs()) {
            result = Shapes.or(result, rotateBox(box, direction));
        }

        return result;
    }

    private static @NotNull VoxelShape rotateBox(AABB box, Direction direction) {
        return switch (direction) {
            case EAST -> Shapes.box(1 - box.maxZ, box.minY, box.minX, 1 - box.minZ, box.maxY, box.maxX);
            case SOUTH -> Shapes.box(1 - box.maxX, box.minY, 1 - box.maxZ, 1 - box.minX, box.maxY, 1 - box.minZ);
            case WEST -> Shapes.box(box.minZ, box.minY, 1 - box.maxX, box.maxZ, box.maxY, 1 - box.minX);
            default -> Shapes.box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
        };
    }

    public static VoxelShape orientShape(VoxelShape shape, Direction direction) {
        if (direction == Direction.UP) {
            return shape;
        }

        var result = Shapes.empty();

        for (AABB box : shape.toAabbs()) {
            result = Shapes.or(result, orientBox(box, direction));
        }

        return result;
    }

    private static @NotNull VoxelShape orientBox(AABB box, Direction direction) {
        return switch (direction) {
            case DOWN -> Shapes.box(box.minX, 1 - box.maxY, box.minZ, box.maxX, 1 - box.minY, box.maxZ);
            case NORTH -> Shapes.box(box.minX, box.minZ, 1 - box.maxY, box.maxX, box.maxZ, 1 - box.minY);
            case SOUTH -> Shapes.box(box.minX, 1 - box.maxZ, box.minY, box.maxX, 1 - box.minZ, box.maxY);
            case WEST -> Shapes.box(1 - box.maxY, box.minX, box.minZ, 1 - box.minY, box.maxX, box.maxZ);
            case EAST -> Shapes.box(box.minY, 1 - box.maxX, box.minZ, box.maxY, 1 - box.minX, box.maxZ);
            default -> Shapes.box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
        };
    }

    @Contract(pure = true)
    public static @NotNull VoxelShape column(double size, double y1, double y2) {
        return column(size, size, y1, y2);
    }

    public static @NotNull VoxelShape column(double xSize, double zSize, double y1, double y2) {
        double d0 = xSize / 2.0;
        double d1 = zSize / 2.0;
        return Block.box(8.0 - d0, y1, 8.0 - d1, 8.0 + d0, y2, 8.0 + d1);
    }
}
