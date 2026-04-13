package by.langvest.plantopia.worldgen.placement.verticalanchor;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorType;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class PlantopiaVerticalAnchor {
    public static final Codec<PlantopiaVerticalAnchor> CODEC = PlantopiaRegistries.VERTICAL_ANCHOR_TYPE.byNameCodec().dispatch(PlantopiaVerticalAnchor::type, PlantopiaVerticalAnchorType::codec);
    protected static final int SEA_LEVEL = 63;

    protected abstract PlantopiaVerticalAnchorType<?> type();

    public int resolveY(@NotNull WorldGenerationContext context, @NotNull WorldGenLevel level, @NotNull BlockPos pos) {
        return resolveY(context, level, pos.getX(), pos.getZ());
    }

    public abstract int resolveY(@NotNull WorldGenerationContext context, @NotNull WorldGenLevel level, int x, int z);

    protected static int bound(@NotNull WorldGenerationContext context, int y) {
        return Mth.clamp(y, context.getMinGenY(), context.getMinGenY() + context.getGenDepth() - 1);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor absolute(int y) {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.absolute(y));
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor seaLevel() {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.absolute(SEA_LEVEL));
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor seaLevel(int offset) {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.absolute(SEA_LEVEL + offset));
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor aboveBottom(int offset) {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.aboveBottom(offset));
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor belowTop(int offset) {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.belowTop(offset));
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor bottom() {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.bottom());
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor top() {
        return PlantopiaAbsoluteVerticalAnchor.of(VerticalAnchor.top());
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor heightmap(Heightmap.Types heightmap) {
        return PlantopiaHeightmapVerticalAnchor.of(heightmap);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor heightmap(Heightmap.Types heightmap, int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(heightmap, offset);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlocking() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlocking(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING, offset);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlockingNoLeaves() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlockingNoLeaves(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, offset);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloor() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloor(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR, offset);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloorWg() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR_WG);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloorWg(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR_WG, offset);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurface() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurface(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE, offset);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurfaceWg() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE_WG);
    }

    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurfaceWg(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE_WG, offset);
    }
}
