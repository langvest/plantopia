package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaIceCrustBlock extends MultifaceBlock implements PlantopiaNaturalBlock {
    private static final double MIN_PROJECTILE_VELOCITY_TO_BREAK_ICE_CRUST = 1.0D;
    private static final VoxelShape FLOATING_AABB = Block.box(0.0D, -2.0D, 0.0D, 16.0D, 0.0D, 16.0D);
    public static final BooleanProperty FLOATING = PlantopiaBlockStateProperties.FLOATING;
    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public PlantopiaIceCrustBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FLOATING, false));
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        var item = context.getItemInHand().getItem();
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();

        if (item instanceof BlockItem blockItem) {
            var block = blockItem.getBlock();
            var testState = block.defaultBlockState();

            if (!testState.is(this) && !Block.isShapeFullBlock(testState.getCollisionShape(level, clickedPos))) {
                return false;
            }
        }

        return super.canBeReplaced(state, context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
        if (level.isClientSide()) return;

        var pos = hitResult.getBlockPos();

        if (!projectile.mayInteract(level, pos)) return;
        if (projectile.getDeltaMovement().length() < MIN_PROJECTILE_VELOCITY_TO_BREAK_ICE_CRUST) return;

        if (projectile instanceof AbstractArrow) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FLOATING);
    }

    @Override
    public @NotNull MultifaceSpreader getSpreader() {
        return spreader;
    }

    @SuppressWarnings("deprecation")
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        if (adjacentState.is(this) && adjacentState.getValue(FLOATING)) {
            return true;
        }

        if (adjacentState.getBlock() instanceof IceBlock) {
            return true;
        }

        return super.skipRendering(state, adjacentState, side);
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var superShape = super.getShape(state, level, pos, context);

        if (state.getValue(FLOATING)) {
            return Shapes.join(FLOATING_AABB, superShape, BooleanOp.OR);
        }

        return superShape;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FLOATING) ? FLOATING_AABB : Shapes.empty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(level, pos)) {
            level.removeBlock(pos, false);
        }
    }

    private boolean isWaterSource(BlockGetter level, BlockPos pos) {
        return level.getFluidState(pos).isSourceOfType(Fluids.WATER);
    }

    private boolean canAttachToBlock(BlockGetter level, Direction direction, BlockPos pos, BlockState state) {
        if (direction == Direction.DOWN && isWaterSource(level, pos)) {
            return true;
        }

        return MultifaceBlock.canAttachTo(level, direction, pos, state);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean canSurvive = false;

        for (var direction : DIRECTIONS) {
            if (hasFace(state, direction)) {
                var attachedPos = pos.relative(direction);
                var attachedState = level.getBlockState(attachedPos);

                if (!canAttachToBlock(level, direction, attachedPos, attachedState)) {
                    return false;
                }

                canSurvive = true;
            }
        }

        return canSurvive;
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (!isFaceSupported(direction)) {
            return null;
        }

        var newState = state.is(this) ? state : defaultBlockState();
        var attachedPos = pos.relative(direction);
        var attachedState = level.getBlockState(attachedPos);

        if (canAttachToBlock(level, direction, attachedPos, attachedState)) {
            return newState.setValue(getFaceProperty(direction), true);
        }

        return null;
    }

    @Nullable
    protected BlockState accumulateStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var currentState = level.getBlockState(clickedPos);
        var primaryDirection = context.getClickedFace().getOpposite();

        if (!(currentState.is(this) && hasFace(currentState, primaryDirection))) {
            return getStateForPlacement(currentState, level, clickedPos, primaryDirection);
        }

        return super.getStateForPlacement(context);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        if (level.getFluidState(clickedPos).isSource()) return null;

        var newState = accumulateStateForPlacement(context);
        if (newState == null) return null;

        boolean isFloating = hasFace(newState, Direction.DOWN) && isWaterSource(level, clickedPos.below());
        return newState.setValue(FLOATING, isFloating);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        var newState = state;

        if (hasFace(state, direction) && !canAttachToBlock(level, direction, neighborPos, neighborState)) {
            newState = MultifaceBlock.removeFace(state, getFaceProperty(direction));
        }

        if (!hasAnyFace(newState)) {
            return Blocks.AIR.defaultBlockState();
        }

        boolean isFloating = hasFace(newState, Direction.DOWN) && isWaterSource(level, pos.below());
        return newState.setValue(FLOATING, isFloating);
    }

    @Override
    public boolean isValidStateForPlacement(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        if (!isFaceSupported(direction)) {
            return false;
        }

        if (!state.is(this) || !hasFace(state, direction)) {
            var attachedPos = pos.relative(direction);
            var attachedState = level.getBlockState(attachedPos);

            return canAttachToBlock(level, direction, attachedPos, attachedState);
        }

        return false;
    }

    @Override
    public boolean placeNaturally(PlaceContext context) {
        var state = context.state();
        var level = context.level();
        var pos = context.origin();
        var posBelow = pos.below();
        var flags = context.flags();

        if (level.getFluidState(posBelow).isSourceOfType(Fluids.WATER)) {
            state = state.setValue(FLOATING, true);
        }

        return level.setBlock(pos, state, flags);
    }
}
