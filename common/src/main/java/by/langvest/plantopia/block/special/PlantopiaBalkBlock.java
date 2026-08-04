package by.langvest.plantopia.block.special;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

@ParametersAreNonnullByDefault
public class PlantopiaBalkBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape CENTER_SHAPE = Block.box(4, 4, 4, 12, 12, 12);
    protected static final VoxelShape NORTH_SHAPE = Block.box(4, 4, 0, 12, 12, 4);
    protected static final VoxelShape SOUTH_SHAPE = Block.box(4, 4, 12, 12, 12, 16);
    protected static final VoxelShape EAST_SHAPE = Block.box(12, 4, 4, 16, 12, 12);
    protected static final VoxelShape WEST_SHAPE = Block.box(0, 4, 4, 4, 12, 12);
    protected static final VoxelShape UP_SHAPE = Block.box(4, 12, 4, 12, 16, 12);
    protected static final VoxelShape DOWN_SHAPE = Block.box(4, 0, 4, 12, 4, 12);

    protected static final ImmutableMap<BooleanProperty, VoxelShape> SHAPE_BY_PROPERTY = ImmutableMap.of(
        NORTH, NORTH_SHAPE,
        SOUTH, SOUTH_SHAPE,
        EAST, EAST_SHAPE,
        WEST, WEST_SHAPE,
        UP, UP_SHAPE,
        DOWN, DOWN_SHAPE
    );

    private static final ImmutableMap<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.of(
        Direction.NORTH, NORTH,
        Direction.SOUTH, SOUTH,
        Direction.EAST, EAST,
        Direction.WEST, WEST,
        Direction.UP, UP,
        Direction.DOWN, DOWN
    );

    protected final Supplier<Block> stubBlock;

    public PlantopiaBalkBlock(Properties properties, Supplier<Block> stubBlock) {
        super(properties);

        this.stubBlock = stubBlock;

        registerDefaultState(
            defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false)
        );
    }

    public Block getStubBlock() {
        return stubBlock.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, NORTH, SOUTH, EAST, WEST, UP, DOWN, WATERLOGGED);
    }

    public static BooleanProperty getSegmentProperty(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var shape = CENTER_SHAPE;
        for (var entry : SHAPE_BY_PROPERTY.entrySet()) {
            if (state.getValue(entry.getKey())) {
                shape = Shapes.or(shape, entry.getValue());
            }
        }
        return shape;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var clickedFace = context.getClickedFace();
        boolean isSneaking = context.getPlayer() != null && context.getPlayer().isSecondaryUseActive();

        BlockState newState;
        if (isSneaking) {
            newState = getStubBlock().defaultBlockState();
        } else {
            newState = switch (clickedFace.getAxis()) {
                case X -> defaultBlockState().setValue(EAST, true).setValue(WEST, true);
                case Y -> defaultBlockState().setValue(UP, true).setValue(DOWN, true);
                case Z -> defaultBlockState().setValue(NORTH, true).setValue(SOUTH, true);
            };
        }

        return copyWaterloggedFrom(level, pos, newState.setValue(FACING, clickedFace));
    }

    protected boolean isStraight(BlockState state) {
        var tipDirection = state.getValue(FACING);
        var baseDirection = tipDirection.getOpposite();

        for (var direction : Direction.values()) {
            if (direction == baseDirection || direction == tipDirection) {
                if (!state.getValue(getSegmentProperty(direction))) {
                    return false;
                }
            } else {
                if (state.getValue(getSegmentProperty(direction))) {
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean isTipNeighbourSturdy(LevelAccessor level, BlockPos tipPos, BlockState tipState, Direction facing) {
        if (tipState.getBlock() instanceof PlantopiaBalkBlock && tipState.getValue(getSegmentProperty(facing.getOpposite()))) {
            return true;
        }

        if (tipState.getBlock() instanceof PlantopiaBalkStubBlock && tipState.getValue(FACING) == facing) {
            return true;
        }

        return tipState.isFaceSturdy(level, tipPos, facing.getOpposite(), SupportType.CENTER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        var facing = state.getValue(FACING);

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (direction == facing.getOpposite()) {
            return state;
        }

        boolean isNeighborBalk = neighborState.getBlock() instanceof PlantopiaBalkBlock || neighborState.getBlock() instanceof PlantopiaBalkStubBlock;

        if (isNeighborBalk) {
            var neighborFacing = neighborState.getValue(FACING);

            if (direction == neighborFacing) {
                var newState = state.setValue(getSegmentProperty(direction), true);

                if (direction.getAxis() != facing.getAxis() && isStraight(state)) {
                    var tipPos = pos.relative(facing);
                    var tipState = level.getBlockState(tipPos);
                    if (!isTipNeighbourSturdy(level, tipPos, tipState, facing)) {
                        newState = newState.setValue(getSegmentProperty(facing), false);
                    }
                }

                return newState;
            }
        }

        return state;
    }
}
