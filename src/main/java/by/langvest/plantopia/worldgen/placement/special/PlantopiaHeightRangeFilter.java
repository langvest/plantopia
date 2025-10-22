package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHeightRangeFilter extends PlacementFilter {
    public static final Codec<PlantopiaHeightRangeFilter> CODEC = RecordCodecBuilder.create((p_191679_) -> p_191679_.group(
        VerticalAnchor.CODEC.fieldOf("min_inclusive").forGetter((p_161941_) -> p_161941_.minInclusive),
        VerticalAnchor.CODEC.fieldOf("max_inclusive").forGetter((p_161941_) -> p_161941_.maxInclusive)
    ).apply(p_191679_, PlantopiaHeightRangeFilter::new));

    private final VerticalAnchor minInclusive;
    private final VerticalAnchor maxInclusive;

    private PlantopiaHeightRangeFilter(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    @Contract("_, _ -> new")
    public static @NotNull PlantopiaHeightRangeFilter uniform(VerticalAnchor minInclusive, VerticalAnchor maxInclusive) {
        return new PlantopiaHeightRangeFilter(minInclusive, maxInclusive);
    }

    @Override
    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        int candidateY = pos.getY();
        int minY = minInclusive.resolveY(context);
        int maxY = maxInclusive.resolveY(context);

        if(candidateY < minY) return false;
        if(candidateY > maxY) return false;

        return true;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.HEIGHT_RANGE_FILTER;
    }
}
