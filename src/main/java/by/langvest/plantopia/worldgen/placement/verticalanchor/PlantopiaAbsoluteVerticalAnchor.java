package by.langvest.plantopia.worldgen.placement.verticalanchor;

import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorType;
import by.langvest.plantopia.worldgen.placement.PlantopiaVerticalAnchorTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaAbsoluteVerticalAnchor extends PlantopiaVerticalAnchor {
    public static final Codec<PlantopiaAbsoluteVerticalAnchor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        VerticalAnchor.CODEC.fieldOf("anchor").forGetter(it -> it.anchor)
    ).apply(instance, PlantopiaAbsoluteVerticalAnchor::new));

    private final VerticalAnchor anchor;

    private PlantopiaAbsoluteVerticalAnchor(VerticalAnchor anchor) {
        this.anchor = anchor;
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaAbsoluteVerticalAnchor of(VerticalAnchor anchor) {
        return new PlantopiaAbsoluteVerticalAnchor(anchor);
    }

    @Override
    protected PlantopiaVerticalAnchorType<?> type() {
        return PlantopiaVerticalAnchorTypes.ABSOLUTE.get();
    }

    @Override
    public int resolveY(@NotNull WorldGenerationContext context, @NotNull WorldGenLevel level, int x, int z) {
        return anchor.resolveY(context);
    }
}
