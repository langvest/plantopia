package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.worldgen.placement.PlantopiaVegetationPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaBlockHelper.copySnowyAboveFrom;

public class PlantopiaInfestedGrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
	public static final IntegerProperty AGE = PlantopiaBlockStateProperties.INFESTED_DIRT_AGE;
	public static int MAX_AGE = PlantopiaBlockStateProperties.INFESTED_DIRT_MAX_AGE;

	public PlantopiaInfestedGrassBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(SNOWY, false).setValue(AGE, 0));
	}

	public static @NotNull Block getDirtBlock() {
		return PlantopiaBlocks.INFESTED_DIRT.get();
	}

	protected static boolean canBeGrass(BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var posAbove = pos.above();
		var stateAbove = level.getBlockState(posAbove);

		if(stateAbove.is(Blocks.SNOW) && stateAbove.getValue(SnowLayerBlock.LAYERS) == 1) {
			return true;
		}

		if(stateAbove.getFluidState().getAmount() == 8) {
			return false;
		}

		int lightBlockInto = LightEngine.getLightBlockInto(level, state, pos, stateAbove, posAbove, Direction.UP, stateAbove.getLightBlock(level, posAbove));

		return lightBlockInto < level.getMaxLightLevel();
	}

	protected static boolean canPropagateGrass(BlockState state, LevelReader level, @NotNull BlockPos pos) {
		var posAbove = pos.above();

		return canBeGrass(state, level, pos) && !level.getFluidState(posAbove).is(FluidTags.WATER);
	}

	/**
	 * Performs a random tick on a block.
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(!canBeGrass(state, level, pos)) {
			if(!level.isAreaLoaded(pos, 1)) return; // LanGvest: Prevent loading unloaded chunks when checking neighbor's light and spreading.

			var currentAge = state.getValue(AGE);
			var dirtState = getDirtBlock().defaultBlockState().setValue(AGE, currentAge);

			level.setBlockAndUpdate(pos, dirtState);
			return;
		}

		if(!level.isAreaLoaded(pos, 3)) return; // LanGvest: Prevent loading unloaded chunks when checking neighbor's light and spreading.

		for(int i = 0; i < 4; i++) {
			var candidatePos = pos.offset(PlantopiaMathHelper.getRandomOffsetInArea(random, 1));
			var candidateState = level.getBlockState(candidatePos);
			boolean isBrightnessEnough = level.getMaxLocalRawBrightness(pos.above()) >= 9;
			int currentAge = state.getValue(AGE);
			boolean isAgeCanSpread = currentAge < MAX_AGE;
			boolean isCandidateCloseNeighbour = PlantopiaMathHelper.isCloseNeighbours(pos, candidatePos);

			if(candidateState.is(getDirtBlock())) {
				// LanGvest: If the environment permits, we spread like infested grass block into infested dirt.
				var newCandidateState = defaultBlockState().setValue(AGE, candidateState.getValue(AGE));

				if(isBrightnessEnough && canPropagateGrass(newCandidateState, level, candidatePos)) {
					level.setBlockAndUpdate(candidatePos, copySnowyAboveFrom(level, candidatePos, newCandidateState));
				}
			} else if(isAgeCanSpread && isCandidateCloseNeighbour && candidateState.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
				// LanGvest: If age permits, we spread like infested dirt into normal dirt.
				var newCandidateState = getDirtBlock().defaultBlockState().setValue(AGE, PlantopiaInfestedDirtBlock.increaseAge(random, currentAge));

				level.setBlockAndUpdate(candidatePos, newCandidateState);
			} else if(isAgeCanSpread && isCandidateCloseNeighbour && candidateState.is(Blocks.GRASS_BLOCK)) {
				// LanGvest: If age permits, we spread like infested grass block into normal grass block.
				var newCandidateState = defaultBlockState().setValue(AGE, PlantopiaInfestedDirtBlock.increaseAge(random, currentAge));

				level.setBlockAndUpdate(candidatePos, copySnowyAboveFrom(level, candidatePos, newCandidateState));
			} else if(candidateState.is(Blocks.DIRT)) {
				// LanGvest: If age does not permit but the environment permits, we spread like normal grass block into normal dirt.
				var newCandidateState = Blocks.GRASS_BLOCK.defaultBlockState();

				if(isBrightnessEnough && canPropagateGrass(newCandidateState, level, candidatePos)) {
					level.setBlockAndUpdate(candidatePos, copySnowyAboveFrom(level, candidatePos, newCandidateState));
				}
			}
		}

		// LanGvest: Try to grow a hogweed.

		if(random.nextInt(3) == 0) {
			var hogweedFeature = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE).getHolder(PlantopiaVegetationPlacements.HOGWEED_INFESTED_GRASS_BLOCK);

			hogweedFeature.ifPresent(placedFeatureReference -> placedFeatureReference.value().place(level, level.getChunkSource().getGenerator(), random, pos.above()));
		}
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, @NotNull Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluidState) {
		if(!level.isClientSide() && !player.isCreative()) {
			for(var direction : Direction.values()) {
				var neighbourPos = pos.relative(direction);
				var neighbourState = level.getBlockState(neighbourPos);

				if(!neighbourState.is(this) && !neighbourState.is(getDirtBlock())) continue;

				level.setBlock(neighbourPos, neighbourState.setValue(AGE, 0), 54);
			}
		}

		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluidState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(SNOWY, AGE);
	}

	/**
	 * @return whether bonemeal can be used on this block
	 */
	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		return level.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		var poseAbove = pos.above();
		var placedFeatures = level.registryAccess().registryOrThrow(Registries.PLACED_FEATURE);
		var grassBonemealFeature = placedFeatures.getHolder(VegetationPlacements.GRASS_BONEMEAL);
		var hogweedBonemealFeature = placedFeatures.getHolder(PlantopiaVegetationPlacements.HOGWEED_BONEMEAL);

		label49: for(int i = 0; i < 128; i++) {
			var candidatePos = poseAbove;

			// LanGvest: Locate the correct position to place the grass.
			for(int j = 0; j < i / 16; j++) {
				// LanGvest: Locate the candidate block position.

				var dx = random.nextInt(3) - 1;
				var dy = (random.nextInt(3) - 1) * random.nextInt(3) / 2;
				var dz = random.nextInt(3) - 1;

				candidatePos = candidatePos.offset(dx, dy, dz);

				// LanGvest: Validate candidate block position.

				var candidateState = level.getBlockState(candidatePos);
				var candidateStateBelow = level.getBlockState(candidatePos.below());

				if(!candidateStateBelow.is(PlantopiaBlockTags.BONEMEAL_SPREAD_ON)) continue label49;
				if(candidateState.isCollisionShapeFullBlock(level, candidatePos)) continue label49;
			}

			// LanGvest: If candidate block position is valid, continue to placing grass.

			var candidateState = level.getBlockState(candidatePos);

			// lanGvest: Grow plants on which bonemeal can be applied.
			if(candidateState.is(PlantopiaBlockTags.BONEMEAL_SPREAD_GROWABLE) && random.nextInt(10) == 0) {
				var candidateBlock = candidateState.getBlock();

				if(candidateBlock instanceof BonemealableBlock bonemealableBlock) {
					bonemealableBlock.performBonemeal(level, random, candidatePos, candidateState);
					continue;
				}
			}

			var successfullyGrownHogweed = false;

			if(candidateState.canBeReplaced() && random.nextInt(1) == 0 && hogweedBonemealFeature.isPresent()) {
				successfullyGrownHogweed = hogweedBonemealFeature.get().value().place(level, level.getChunkSource().getGenerator(), random, candidatePos);
			}

			if(candidateState.isAir() && !successfullyGrownHogweed && grassBonemealFeature.isPresent()) {
				grassBonemealFeature.get().value().place(level, level.getChunkSource().getGenerator(), random, candidatePos);
			}
		}
	}
}
