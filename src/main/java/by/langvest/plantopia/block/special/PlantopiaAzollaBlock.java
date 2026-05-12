package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

public class PlantopiaAzollaBlock extends BushBlock implements BonemealableBlock {
	protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
	public static final int MIN_LEAFS = 1;
	public static final int MAX_LEAFS = 4;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final IntegerProperty AMOUNT = PlantopiaBlockStateProperties.SEGMENT_AMOUNT;

	public PlantopiaAzollaBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, MIN_LEAFS));
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
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		super.entityInside(state, level, pos, entity);

		if(level instanceof ServerLevel && entity instanceof Boat) {
			level.destroyBlock(new BlockPos(pos), true, entity);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
		return !context.isSecondaryUseActive() && context.getItemInHand().is(asItem()) && state.getValue(AMOUNT) < MAX_LEAFS || super.canBeReplaced(state, context);
	}

    @Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var state = context.getLevel().getBlockState(context.getClickedPos());

		if(state.is(this)) {
			return state.setValue(AMOUNT, Math.min(MAX_LEAFS, state.getValue(AMOUNT) + 1));
		}

		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING, AMOUNT);
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		return level.getFluidState(pos.below()).isSourceOfType(Fluids.WATER);
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return true;
	}

	protected boolean isValidBonemealCandidate(@NotNull ServerLevel level, @NotNull BlockPos pos) {
		var posBelow = pos.below();
		var fluidStateBelow = level.getFluidState(posBelow);
		if(!fluidStateBelow.isSourceOfType(Fluids.WATER)) return false;

		var state = level.getBlockState(pos);
		if(state.is(this)) return true;

        return state.isAir() && canSurvive(state, level, pos);
    }

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		var basePos = new BlockPos(pos);

		label49: for(int i = 0; i < 128; i++) {
			var candidatePos = basePos;

			for(int j = 0; j < i / 40; j++) {
				var dx = random.nextInt(3) - 1;
				var dz = random.nextInt(3) - 1;

				candidatePos = candidatePos.offset(dx, 0, dz);

				if(!isValidBonemealCandidate(level, candidatePos)) continue label49;
			}

			var candidateState = level.getBlockState(candidatePos);

			if(candidateState.is(this) && candidateState.getValue(AMOUNT) < MAX_LEAFS && random.nextInt(10) == 0) {
				var newState = candidateState
					.setValue(AMOUNT, candidateState.getValue(AMOUNT) + 1);

				level.setBlock(candidatePos, newState, Block.UPDATE_CLIENTS);
				continue;
			}

			if(candidateState.isAir()) {
				var newState = defaultBlockState()
					.setValue(FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
					.setValue(AMOUNT, MIN_LEAFS);

				level.setBlock(candidatePos, newState, Block.UPDATE_ALL);
			}
		}
	}

	@Override
	public PlantType getPlantType(BlockGetter level, BlockPos pos) {
		return PlantType.WATER;
	}
}
