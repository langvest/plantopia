package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public interface PlantopiaNaturalBlock {
    boolean placeNaturally(PlaceContext context);

    record PlaceContext(
        WorldGenLevel level,
        BlockPos origin,
        BlockState state,
        int flags,
        RandomSource random,
        Predicate<BlockPos> invadePredicate,
        Predicate<BlockPos> spreadPredicate
    ) {
        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean canInvadeInto(BlockPos pos) {
            return invadePredicate.test(pos);
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean canSpreadInto(BlockPos pos) {
            return spreadPredicate.test(pos);
        }
    }
}
