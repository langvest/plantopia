package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaBranchingShrubBlock extends Block implements SimpleWaterloggedBlock, PlantopiaNaturalBlock {
    protected static final VoxelShape COLLISION_ROD_SHAPE = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty BASE = PlantopiaBlockStateProperties.BASE;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public PlantopiaBranchingShrubBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false).setValue(BASE, true).setValue(FACING, Direction.UP));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var clickedFace = context.getClickedFace();

        Set<Direction> directions = ImmutableSet.of(
            clickedFace,
            Direction.UP,
            Direction.DOWN,
            context.getHorizontalDirection().getOpposite()
        );

        BlockState newState = defaultBlockState();

        for (var direction : directions) {
            var desiredState = getDesiredState(level, clickedPos, direction);

            if (desiredState != null) {
                newState = desiredState;
                break;
            }
        }

        return copyWaterloggedFrom(level, clickedPos, newState);
    }

    @Nullable
    protected BlockState getDesiredState(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction desiredFacing) {
        var adjacentPos = pos.relative(desiredFacing.getOpposite());
        var adjacentState = level.getBlockState(adjacentPos);

        if (adjacentState.is(this)) {
            if (adjacentState.getValue(FACING) == desiredFacing) {
                return defaultBlockState()
                    .setValue(FACING, desiredFacing)
                    .setValue(BASE, false);
            }
        } else if (mayPlaceOn(adjacentState, level, adjacentPos)) {
            return defaultBlockState()
                .setValue(FACING, desiredFacing)
                .setValue(BASE, true);
        }

        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.block();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        var facing = state.getValue(FACING);
        var axis = facing.getAxis();
        var seed = getCollisionSeed(pos, axis);
        var offset1 = getCollisionOffset(seed.getFirst(), axis);
        var offset2 = getCollisionOffset(seed.getSecond(), axis);

        var rotatedShape = PlantopiaShapeHelper.orientShape(COLLISION_ROD_SHAPE, facing);

        var shape = Shapes.or(
            rotatedShape.move(offset1.x, offset1.y, offset1.z),
            rotatedShape.move(offset2.x, offset2.y, offset2.z)
        );

        if (context instanceof EntityCollisionContext entityCollisionContext) {
            var entity = entityCollisionContext.getEntity();
            if (entity instanceof LivingEntity) {
                if (facing == Direction.UP && entity.position().y >= pos.getY() + 0.5D) {
                    return Shapes.empty();
                }
            }
        }

        return shape;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return switch (type) {
            case LAND -> true;
            case AIR -> false;
            default -> super.isPathfindable(state, level, pos, type);
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isCollisionShapeFullBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    protected float getMaxCollisionOffset() {
        return 0.48F;
    }

    protected Pair<Long, Long> getCollisionSeed(@NotNull BlockPos pos, Direction.@NotNull Axis axis) {
        return switch (axis) {
            case X -> Pair.of(
                PlantopiaMathHelper.getSeed(0, pos.getY(), pos.getZ()),
                PlantopiaMathHelper.getSeed(0, pos.getZ(), pos.getY())
            );
            case Y -> Pair.of(
                PlantopiaMathHelper.getSeed(pos.getX(), 0, pos.getZ()),
                PlantopiaMathHelper.getSeed(pos.getZ(), 0, pos.getX())
            );
            case Z -> Pair.of(
                PlantopiaMathHelper.getSeed(pos.getX(), pos.getY(), 0),
                PlantopiaMathHelper.getSeed(pos.getY(), pos.getX(), 0)
            );
        };
    }

    protected Vec3 getCollisionOffset(long seed, Direction.@NotNull Axis axis) {
        float maxOffset = getMaxCollisionOffset();
        var offset = PlantopiaMathHelper.getSeededOffset(seed, maxOffset);

        return switch (axis) {
            case X -> new Vec3(0, offset.y(), offset.z());
            case Y -> new Vec3(offset.x(), 0, offset.z());
            case Z -> new Vec3(offset.x(), offset.y(), 0);
        };
    }

    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(Blocks.CLAY) || state.is(BlockTags.DEAD_BUSH_MAY_PLACE_ON);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        var isBase = state.getValue(BASE);
        var facing = state.getValue(FACING);
        var adjacentPos = pos.relative(facing.getOpposite());
        var adjacentState = level.getBlockState(adjacentPos);

        if (isBase) {
            return mayPlaceOn(adjacentState, level, adjacentPos);
        }

        return adjacentState.is(this) && adjacentState.getValue(FACING) == facing;
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        var mainHandItem = player.getMainHandItem();

        if (mainHandItem.canPerformAction(ToolActions.SWORD_DIG) || mainHandItem.canPerformAction(ToolActions.SHEARS_DIG)) {
            return 1.0F;
        }

        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        var facing = state.getValue(FACING);

        if (direction == facing.getOpposite() && !state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, BASE, FACING);
    }

    @Override
    public boolean generateAt(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull RandomSource random, int flags) {
        int height = 1 + random.nextIntBetweenInclusive(0, 2);

        if (random.nextDouble() < 0.35D) {
            height += random.nextIntBetweenInclusive(0, 1);
        }

        int successfulTries = 0;

        for (int i = 0; i < height; i++) {
            var candidatePos = pos.above(i);
            var targetState = level.getBlockState(candidatePos);

            if (!targetState.canBeReplaced()) break;

            var newState = copyWaterloggedFrom(level, candidatePos, state.setValue(BASE, i == 0));

            if (!newState.canSurvive(level, candidatePos)) break;

            level.setBlock(candidatePos, newState, flags);
            successfulTries++;
        }

        return successfulTries > 0;
    }
}
