package by.langvest.plantopia.worldgen.util;

import by.langvest.plantopia.worldgen.util.floatprovider.PlantopiaWeightedListFloat;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class PlantopiaProviderUtils {
    @Contract(pure = true)
    public static @NotNull BlockStateProvider simpleProvider(Block block) {
        return BlockStateProvider.simple(block);
    }

    @Contract(pure = true)
    public static @NotNull BlockStateProvider simpleProvider(BlockState state) {
        return BlockStateProvider.simple(state);
    }

    @Contract(pure = true)
    public static @NotNull WeightedStateProvider weightedProvider(@NotNull Function<SimpleWeightedRandomList.Builder<BlockState>, SimpleWeightedRandomList.Builder<BlockState>> states) {
        return new WeightedStateProvider(states.apply(SimpleWeightedRandomList.builder()));
    }

    @Contract(pure = true)
    public static @NotNull WeightedListInt weightedListInt(@NotNull Function<SimpleWeightedRandomList.Builder<IntProvider>, SimpleWeightedRandomList.Builder<IntProvider>> values) {
        return new WeightedListInt(values.apply(SimpleWeightedRandomList.builder()).build());
    }

    @Contract(pure = true)
    public static @NotNull PlantopiaWeightedListFloat weightedListFloat(@NotNull Function<SimpleWeightedRandomList.Builder<FloatProvider>, SimpleWeightedRandomList.Builder<FloatProvider>> values) {
        return new PlantopiaWeightedListFloat(values.apply(SimpleWeightedRandomList.builder()).build());
    }
}
