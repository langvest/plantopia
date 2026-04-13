package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaRangeFilter extends PlacementFilter {
    public static final Codec<PlantopiaRangeFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlantopiaVerticalAnchor.CODEC.fieldOf("top_anchor").forGetter(it -> it.topAnchor),
        PlantopiaVerticalAnchor.CODEC.fieldOf("bottom_anchor").forGetter(it -> it.bottomAnchor)
    ).apply(instance, PlantopiaRangeFilter::new));

    private final PlantopiaVerticalAnchor topAnchor;
    private final PlantopiaVerticalAnchor bottomAnchor;

    private PlantopiaRangeFilter(PlantopiaVerticalAnchor topAnchor, PlantopiaVerticalAnchor bottomAnchor) {
        this.topAnchor = topAnchor;
        this.bottomAnchor = bottomAnchor;
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaRangeFilter between(PlantopiaVerticalAnchor topAnchor, PlantopiaVerticalAnchor bottomAnchor) {
        return new PlantopiaRangeFilter(topAnchor, bottomAnchor);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaRangeFilter above(PlantopiaVerticalAnchor anchor) {
        return new PlantopiaRangeFilter(PlantopiaVerticalAnchor.top(), anchor);
    }

    @Contract("_ -> new")
    public static @NotNull PlantopiaRangeFilter below(PlantopiaVerticalAnchor anchor) {
        return new PlantopiaRangeFilter(anchor, PlantopiaVerticalAnchor.bottom());
    }

    @Override
    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        var level = context.getLevel();
        int candidateY = pos.getY();
        int maxY = topAnchor.resolveY(context, level, pos);
        int minY = bottomAnchor.resolveY(context, level, pos);

        return candidateY >= minY && candidateY <= maxY;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.RANGE_FILTER.get();
    }
}
