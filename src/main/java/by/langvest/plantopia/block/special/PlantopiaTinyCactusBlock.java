package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaOffsettableBlock;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PlantopiaTinyCactusBlock extends BushBlock implements BonemealableBlock, PlantopiaOffsettableBlock {
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D);
	protected static final VoxelShape ATTACHED_SHAPE = Block.box(4.0D, -1.0D, 4.0D, 12.0D, 8.0D, 12.0D);
	public static final DirectionProperty FACING = PlantopiaBlockStateProperties.CACTUS_FACING;

	public PlantopiaTinyCactusBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		var facing = state.getValue(FACING);
		var shape = SHAPE;

		if (facing.getAxis().isHorizontal()) {
			shape = ATTACHED_SHAPE;
		} else {
			var offset = state.getOffset(level, pos);
			shape = SHAPE.move(offset.x, offset.y, offset.z);
		}

		return PlantopiaShapeHelper.orientShape(shape, facing);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var level = context.getLevel();
		var clickedPos = context.getClickedPos();
		var clickedFace = context.getClickedFace();

		var facing = Direction.UP;

		if(clickedFace.getAxis().isHorizontal()) {
			var adjacentPos = clickedPos.relative(clickedFace.getOpposite());
			var adjacentState = level.getBlockState(adjacentPos);

			if(adjacentState.is(Blocks.CACTUS)) {
				facing = clickedFace;
			}
		}

		var newState = defaultBlockState().setValue(FACING, facing);

		if (!newState.canSurvive(level, clickedPos)) {
			return null;
		}

		return newState;
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.is(Blocks.CACTUS) || state.is(BlockTags.SAND) || super.mayPlaceOn(state, level, pos);
	}

	@Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		var facing = state.getValue(FACING);

		if (facing == Direction.UP) {
			var posBelow = pos.below();
			var stateBelow = level.getBlockState(posBelow);
			return mayPlaceOn(stateBelow, level, posBelow);
		}

		var attachPos = pos.relative(facing.getOpposite());
		var attachState = level.getBlockState(attachPos);
		return attachState.is(Blocks.CACTUS);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		entity.hurt(level.damageSources().cactus(), 1.0F);
	}

	@Override
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return false;
	}

	@Override
	public float getMaxHorizontalOffset() {
		return super.getMaxHorizontalOffset() * 0.6F;
	}

	@Override
	public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
		return state.is(PlantopiaBlocks.TINY_CACTUS.get());
	}

	@Override
	public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		return level.random.nextFloat() < 0.45F;
	}

	@Override
	public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
		var facing = state.getValue(FACING);
		var floweringState = PlantopiaBlocks.FLOWERING_TINY_CACTUS.get().defaultBlockState().setValue(FACING, facing);

		level.setBlock(pos, floweringState, 2);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public Optional<OffsetFunction> getOffsetFunction(Optional<OffsetFunction> defaultOffsetFunction) {
		OffsetFunction offsetFunction = (state, level, pos) -> {
			var facing = state.getValue(FACING);

			if (facing.getAxis().isHorizontal()) {
				return Vec3.ZERO;
			}

			var posBelow = pos.below();
			var stateBelow = level.getBlockState(posBelow);

			if (stateBelow.is(Blocks.CACTUS)) {
				return Vec3.ZERO;
			}

            return defaultOffsetFunction
				.map(function -> function.evaluate(state, level, pos))
				.orElse(Vec3.ZERO);
        };

		return Optional.of(offsetFunction);
	}
}
