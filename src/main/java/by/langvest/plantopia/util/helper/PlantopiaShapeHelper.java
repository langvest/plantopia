package by.langvest.plantopia.util.helper;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaShapeHelper {
	public static VoxelShape rotateShape(@NotNull VoxelShape shape, Direction direction) {
		VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

		for(AABB box : shape.toAabbs()) {
			buffer[1] = Shapes.or(buffer[1], rotateBox(box, direction));
		}

		return buffer[1];
	}

	private static @NotNull VoxelShape rotateBox(@NotNull AABB box, @NotNull Direction direction) {
		double top = box.minZ;
		double left = box.minX;
		double dx = box.maxX - box.minX;
		double dz = box.maxZ - box.minZ;

		return switch(direction) {
			case EAST -> Shapes.box(1 - top - dz, box.minY, left, 1 - top, box.maxY, left + dx);
			case SOUTH -> Shapes.box(1 - left - dx, box.minY, 1 - top - dz, 1 - left, box.maxY, 1 - top);
			case WEST -> Shapes.box(top, box.minY, 1 - left - dx, top + dz, box.maxY, 1 - left);
			default -> Shapes.box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
		};
	}
}
