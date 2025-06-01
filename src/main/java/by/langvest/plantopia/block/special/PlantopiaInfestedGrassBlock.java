package by.langvest.plantopia.block.special;

import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.worldgen.placement.PlantopiaVegetationPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class PlantopiaInfestedGrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
	public static final IntegerProperty AGE = BlockStateProperties.AGE_25;

	public PlantopiaInfestedGrassBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(SNOWY, false).setValue(AGE, 0));
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
