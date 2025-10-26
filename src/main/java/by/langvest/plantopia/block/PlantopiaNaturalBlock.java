package by.langvest.plantopia.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public interface PlantopiaNaturalBlock {
    boolean canPlaceNaturallyAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull RandomSource random);

    void placeNaturallyAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, int flags);
}
