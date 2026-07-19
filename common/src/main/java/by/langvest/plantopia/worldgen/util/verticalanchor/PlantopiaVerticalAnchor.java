package by.langvest.plantopia.worldgen.util.verticalanchor;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.util.PlantopiaVerticalAnchorType;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PlantopiaVerticalAnchor {
    public static final Codec<PlantopiaVerticalAnchor> CODEC = PlantopiaRegistries.VERTICAL_ANCHOR_TYPE.byNameCodec().dispatch(PlantopiaVerticalAnchor::type, PlantopiaVerticalAnchorType::codec);
    protected static final int SEA_LEVEL = 63;

    public abstract PlantopiaVerticalAnchorType<?> type();

    public int resolveY(WorldGenerationContext context, WorldGenLevel level, BlockPos pos) {
        return resolveY(context, level, pos.getX(), pos.getZ());
    }

    public abstract int resolveY(WorldGenerationContext context, WorldGenLevel level, int x, int z);

    protected static int bound(WorldGenerationContext context, int y) {
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

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor heightmap(Heightmap.Types heightmap) {
        return PlantopiaHeightmapVerticalAnchor.of(heightmap);
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor heightmap(Heightmap.Types heightmap, int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(heightmap, offset);
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlocking() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlocking(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING, offset);
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlockingNoLeaves() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor motionBlockingNoLeaves(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, offset);
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloor() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloor(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR, offset);
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloorWg() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR_WG);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor oceanFloorWg(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.OCEAN_FLOOR_WG, offset);
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurface() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurface(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE, offset);
    }

    @Contract(" -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurfaceWg() {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE_WG);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor worldSurfaceWg(int offset) {
        return PlantopiaHeightmapVerticalAnchor.of(Heightmap.Types.WORLD_SURFACE_WG, offset);
    }
}
