package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaPineconeBlock extends Block {
    public static final DirectionProperty DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;

    protected static final VoxelShape SHAPE_DOWN = Block.box(5.0D, 6.75D, 5.0D, 11.0D, 16.0D, 11.0D);
    protected static final VoxelShape SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 9.25D, 11.0D);

    public PlantopiaPineconeBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(DIRECTION, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DIRECTION);
    }

    public static @NotNull BlockState getStateForDirection(Direction direction) {
        return PlantopiaBlocks.PINE_CONE.get().defaultBlockState().setValue(DIRECTION, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var clickedFace = context.getNearestLookingVerticalDirection().getOpposite();

        var direction = calculateSuitableDirection(level, pos, clickedFace);
        if (direction == null) {
            return null;
        }

        return defaultBlockState().setValue(DIRECTION, direction);
    }

    private static @Nullable Direction calculateSuitableDirection(LevelReader level, BlockPos pos, Direction direction) {
        // Try to place in the desired direction.
        if (isValidPlacement(level, pos, direction)) {
            return direction;
        }

        // If that fails, try the opposite direction.
        if (isValidPlacement(level, pos, direction.getOpposite())) {
            return direction.getOpposite();
        }

        // If both fail, placement is not possible.
        return null;
    }

    private static boolean isValidPlacement(LevelReader level, BlockPos pos, Direction direction) {
        var basePos = pos.relative(direction.getOpposite());
        var baseState = level.getBlockState(basePos);
        return baseState.is(BlockTags.LEAVES);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var vec3 = state.getOffset(level, pos);
        var shape = state.getValue(DIRECTION) == Direction.UP ? SHAPE_UP : SHAPE_DOWN;
        return shape.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var direction = state.getValue(DIRECTION);
        return isValidPlacement(level, pos, direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.getValue(DIRECTION).getOpposite() && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}
