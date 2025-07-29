package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlantopiaSmallPlatterleafBlock extends WaterlilyBlock implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);

	public PlantopiaSmallPlatterleafBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		var fluidState = level.getFluidState(pos);
		var fluidStateAbove = level.getFluidState(pos.above());

		return (fluidState.isSourceOfType(Fluids.WATER) || state.getBlock() instanceof IceBlock) && fluidStateAbove.isEmpty();
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var posBelow = pos.below();
		var stateBelow = level.getBlockState(posBelow);

		return mayPlaceOn(stateBelow, level, posBelow);
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.WATER;
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		var candidateBasePoses = getCandidatePosesToGrowBigPlatterleaf(pos);

		PlantopiaBigPlatterleafBlock bigPlatterleafBlock = (PlantopiaBigPlatterleafBlock)PlantopiaBlocks.BIG_PLATTERLEAF.get();

		return candidateBasePoses.stream().anyMatch(candidateBasePos -> bigPlatterleafBlock.canNaturallyPlaceAt(level, candidateBasePos, pos));
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return level.random.nextFloat() < 0.45F;
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		growBigPlatterleaf(level, pos);
	}

	protected void growBigPlatterleaf(@NotNull ServerLevel level, BlockPos pos) {
		var candidateBasePoses = getCandidatePosesToGrowBigPlatterleaf(pos);

		Collections.shuffle(candidateBasePoses);

		PlantopiaBigPlatterleafBlock bigPlatterleafBlock = (PlantopiaBigPlatterleafBlock)PlantopiaBlocks.BIG_PLATTERLEAF.get();

		for(var candidateBasePos : candidateBasePoses) {
			var successfullyPlaced = bigPlatterleafBlock.placeAt(level, candidateBasePos, bigPlatterleafBlock.defaultBlockState(), 3, pos);
			if(successfullyPlaced) break;
		}
	}

	protected List<BlockPos> getCandidatePosesToGrowBigPlatterleaf(BlockPos pos) {
		return Arrays.asList(
			pos,
			pos.south(),
			pos.south().west(),
			pos.west()
		);
	}
}
