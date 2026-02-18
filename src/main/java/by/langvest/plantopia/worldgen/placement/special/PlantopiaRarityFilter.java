package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

public class PlantopiaRarityFilter extends PlacementFilter {
    public static final Codec<PlantopiaRarityFilter> CODEC = FloatProvider.codec(1.0F, Float.MAX_VALUE)
        .fieldOf("chance")
        .xmap(PlantopiaRarityFilter::new, p_191907_ -> p_191907_.chance)
        .codec();

    private final FloatProvider chance;

    private PlantopiaRarityFilter(FloatProvider chance) {
        this.chance = chance;
    }

    public static @NotNull PlantopiaRarityFilter onAverageOnceEvery(float minInclusive, float maxExclusive) {
        return new PlantopiaRarityFilter(UniformFloat.of(minInclusive, maxExclusive));
    }

    public static @NotNull PlantopiaRarityFilter onAverageOnceEvery(float chance) {
        return new PlantopiaRarityFilter(ConstantFloat.of(chance));
    }

    public static @NotNull PlantopiaRarityFilter onAverageOnceEvery(FloatProvider chance) {
        return new PlantopiaRarityFilter(chance);
    }

    @Override
    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        return random.nextFloat() < (1.0F / chance.sample(random));
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.RARITY_FILTER.get();
    }
}
