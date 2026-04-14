package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.extension.PlantopiaAbstractCauldronBlockExtension;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.scheduleWaterTick;

/**
 * @see net.minecraft.world.level.block.PointedDripstoneBlock
 */
public class PlantopiaIcicleBlock extends Block implements Fallable, SimpleWaterloggedBlock {
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // Ticks before a stalactite falls when its support is broken.
    private static final int DELAY_BEFORE_FALLING = 2;
    // Ticks before a stalagmite breaks if its support is broken.
    private static final int STALAGMITE_BREAK_DELAY = 1;
    // Drip particle chance when an icicle is just melting.
    private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK = 0.08F;
    // Drip particle chance when an icicle is conducting fluid.
    private static final float DRIP_PROBABILITY_PER_ANIMATE_TICK_IF_UNDER_LIQUID_SOURCE = 0.16F;
    // Chance to transfer water to a cauldron per random tick.
    private static final float WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK = 0.17578125F;
    // Minimum trident velocity to break the icicle block.
    private static final double MIN_TRIDENT_VELOCITY_TO_BREAK_ICICLE = 0.6D;
    // Damage from a falling stalactite per block of fall distance.
    private static final float STALACTITE_DAMAGE_PER_FALL_DISTANCE = 1.0F;
    // Max damage a falling stalactite can inflict.
    private static final int STALACTITE_MAX_DAMAGE = 40;
    // Minimum height for a stalactite to be considered for damage calculation.
    private static final int MIN_FALL_DAMAGE_HEIGHT = 6;
    // Extra fall distance added when falling onto a stalagmite (making them more dangerous).
    private static final float STALAGMITE_FALL_DISTANCE_OFFSET = 2.0F;
    // Damage modifier for falling onto a stalagmite.
    private static final int STALAGMITE_FALL_DAMAGE_MODIFIER = 2;
    // Growth chance per random tick.
    private static final float GROWTH_PROBABILITY_PER_RANDOM_TICK = 0.011377778F;
    // Max length a stalactite or stalagmite can grow from its base.
    private static final int MAX_GROWTH_LENGTH = 7;
    // Max search distance for a stalagmite to grow under a stalactite.
    private static final int MAX_STALAGMITE_GROWTH_SEARCH_RANGE = 10;
    // Y-position from where the drip starts (relative to the top of the block).
    private static final float STALACTITE_DRIP_START_PIXEL = 0.6875F;
    // Base tick delay before filling a cauldron.
    private static final int BASE_CAULDRON_FILL_TICK_DELAY = 50;
    // Max vertical search range for actions like finding a root or cauldron.
    private static final int MAX_VERTICAL_INTERACTION_RANGE = 11;

    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 16.0D, 11.0D);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 16.0D, 13.0D);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    private static final float MAX_HORIZONTAL_OFFSET = 0.125F;
    // Shape defining the space required for a drip to pass through a non-solid block.
    private static final VoxelShape REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);

    public PlantopiaIcicleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TIP_DIRECTION, Direction.UP).setValue(THICKNESS, DripstoneThickness.TIP).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(TIP_DIRECTION, THICKNESS, WATERLOGGED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return isValidIciclePlacement(level, pos, state.getValue(TIP_DIRECTION));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        // Standard waterlogged logic.
        scheduleWaterTick(state, level, pos);

        // Ignore horizontal updates, as they don't affect the icicle's vertical structure.
        if (direction != Direction.UP && direction != Direction.DOWN) {
            return state;
        }

        var tipDirection = state.getValue(TIP_DIRECTION);
        // If a stalactite is already scheduled to fall, do nothing.
        if (tipDirection == Direction.DOWN && level.getBlockTicks().hasScheduledTick(pos, this)) {
            return state;
        }

        // If the supporting block is changed and the icicle can no longer survive, schedule a tick to break or fall.
        if (direction == tipDirection.getOpposite() && !canSurvive(state, level, pos)) {
            if (tipDirection == Direction.DOWN) {
                level.scheduleTick(pos, this, DELAY_BEFORE_FALLING);
            } else {
                level.scheduleTick(pos, this, STALAGMITE_BREAK_DELAY);
            }
            return state;
        }

        // Otherwise, recalculate thickness based on neighbors.
        boolean isTipMerge = state.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE;
        var newThickness = calculateIcicleThickness(level, pos, tipDirection, isTipMerge);
        return state.setValue(THICKNESS, newThickness);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onProjectileHit(@NotNull Level level, @NotNull BlockState state, @NotNull BlockHitResult hitResult, @NotNull Projectile projectile) {
        if (level.isClientSide()) return;

        var pos = hitResult.getBlockPos();

        // Tridents moving fast enough can break the icicle.
        if (projectile.mayInteract(level, pos) && projectile instanceof ThrownTrident && projectile.getDeltaMovement().length() > MIN_TRIDENT_VELOCITY_TO_BREAK_ICICLE) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        // Falling onto a stalagmite tip deals extra damage.
        if (state.getValue(TIP_DIRECTION) == Direction.UP && state.getValue(THICKNESS) == DripstoneThickness.TIP) {
            entity.causeFallDamage(fallDistance + STALAGMITE_FALL_DISTANCE_OFFSET, STALAGMITE_FALL_DAMAGE_MODIFIER, level.damageSources().stalagmite());
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.dimensionType().ultraWarm()) return; // Prevent dripping in ultra-warm dimensions like the Nether.

        float randomChance = random.nextFloat();

        if (randomChance > DRIP_PROBABILITY_PER_ANIMATE_TICK_IF_UNDER_LIQUID_SOURCE) return;
        if (!canDrip(state)) return;

        if ((randomChance < DRIP_PROBABILITY_PER_ANIMATE_TICK && level.getBiome(pos).value().warmEnoughToRain(pos)) || getLiquidSourceInfo(level, pos, state).isPresent()) {
            spawnDripParticle(level, pos, state);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        // If a stalagmite can no longer survive, destroy it.
        if (isStalagmite(state) && !canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        } else {
            // Otherwise, it must be a stalactite, which should fall.
            spawnFallingStalactite(state, level, pos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.dimensionType().ultraWarm()) return; // Prevent fluid transfer and growth in ultra-warm dimensions.

        // Attempt to transfer fluid (e.g., drip into a cauldron).
        maybeTransferFluid(state, level, pos, random.nextFloat());

        // With a certain probability, attempt to grow.
        if (random.nextFloat() < GROWTH_PROBABILITY_PER_RANDOM_TICK && isStalactiteStartPos(state, level, pos)) {
            growStalactiteOrStalagmiteIfPossible(state, level, pos, random);
        }
    }

    @VisibleForTesting
    public static void maybeTransferFluid(BlockState state, ServerLevel level, BlockPos pos, float randomChance) {
        // Optimization: perform the cheapest check first.
        if (randomChance > WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK) return;
        // Check if this is the top-most block of a stalactite.
        if (!isStalactiteStartPos(state, level, pos)) return;

        Optional<FluidInfo> fluidInfoOptional = getLiquidSourceInfo(level, pos, state);
        if (fluidInfoOptional.isEmpty()) return;

        var tipPos = findTip(state, level, pos);
        if (tipPos == null) return;

        BlockPos cauldronPos = findFillableCauldronBelowStalactiteTip(level, tipPos);
        if (cauldronPos == null) return;

        level.levelEvent(1504, tipPos, 0); // Drip effect.
        // Schedule the cauldron to be filled.
        int fallHeight = tipPos.getY() - cauldronPos.getY();
        int tickDelay = BASE_CAULDRON_FILL_TICK_DELAY + fallHeight;
        var cauldronState = level.getBlockState(cauldronPos);
        level.scheduleTick(cauldronPos, cauldronState.getBlock(), tickDelay);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var clickedFace = context.getNearestLookingVerticalDirection().getOpposite();

        var tipDirection = calculateTipDirection(level, pos, clickedFace);
        if (tipDirection == null) {
            return null; // Cannot be placed here.
        }

        // Try to merge with an adjacent icicle unless the player is sneaking.
        boolean tryMerge = !context.isSecondaryUseActive();
        var thickness = calculateIcicleThickness(level, pos, tipDirection, tryMerge);
        if (thickness == null) {
            return null;
        }

        boolean isWaterlogged = level.getFluidState(pos).getType() == Fluids.WATER;
        return defaultBlockState().setValue(TIP_DIRECTION, tipDirection).setValue(THICKNESS, thickness).setValue(WATERLOGGED, isWaterlogged);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        var thickness = state.getValue(THICKNESS);

        VoxelShape shape;
        if (thickness == DripstoneThickness.TIP_MERGE) {
            shape = TIP_MERGE_SHAPE;
        } else if (thickness == DripstoneThickness.TIP) {
            shape = state.getValue(TIP_DIRECTION) == Direction.DOWN ? TIP_SHAPE_DOWN : TIP_SHAPE_UP;
        } else if (thickness == DripstoneThickness.FRUSTUM) {
            shape = FRUSTUM_SHAPE;
        } else if (thickness == DripstoneThickness.MIDDLE) {
            shape = MIDDLE_SHAPE;
        } else {
            shape = BASE_SHAPE;
        }

        // Apply a small horizontal offset for visual variety.
        Vec3 offset = state.getOffset(level, pos);
        return shape.move(offset.x, offset.y, offset.z);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isCollisionShapeFullBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public float getMaxHorizontalOffset() {
        return MAX_HORIZONTAL_OFFSET;
    }

    @Override
    public void onBrokenAfterFall(@NotNull Level level, @NotNull BlockPos pos, @NotNull FallingBlockEntity fallingBlock) {
        if (!fallingBlock.isSilent()) {
            level.levelEvent(1045, pos, 0); // Breaking sound
        }
    }

    @Override
    public @NotNull DamageSource getFallDamageSource(@NotNull Entity entity) {
        return entity.damageSources().fallingStalactite(entity);
    }

    private static void spawnFallingStalactite(BlockState state, ServerLevel level, @NotNull BlockPos pos) {
        var mutablePos = pos.mutable();

        // Iterate downwards through all connected stalactite blocks, creating a falling entity for each.
        for (var currentState = state; isStalactite(currentState); currentState = level.getBlockState(mutablePos)) {
            var fallingBlockEntity = FallingBlockEntity.fall(level, mutablePos, currentState);

            // If this is the tip, configure fall damage.
            if (isTip(currentState, true)) {
                int height = Math.max(1 + pos.getY() - mutablePos.getY(), MIN_FALL_DAMAGE_HEIGHT);
                float damage = (float) height * STALACTITE_DAMAGE_PER_FALL_DISTANCE;
                fallingBlockEntity.setHurtsEntities(damage, STALACTITE_MAX_DAMAGE);
                break; // Damage is set, no need to continue.
            }

            mutablePos.move(Direction.DOWN);
        }
    }

    @VisibleForTesting
    public static void growStalactiteOrStalagmiteIfPossible(BlockState state, @NotNull ServerLevel level, BlockPos pos, RandomSource random) {
        // Growth is only possible in cold biomes.
        if (level.getBiome(pos).value().warmEnoughToRain(pos)) return;

        // Check for a valid source block (packed ice) and water above.
        var sourceBlockState = level.getBlockState(pos.above(1));
        var fluidBlockState = level.getBlockState(pos.above(2));
        if (!canGrow(sourceBlockState, fluidBlockState)) return;

        var tipPos = findTip(state, level, pos);
        if (tipPos == null) return;

        var tipState = level.getBlockState(tipPos);
        if (canDrip(tipState) && canTipGrow(tipState, level, tipPos)) {
            if (random.nextBoolean()) {
                // 50% chance to grow the stalactite down.
                grow(level, tipPos, Direction.DOWN);
            } else {
                // Otherwise, try to grow a stalagmite below it.
                growStalagmiteBelow(level, tipPos);
            }
        }
    }

    private static void growStalagmiteBelow(ServerLevel level, @NotNull BlockPos pos) {
        var mutablePos = pos.mutable();

        for (int i = 0; i < MAX_STALAGMITE_GROWTH_SEARCH_RANGE; i++) {
            mutablePos.move(Direction.DOWN);
            var stateAtPos = level.getBlockState(mutablePos);

            // Stop if we hit a liquid.
            if (!stateAtPos.getFluidState().isEmpty()) {
                return;
            }

            // If we find another stalagmite that can grow, extend it.
            if (isUnmergedTipWithDirection(stateAtPos, Direction.UP) && canTipGrow(stateAtPos, level, mutablePos)) {
                grow(level, mutablePos, Direction.UP);
                return;
            }

            // If we find a suitable spot for a new stalagmite...
            if (isValidIciclePlacement(level, mutablePos, Direction.UP) && !level.isWaterAt(mutablePos.below())) {
                // ...create it on the block below the current empty position.
                grow(level, mutablePos.below(), Direction.UP);
                return;
            }

            // If the block in the path is not drip-through-able, stop.
            if (!canDripThrough(level, mutablePos, stateAtPos)) {
                return;
            }
        }
    }

    private static void grow(@NotNull ServerLevel serverLevel, @NotNull BlockPos pos, Direction direction) {
        var newPos = pos.relative(direction);
        var neighborState = serverLevel.getBlockState(newPos);

        if (isUnmergedTipWithDirection(neighborState, direction.getOpposite())) {
            // If we hit the tip of another icicle, merge them.
            createMergedTips(neighborState, serverLevel, newPos);
        } else if (neighborState.isAir() || neighborState.is(Blocks.WATER)) {
            // If the space is empty or water-filled, create a new tip.
            createIcicle(serverLevel, newPos, direction, DripstoneThickness.TIP);
        }
    }

    private static void createIcicle(@NotNull LevelAccessor level, BlockPos pos, Direction direction, DripstoneThickness thickness) {
        BlockState icicleState = getIcicleBlock().defaultBlockState()
            .setValue(TIP_DIRECTION, direction)
            .setValue(THICKNESS, thickness)
            .setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);

        level.setBlock(pos, icicleState, Block.UPDATE_ALL);
    }

    private static void createMergedTips(@NotNull BlockState state, LevelAccessor level, BlockPos pos) {
        BlockPos downPos;
        BlockPos upPos;
        if (state.getValue(TIP_DIRECTION) == Direction.UP) {
            upPos = pos;
            downPos = pos.above();
        } else {
            downPos = pos;
            upPos = pos.below();
        }

        createIcicle(level, downPos, Direction.DOWN, DripstoneThickness.TIP_MERGE);
        createIcicle(level, upPos, Direction.UP, DripstoneThickness.TIP_MERGE);
    }

    private static void spawnDripParticle(Level level, BlockPos pos, @NotNull BlockState state) {
        Vec3 offset = state.getOffset(level, pos);
        double particleX = (double) pos.getX() + 0.5D + offset.x;
        double particleY = (double) ((float) (pos.getY() + 1) - STALACTITE_DRIP_START_PIXEL) - 0.0625D;
        double particleZ = (double) pos.getZ() + 0.5D + offset.z;
        // Particles are always water.
        ParticleOptions particleOptions = ParticleTypes.DRIPPING_DRIPSTONE_WATER;
        level.addParticle(particleOptions, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
    }

    @Nullable
    private static BlockPos findTip(BlockState state, LevelAccessor level, BlockPos pos) {
        if (isTip(state, false)) {
            return pos;
        }

        var direction = state.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> isSameIcicle = (currentPos, currentState) -> currentState.is(getIcicleBlock()) && currentState.getValue(TIP_DIRECTION) == direction;
        return findBlockVertical(level, pos, direction.getAxisDirection(), isSameIcicle, (tipState) -> isTip(tipState, false), MAX_GROWTH_LENGTH).orElse(null);
    }

    @Nullable
    private static Direction calculateTipDirection(LevelReader level, BlockPos pos, Direction direction) {
        // Try to place in the desired direction.
        if (isValidIciclePlacement(level, pos, direction)) {
            return direction;
        }

        // If that fails, try the opposite direction.
        if (isValidIciclePlacement(level, pos, direction.getOpposite())) {
            return direction.getOpposite();
        }

        // If both fail, placement is not possible.
        return null;
    }

    private static DripstoneThickness calculateIcicleThickness(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Direction opposite, boolean isTipMerge) {
        var oppositeDirection = opposite.getOpposite();
        var neighborState = level.getBlockState(pos.relative(opposite));

        // If the neighbor is an icicle pointing towards us, it's either a merge or a simple tip.
        if (isIcicleWithDirection(neighborState, oppositeDirection)) {
            return !isTipMerge && neighborState.getValue(THICKNESS) != DripstoneThickness.TIP_MERGE ? DripstoneThickness.TIP : DripstoneThickness.TIP_MERGE;
        }

        // If the neighbor is not an icicle pointing away from us, this must be a tip.
        if (!isIcicleWithDirection(neighborState, opposite)) {
            return DripstoneThickness.TIP;
        }

        // If the neighbor is a continuation of our icicle, determine our thickness based on it.
        var neighborThickness = neighborState.getValue(THICKNESS);
        if (neighborThickness != DripstoneThickness.TIP && neighborThickness != DripstoneThickness.TIP_MERGE) {
            var behindState = level.getBlockState(pos.relative(oppositeDirection));
            // If there's no icicle behind us, we are the base. Otherwise, we are the middle.
            return !isIcicleWithDirection(behindState, opposite) ? DripstoneThickness.BASE : DripstoneThickness.MIDDLE;
        }

        return DripstoneThickness.FRUSTUM;
    }

    public static boolean canDrip(BlockState state) {
        // Only non-waterlogged stalactite tips can drip.
        return isStalactite(state) && state.getValue(THICKNESS) == DripstoneThickness.TIP && !state.getValue(WATERLOGGED);
    }

    private static boolean canTipGrow(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos) {
        var direction = state.getValue(TIP_DIRECTION);
        var nextPos = pos.relative(direction);
        var nextState = level.getBlockState(nextPos);

        // Cannot grow into a liquid.
        if (!nextState.getFluidState().isEmpty()) {
            return false;
        }

        return nextState.isAir() || isUnmergedTipWithDirection(nextState, direction.getOpposite());
    }

    private static Optional<BlockPos> findRootBlock(Level level, BlockPos pos, @NotNull BlockState state) {
        var direction = state.getValue(TIP_DIRECTION);
        BiPredicate<BlockPos, BlockState> isSameIcicle = (currentPos, currentState) -> currentState.is(getIcicleBlock()) && currentState.getValue(TIP_DIRECTION) == direction;
        // Search opposite to the growth direction to find the first non-icicle block.
        return findBlockVertical(level, pos, direction.getOpposite().getAxisDirection(), isSameIcicle, rootState -> !rootState.is(getIcicleBlock()), MAX_VERTICAL_INTERACTION_RANGE);
    }

    private static boolean isValidIciclePlacement(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Direction direction) {
        var basePos = pos.relative(direction.getOpposite());
        var baseState = level.getBlockState(basePos);
        // Can be placed on a solid face or on another icicle.
        return baseState.isFaceSturdy(level, basePos, direction) || isIcicleWithDirection(baseState, direction);
    }

    private static boolean isTip(@NotNull BlockState state, boolean isTipMerge) {
        if (!state.is(getIcicleBlock())) {
            return false;
        }

        var thickness = state.getValue(THICKNESS);
        // It's a tip if its thickness is TIP, or if we allow checking for merges and the thickness is TIP_MERGE.
        return thickness == DripstoneThickness.TIP || (isTipMerge && thickness == DripstoneThickness.TIP_MERGE);
    }

    private static boolean isUnmergedTipWithDirection(BlockState state, Direction direction) {
        return isTip(state, false) && state.getValue(TIP_DIRECTION) == direction;
    }

    private static boolean isStalactite(BlockState state) {
        return isIcicleWithDirection(state, Direction.DOWN);
    }

    private static boolean isStalagmite(BlockState state) {
        return isIcicleWithDirection(state, Direction.UP);
    }

    public static Block getIcicleBlock() {
        return PlantopiaBlocks.ICICLE.get();
    }

    private static boolean isStalactiteStartPos(BlockState state, LevelReader level, BlockPos pos) {
        return isStalactite(state) && !level.getBlockState(pos.above()).is(getIcicleBlock());
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return false;
    }

    private static boolean isIcicleWithDirection(@NotNull BlockState state, Direction direction) {
        return state.is(getIcicleBlock()) && state.getValue(TIP_DIRECTION) == direction;
    }

    @Nullable
    private static BlockPos findFillableCauldronBelowStalactiteTip(Level level, BlockPos pos) {
        Predicate<BlockState> isFillableCauldron = state -> state.getBlock() instanceof PlantopiaAbstractCauldronBlockExtension extendedBlock && extendedBlock.plantopia$canReceiveStalactiteDrip(Fluids.WATER);
        BiPredicate<BlockPos, BlockState> canPassThrough = (currentPos, state) -> canDripThrough(level, currentPos, state);
        return findBlockVertical(level, pos, Direction.DOWN.getAxisDirection(), canPassThrough, isFillableCauldron, MAX_VERTICAL_INTERACTION_RANGE).orElse(null);
    }

    @Nullable
    public static BlockPos findStalactiteTipAboveCauldron(Level level, BlockPos pos) {
        BiPredicate<BlockPos, BlockState> canPassThrough = (currentPos, state) -> canDripThrough(level, currentPos, state);
        return findBlockVertical(level, pos, Direction.UP.getAxisDirection(), canPassThrough, PlantopiaIcicleBlock::canDrip, MAX_VERTICAL_INTERACTION_RANGE).orElse(null);
    }

    public static Fluid getCauldronFillFluidType(@NotNull ServerLevel level, BlockPos pos) {
        if (level.dimensionType().ultraWarm()) {
            return Fluids.EMPTY;
        }

        return getLiquidSourceInfo(level, pos, level.getBlockState(pos))
            .map(fluidInfo -> fluidInfo.fluid) // fluidInfo.fluid will always be water
            .orElse(Fluids.EMPTY);
    }

    private static Optional<FluidInfo> getLiquidSourceInfo(Level level, BlockPos pos, BlockState state) {
        if (!isStalactite(state)) {
            return Optional.empty();
        }

        return findRootBlock(level, pos, state).flatMap(rootPos -> {
            var sourcePos = rootPos.above();
            var sourceState = level.getBlockState(sourcePos);
            var sourceFluid = sourceState.getFluidState().getType();

            // Lava (source only) acts as a heat source, thus providing water.
            if (sourceFluid == Fluids.WATER || sourceFluid == Fluids.LAVA) {
                return Optional.of(new FluidInfo(sourcePos, Fluids.WATER, sourceState));
            }

            return Optional.empty();
        });
    }

    private static boolean canGrow(@NotNull BlockState baseBlockState, BlockState fluidSourceState) {
        return baseBlockState.is(Blocks.PACKED_ICE) && fluidSourceState.getFluidState().isSourceOfType(Fluids.WATER);
    }

    private static Optional<BlockPos> findBlockVertical(LevelAccessor level, @NotNull BlockPos pos, Direction.AxisDirection axis, BiPredicate<BlockPos, BlockState> positionalStatePredicate, Predicate<BlockState> statePredicate, int maxIterations) {
        var direction = Direction.get(axis, Direction.Axis.Y);
        var mutablePos = pos.mutable();

        for (int i = 1; i < maxIterations; i++) {
            mutablePos.move(direction);

            var candidateState = level.getBlockState(mutablePos);

            if (statePredicate.test(candidateState)) {
                return Optional.of(mutablePos.immutable());
            }

            // Stop if we go out of bounds or the path is interrupted.
            if (level.isOutsideBuildHeight(mutablePos.getY()) || !positionalStatePredicate.test(mutablePos, candidateState)) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    private static boolean canDripThrough(BlockGetter level, BlockPos pos, @NotNull BlockState state) {
        if (state.isAir()) return true;
        if (state.isSolidRender(level, pos)) return false; // Cannot drip through solid blocks.
        if (!state.getFluidState().isEmpty()) return false; // Cannot drip through liquids.

        // Check if the block's shape intersects with the "corridor" a drip needs.
        var collisionShape = state.getCollisionShape(level, pos);
        return !Shapes.joinIsNotEmpty(REQUIRED_SPACE_TO_DRIP_THROUGH_NON_SOLID_BLOCK, collisionShape, BooleanOp.AND);
    }

    record FluidInfo(BlockPos pos, Fluid fluid, BlockState sourceState) {}
}
