package by.langvest.plantopia.block;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class PlantopiaHogweedUtils {
    public static final IntegerProperty AGE = PlantopiaBlockStateProperties.INFESTED_AGE;
    public static final int MAX_AGE = AGE.max;
    public static final int MIN_AGE = AGE.min;

    public static Block getDirtBlock() {
        return PlantopiaBlocks.INFESTED_DIRT.get();
    }

    public static Block getGrassBlock() {
        return PlantopiaBlocks.INFESTED_GRASS_BLOCK.get();
    }

    public static BlockState getGrassState(int age) {
        return getGrassBlock().defaultBlockState()
            .setValue(AGE, age);
    }

    public static BlockState getDirtState(int age) {
        return getDirtBlock().defaultBlockState()
            .setValue(AGE, age);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isInfestedBlock(BlockState state) {
        return state.is(getDirtBlock()) || state.is(getGrassBlock());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canBeInfested(BlockState state) {
        return state.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO) || state.is(Blocks.GRASS_BLOCK);
    }

    @Nullable
    public static BlockState getInfestedState(BlockState state) {
        return getInfestedState(state, MIN_AGE);
    }

    @Nullable
    public static BlockState getInfestedState(BlockState healthyState, int age) {
        if (healthyState.is(Blocks.GRASS_BLOCK)) {
            return getGrassBlock().defaultBlockState()
                .setValue(AGE, age);
        }

        if (healthyState.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
            return getDirtBlock().defaultBlockState()
                .setValue(AGE, age);
        }

        return null;
    }

    public static BlockState rollupInfestedState(BlockState healthyState, BlockState infestedState) {
        if (healthyState.is(Blocks.FARMLAND) || healthyState.is(Blocks.DIRT_PATH)) {
            return getGrassBlock().defaultBlockState()
                .setValue(AGE, infestedState.getValue(AGE));
        }

        return infestedState;
    }

    public static int increaseAge(RandomSource random, int currentAge) {
        var inc = 1;

        if (random.nextInt(3) == 0) inc++;
        if (random.nextInt(3) == 0) inc++;

        return Math.min(currentAge + inc, MAX_AGE);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static boolean resetInfestedBlock(Level level, BlockPos pos) {
        return resetInfestedBlock(level, pos, MIN_AGE);
    }

    public static boolean resetInfestedBlock(Level level, BlockPos pos, int age) {
        var state = level.getBlockState(pos);

        if (!isInfestedBlock(state)) return false;

        return level.setBlock(pos, state.setValue(AGE, age), 54);
    }
}
