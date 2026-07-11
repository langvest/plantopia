package by.langvest.plantopia.worldgen.feature.foliageplacer;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PlantopiaFoliagePlacer extends FoliagePlacer {
    public PlantopiaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    protected void placeRow(LevelSimulatedReader level, FoliageSetter foliageSetter, RandomSource random, TreeConfiguration config, FoliageAttachment attachment, int range, int dy, Template template) {
        int i = attachment.doubleTrunk() ? 1 : 0;
        var mutablePos = new BlockPos.MutableBlockPos();

        for (int dx = -range; dx <= range + i; dx++) {
            for (int dz = -range; dz <= range + i; dz++) {
                if (template.shouldPlace(random, dx, dy, dz, range, attachment.doubleTrunk())) {
                    mutablePos.setWithOffset(attachment.pos(), dx, dy, dz);
                    tryPlaceLeaf(level, foliageSetter, random, config, mutablePos);
                }
            }
        }
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int dy, int dz, int range, boolean large) {
        return false;
    }

    @FunctionalInterface
    public interface Template {
        boolean shouldPlace(RandomSource random, int dx, int dy, int dz, int range, boolean large);
    }

    // --- Primitives ---

    @Contract(pure = true)
    public static @NotNull Template square() {
        return square(1.0F);
    }

    @Contract(pure = true)
    public static @NotNull Template square(float scale) {
        return (random, dx, dy, dz, range, large) -> {
            if (scale < 0) return false;
            float radius = range * scale;
            return Math.abs(dx) <= radius && Math.abs(dz) <= radius;
        };
    }

    @Contract(pure = true)
    public static @NotNull Template circle() {
        return circle(1.0F);
    }

    @Contract(pure = true)
    public static @NotNull Template circle(float scale) {
        return (random, dx, dy, dz, range, large) -> {
            if (scale < 0) return false;
            float radius = range * scale;
            return (dx * dx + dz * dz) <= (radius * radius);
        };
    }

    @Contract(pure = true)
    public static @NotNull Template cross() {
        return cross(0);
    }

    @Contract(pure = true)
    public static @NotNull Template cross(int thickness) {
        return (random, dx, dy, dz, range, large) -> Math.abs(dx) <= thickness || Math.abs(dz) <= thickness;
    }

    @Contract(pure = true)
    public static @NotNull Template outline() {
        return outline(1);
    }

    @Contract(pure = true)
    public static @NotNull Template outline(int size) {
        return (random, dx, dy, dz, range, large) -> {
            if (size <= 0) return false;
            return Math.abs(dx) > range - size || Math.abs(dz) > range - size;
        };
    }

    @Contract(pure = true)
    public static @NotNull Template corner() {
        return corner(1);
    }

    @Contract(pure = true)
    public static @NotNull Template corner(int size) {
        return (random, dx, dy, dz, range, large) -> {
            if (size <= 0) return false;
            return Math.abs(dx) > range - size && Math.abs(dz) > range - size;
        };
    }

    @Contract(pure = true)
    public static @NotNull Template withChance(float chance) {
        return (random, dx, dy, dz, range, large) -> random.nextFloat() < chance;
    }

    @Contract(pure = true)
    public static @NotNull Template not(Template template) {
        return (random, dx, dy, dz, range, large) -> !template.shouldPlace(random, dx, dy, dz, range, large);
    }

    @Contract(pure = true)
    public static @NotNull Template anyOf(Template... templates) {
        return (random, dx, dy, dz, range, large) -> {
            for (Template template : templates) {
                if (template.shouldPlace(random, dx, dy, dz, range, large)) {
                    return true;
                }
            }
            return false;
        };
    }

    @Contract(pure = true)
    public static @NotNull Template allOf(Template... templates) {
        return (random, dx, dy, dz, range, large) -> {
            for (Template template : templates) {
                if (!template.shouldPlace(random, dx, dy, dz, range, large)) {
                    return false;
                }
            }
            return true;
        };
    }

    @Contract(pure = true)
    public static @NotNull Template noCorner() {
        return not(corner());
    }

    @Contract(pure = true)
    public static @NotNull Template noCorner(int size) {
        return not(corner(size));
    }

    @Contract(pure = true)
    public static @NotNull Template noOutline() {
        return not(outline());
    }

    @Contract(pure = true)
    public static @NotNull Template noOutline(int size) {
        return not(outline(size));
    }
}
