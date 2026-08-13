package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.helper.PlantopiaMathHelper;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

@ParametersAreNonnullByDefault
public class PlantopiaBranchingShrubBlock extends Block implements SimpleWaterloggedBlock {
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var clickedFace = context.getClickedFace();

        Set<Direction> directions = ImmutableSet.of(
            clickedFace,
            Direction.UP,
            Direction.DOWN,
            context.getHorizontalDirection().getOpposite()
        );

        var newState = defaultBlockState();

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
    protected BlockState getDesiredState(Level level, BlockPos pos, Direction desiredFacing) {
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
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return switch (type) {
            case LAND -> true;
            case AIR -> false;
            default -> super.isPathfindable(state, level, pos, type);
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    protected float getMaxCollisionOffset() {
        return 0.48F;
    }

    protected Pair<Long, Long> getCollisionSeed(BlockPos pos, Direction.Axis axis) {
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

    protected Vec3 getCollisionOffset(long seed, Direction.Axis axis) {
        float maxOffset = getMaxCollisionOffset();
        var offset = PlantopiaMathHelper.getSeededOffset(seed, maxOffset);

        return switch (axis) {
            case X -> new Vec3(0, offset.y(), offset.z());
            case Y -> new Vec3(offset.x(), 0, offset.z());
            case Z -> new Vec3(offset.x(), offset.y(), 0);
        };
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(PlantopiaBlockTags.BRANCHING_SHRUB_MAY_PLACE_ON);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
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
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        var mainHandItem = player.getMainHandItem();

        if (mainHandItem.is(ItemTags.SWORDS) || mainHandItem.is(Items.SHEARS)) {
            return 1.0F;
        }

        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
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
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, BASE, FACING);
    }
}
