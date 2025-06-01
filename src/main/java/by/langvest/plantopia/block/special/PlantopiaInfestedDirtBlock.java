package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaBlockHelper.copySnowyAboveFrom;

public class PlantopiaInfestedDirtBlock extends Block {
	public static final IntegerProperty AGE = PlantopiaBlockStateProperties.INFESTED_DIRT_AGE;
	public static int MAX_AGE = PlantopiaBlockStateProperties.INFESTED_DIRT_MAX_AGE;

	public PlantopiaInfestedDirtBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(AGE, 0));
	}

	public static @NotNull Block getGrassBlock() {
		return PlantopiaBlocks.INFESTED_GRASS_BLOCK.get();
	}

	protected static int increaseAge(@NotNull RandomSource random, int currentAge) {
		var inc = 1;

		if(random.nextInt(3) == 0) inc++;
		if(random.nextInt(3) == 0) inc++;

		return Math.min(currentAge + inc, MAX_AGE);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(!level.isAreaLoaded(pos, 3)) return; // Forge: Prevent loading unloaded chunks when checking neighbor's light and spreading.

		for(int i = 0; i < 4; i++) {
			var candidatePos = pos.offset(PlantopiaMathHelper.getRandomXYZOffsetAlongFaces(random));
			var candidateState = level.getBlockState(candidatePos);
			int currentAge = state.getValue(AGE);
			boolean isAgeCanSpread = currentAge < MAX_AGE;

			if(isAgeCanSpread && candidateState.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
				// LanGvest: If age permits, we spread like infested dirt into normal dirt.
				var newCandidateState = defaultBlockState().setValue(AGE, increaseAge(random, currentAge));

				level.setBlockAndUpdate(candidatePos, newCandidateState);
			} else if(isAgeCanSpread && candidateState.is(Blocks.GRASS_BLOCK)) {
				// LanGvest: If age permits, we spread like infested grass block into normal grass block.
				var newCandidateState = getGrassBlock().defaultBlockState().setValue(AGE, increaseAge(random, currentAge));

				level.setBlockAndUpdate(candidatePos, copySnowyAboveFrom(level, candidatePos, newCandidateState));
			} else if(!isAgeCanSpread && candidateState.is(Blocks.GRASS_BLOCK)) {
				// LanGvest: If age does not permit but the environment permits, we turn from infested dirt to infested grass block from normal grass block.
				var newState = getGrassBlock().defaultBlockState().setValue(AGE, currentAge);

				boolean isBrightnessEnough = level.getMaxLocalRawBrightness(candidatePos.above()) >= 9;

				if(isBrightnessEnough && PlantopiaInfestedGrassBlock.canPropagateGrass(newState, level, pos)) {
					level.setBlockAndUpdate(pos, copySnowyAboveFrom(level, pos, newState));
				}
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
