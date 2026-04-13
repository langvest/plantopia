package by.langvest.plantopia.worldgen.placement.verticalanchor;

import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorType;
import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHeightmapVerticalAnchor extends PlantopiaVerticalAnchor {
    public static final Codec<PlantopiaHeightmapVerticalAnchor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Heightmap.Types.CODEC.fieldOf("heightmap").forGetter(it -> it.heightmap),
        Codec.INT.fieldOf("offset").orElse(0).forGetter(it -> it.offset)
    ).apply(instance, PlantopiaHeightmapVerticalAnchor::new));

    private final Heightmap.Types heightmap;
    private final int offset;

    private PlantopiaHeightmapVerticalAnchor(Heightmap.Types heightmap, int offset) {
        this.heightmap = heightmap;
        this.offset = offset;
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor of(Heightmap.Types heightmap, int offset) {
        return new  PlantopiaHeightmapVerticalAnchor(heightmap, offset);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaHeightmapVerticalAnchor of(Heightmap.Types heightmap) {
        return new  PlantopiaHeightmapVerticalAnchor(heightmap, 0);
    }

    @Override
    protected PlantopiaVerticalAnchorType<?> type() {
        return PlantopiaVerticalAnchorTypes.HEIGHTMAP.get();
    }

    @Override
    public int resolveY(@NotNull WorldGenerationContext context, @NotNull WorldGenLevel level, int x, int z) {
        return bound(context, level.getHeight(heightmap, x, z) + offset);
    }
}
