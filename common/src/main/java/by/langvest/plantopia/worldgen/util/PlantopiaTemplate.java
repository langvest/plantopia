package by.langvest.plantopia.worldgen.util;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PlantopiaTemplate {
    boolean test(RandomSource random, int dx, int dz, int range);

    /* TEMPLATES **********************************************************************************/

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate square() {
        return square(1.0F);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate square(float scale) {
        return (random, dx, dz, range) -> {
            if (scale < 0) return false;
            float radius = range * scale;
            return Math.abs(dx) <= radius && Math.abs(dz) <= radius;
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate circle() {
        return circle(1.0F);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate circle(float scale) {
        return (random, dx, dz, range) -> {
            if (scale < 0) return false;
            float radius = range * scale;
            return (dx * dx + dz * dz) <= (radius * radius);
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate cross() {
        return cross(0);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate cross(int thickness) {
        return (random, dx, dz, range) -> Math.abs(dx) <= thickness || Math.abs(dz) <= thickness;
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate outline() {
        return outline(1);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate outline(int thickness) {
        return (random, dx, dz, range) -> {
            if (thickness <= 0) return false;
            return Math.abs(dx) > range - thickness || Math.abs(dz) > range - thickness;
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate corner() {
        return corner(1);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate corner(int inset) {
        return (random, dx, dz, range) -> {
            if (inset <= 0) return false;
            return Math.abs(dx) > range - inset && Math.abs(dz) > range - inset;
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate withChance(float chance) {
        return (random, dx, dz, range) -> random.nextFloat() < chance;
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate not(PlantopiaTemplate template) {
        return (random, dx, dz, range) -> !template.test(random, dx, dz, range);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate anyOf(PlantopiaTemplate... templates) {
        return (random, dx, dz, range) -> {
            for (PlantopiaTemplate template : templates) {
                if (template.test(random, dx, dz, range)) {
                    return true;
                }
            }
            return false;
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate allOf(PlantopiaTemplate... templates) {
        return (random, dx, dz, range) -> {
            for (PlantopiaTemplate template : templates) {
                if (!template.test(random, dx, dz, range)) {
                    return false;
                }
            }
            return true;
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate noCorner() {
        return not(corner());
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate noCorner(int inset) {
        return not(corner(inset));
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate noOutline() {
        return not(outline());
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate noOutline(int thickness) {
        return not(outline(thickness));
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate withRangeOffset(int offset, PlantopiaTemplate template) {
        return (random, dx, dz, range) -> {
            int newRange = range + offset;
            if (newRange < 0) return false;
            if (Math.abs(dx) > newRange || Math.abs(dz) > newRange) return false;
            return template.test(random, dx, dz, newRange);
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate withRangeFactor(float factor, PlantopiaTemplate template) {
        return (random, dx, dz, range) -> {
            int newRange = (int) (range * factor);
            if (newRange < 0) return false;
            if (Math.abs(dx) > newRange || Math.abs(dz) > newRange) return false;
            return template.test(random, dx, dz, newRange);
        };
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate octagonalOutline() {
        return octagonalOutline(1, 1);
    }

    @Contract(pure = true)
    static @NotNull PlantopiaTemplate octagonalOutline(int inset, int thickness) {
        return allOf(noCorner(inset), not(withRangeOffset(-thickness, noCorner(inset))));
    }
}
