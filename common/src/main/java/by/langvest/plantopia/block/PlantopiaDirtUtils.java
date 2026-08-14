package by.langvest.plantopia.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class PlantopiaDirtUtils {
    public static boolean canConvertToDirt(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.MYCELIUM);
    }

    public static BlockState getCorrespondingState(BlockState prevState, BlockState nextState) {
        return nextState;
    }
}
