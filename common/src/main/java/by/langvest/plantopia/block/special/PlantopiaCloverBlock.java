package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaSegmentableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class PlantopiaCloverBlock extends BushBlock implements BonemealableBlock, PlantopiaSegmentableBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = PlantopiaBlockStateProperties.SEGMENT_AMOUNT;
    protected final Function<BlockState, VoxelShape> shapes;

    public PlantopiaCloverBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, MIN_SEGMENT));
        this.shapes = makeShapes();
    }

    @Override
    public double getShapeHeight() {
        return 3.0D;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return canBeReplaced(state, context, getSegmentAmountProperty()) || super.canBeReplaced(state, context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapes.apply(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStateForPlacement(context, this, getSegmentAmountProperty(), FACING);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    protected boolean isValidBonemealCandidate(ServerLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        if (state.is(this)) return true;
        return state.isAir() && canSurvive(state, level, pos);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        var basePos = new BlockPos(pos);
        boolean shouldGrowBigClover = state.getValue(AMOUNT) == MAX_SEGMENT;

        label49:
        for (int i = 0; i < 128; i++) {
            var candidatePos = basePos;

            for (int j = 0; j < i / 40; j++) {
                var dx = random.nextInt(3) - 1;
                var dy = random.nextInt(3) - 1;
                var dz = random.nextInt(3) - 1;

                candidatePos = candidatePos.offset(dx, dy, dz);

                if (!isValidBonemealCandidate(level, candidatePos)) continue label49;
            }

            var candidateState = level.getBlockState(candidatePos);

            if (candidateState.is(this) && candidateState.getValue(AMOUNT) < MAX_SEGMENT && random.nextInt(10) == 0) {
                var newState = candidateState
                    .setValue(AMOUNT, candidateState.getValue(AMOUNT) + 1);

                level.setBlock(candidatePos, newState, Block.UPDATE_CLIENTS);
                continue;
            }

            if (candidateState.isAir()) {
                var newState = defaultBlockState()
                    .setValue(FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random))
                    .setValue(AMOUNT, MIN_SEGMENT);

                level.setBlock(candidatePos, newState, Block.UPDATE_ALL);
            }
        }

        if (shouldGrowBigClover && level.random.nextFloat() < 0.45F) {
            growBigClover(level, pos);
        }
    }

    protected void growBigClover(ServerLevel level, BlockPos pos) {
        level.setBlock(pos, PlantopiaBlocks.BIG_CLOVER.get().defaultBlockState(), 3);
    }

    private @NotNull @Unmodifiable Function<BlockState, VoxelShape> makeShapes() {
        return getShapeForEachState(getShapeCalculator(FACING, AMOUNT))::get;
    }
}
