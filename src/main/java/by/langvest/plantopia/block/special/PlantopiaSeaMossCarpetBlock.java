package by.langvest.plantopia.block.special;

import by.langvest.plantopia.util.helper.PlantopiaFluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.scheduleWaterTick;

public class PlantopiaSeaMossCarpetBlock extends Block implements SimpleWaterloggedBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PlantopiaSeaMossCarpetBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        var posBelow = pos.below();
        var stateBelow = level.getBlockState(posBelow);
        var fluidState = level.getFluidState(pos);

        if (!fluidState.isEmpty() && stateBelow.is(Blocks.WATER)) {
            return false;
        }

        return !stateBelow.isAir();
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState) {
        boolean successfullyPlaced = SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);

        if (successfullyPlaced && !level.isClientSide() && !canSurvive(state, level, pos)) {
            level.destroyBlock(new BlockPos(pos), true);
        }

        return successfullyPlaced;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        scheduleWaterTick(state, level, pos);

        return copyWaterloggedFrom(level, pos, super.updateShape(state, direction, facingState, level, pos, facingPos));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
        return super.getFluidState(state);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        return copyWaterloggedFrom(context.getLevel(), context.getClickedPos(), state);
    }

    protected boolean isFloatingMossCarpet(@NotNull BlockGetter level, @NotNull BlockPos pos) {
        var posBelow = pos.below();
        var fluidStateBelow = level.getFluidState(posBelow);

        return !fluidStateBelow.isEmpty();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        super.entityInside(state, level, pos, entity);

        if (!level.isClientSide() && entity instanceof Boat && isFloatingMossCarpet(level, pos)) {
            level.destroyBlock(new BlockPos(pos), true, entity);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        boolean shoudCollide = false;

        if (context instanceof EntityCollisionContext entityCollisionContext) {
            var entity = entityCollisionContext.getEntity();

            shoudCollide = entity instanceof ItemEntity || entity == null;
        }

        if (!shoudCollide) {
            return Shapes.empty();
        }

        return super.getCollisionShape(state, level, pos, context);
    }
}
