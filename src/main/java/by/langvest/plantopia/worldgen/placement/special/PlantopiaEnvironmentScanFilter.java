package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaEnvironmentScanFilter extends PlacementFilter {
    public static final Codec<PlantopiaEnvironmentScanFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Direction.VERTICAL_CODEC.fieldOf("direction_of_search").forGetter(it -> it.directionOfSearch),
        BlockPredicate.CODEC.fieldOf("target_condition").forGetter(it -> it.targetCondition),
        BlockPredicate.CODEC.optionalFieldOf("allowed_search_condition", BlockPredicate.alwaysTrue()).forGetter(it -> it.allowedSearchCondition),
        Codec.intRange(1, 32).fieldOf("max_steps").forGetter(it -> it.maxSteps)
    ).apply(instance, PlantopiaEnvironmentScanFilter::new));

    private final Direction directionOfSearch;
    private final BlockPredicate targetCondition;
    private final BlockPredicate allowedSearchCondition;
    private final int maxSteps;

    private PlantopiaEnvironmentScanFilter(Direction directionOfSearch, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) {
        this.directionOfSearch = directionOfSearch;
        this.targetCondition = targetCondition;
        this.allowedSearchCondition = allowedSearchCondition;
        this.maxSteps = maxSteps;
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull PlantopiaEnvironmentScanFilter scanningFor(Direction directionOfSearch, BlockPredicate targetCondition, BlockPredicate allowedSearchCondition, int maxSteps) {
        return new PlantopiaEnvironmentScanFilter(directionOfSearch, targetCondition, allowedSearchCondition, maxSteps);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull PlantopiaEnvironmentScanFilter scanningFor(Direction directionOfSearch, BlockPredicate targetCondition, int maxSteps) {
        return scanningFor(directionOfSearch, targetCondition, BlockPredicate.alwaysTrue(), maxSteps);
    }

    @Override
    protected boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        var mutablePos = pos.mutable();
        var level = context.getLevel();

        if (!allowedSearchCondition.test(level, mutablePos)) {
            return false;
        }

        for (int i = 0; i < maxSteps; i++) {
            if (targetCondition.test(level, mutablePos)) {
                return true;
            }

            mutablePos.move(directionOfSearch);

            if (level.isOutsideBuildHeight(mutablePos.getY())) {
                return false;
            }

            if (!allowedSearchCondition.test(level, mutablePos)) {
                break;
            }
        }

        return targetCondition.test(level, mutablePos);
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.ENVIRONMENT_SCAN_FILTER.get();
    }
}
