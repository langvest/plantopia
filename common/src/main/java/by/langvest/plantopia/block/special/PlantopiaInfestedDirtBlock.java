package by.langvest.plantopia.block.special;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.block.PlantopiaHogweedUtils.*;
import static by.langvest.plantopia.util.helper.PlantopiaBlockHelper.copySnowyAboveFrom;

@ParametersAreNonnullByDefault
public class PlantopiaInfestedDirtBlock extends Block {
    public PlantopiaInfestedDirtBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AGE, MIN_AGE));
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isLoaded(pos)) {
            return; // LanGvest: Prevent loading unloaded chunks when checking neighbor's light and spreading.
        }

        for (int i = 0; i < 4; i++) {
            var candidatePos = pos.offset(PlantopiaMathHelper.getRandomOffsetInArea(random, 1));
            var candidateState = level.getBlockState(candidatePos);
            int currentAge = state.getValue(AGE);
            boolean isAgeCanSpread = currentAge < MAX_AGE;
            boolean isCandidateCloseNeighbour = PlantopiaMathHelper.isCloseNeighbours(pos, candidatePos);

            if (isAgeCanSpread && isCandidateCloseNeighbour && candidateState.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
                // LanGvest: If age permits, we spread like an Infested Dirt block into a normal Dirt block.
                var newNeighbourState = getDirtState(increaseAge(random, currentAge));
                level.setBlockAndUpdate(candidatePos, newNeighbourState);
                continue;
            }

            if (isAgeCanSpread && isCandidateCloseNeighbour && candidateState.is(Blocks.GRASS_BLOCK)) {
                // LanGvest: If age permits, we spread like an Infested Grass block into a normal Grass block.
                var newNeighbourState = getGrassState(increaseAge(random, currentAge));
                level.setBlockAndUpdate(candidatePos, copySnowyAboveFrom(level, candidatePos, newNeighbourState));
                continue;
            }

            if (candidateState.is(Blocks.GRASS_BLOCK)) {
                // LanGvest: If age does not permit but the environment permits, we turn an Infested Dirt block into an Infested Grass block under the influence of a normal Grass block (spreading only the grass cover).
                var newState = getGrassState(currentAge);
                boolean isBrightEnough = level.getMaxLocalRawBrightness(candidatePos.above()) >= 9;
                if (isBrightEnough && PlantopiaInfestedGrassBlock.canPropagateGrass(newState, level, pos)) {
                    level.setBlockAndUpdate(pos, copySnowyAboveFrom(level, pos, newState));
                }
            }
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);

        if (!level.isClientSide() && !player.isCreative()) {
            for (var direction : Direction.values()) {
                resetInfestedBlock(level, pos.relative(direction));
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
