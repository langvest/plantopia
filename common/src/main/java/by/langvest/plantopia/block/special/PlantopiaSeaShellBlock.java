package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaNaturalBlock;
import by.langvest.plantopia.blockentity.special.PlantopiaSeaShellBlockEntity;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

@ParametersAreNonnullByDefault
public class PlantopiaSeaShellBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, PlantopiaNaturalBlock {
    public static final VoxelShape ROUND_SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 4.0D, 12.0D);
    public static final VoxelShape TWISTY_SHAPE = Shapes.or(
        Block.box(7.0D, 0.0D, 2.0D, 12.0D, 3.0D, 11.0D),
        Block.box(4.0D, 0.0D, 7.0D, 7.0D, 3.0D, 15.0D)
    );
    public static final VoxelShape TUBE_SHAPE = Block.box(6.0D, 0.0D, 2.0D, 10.0D, 3.0D, 13.0D);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final VoxelShape shape;

    public PlantopiaSeaShellBlock(VoxelShape shape, Properties properties) {
        super(properties);
        this.shape = shape;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        var facing = state.getValue(FACING);

        return PlantopiaShapeHelper.rotateShape(shape, facing);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PlantopiaSeaShellBlockEntity(pos, state);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        var itemStack = asItem().getDefaultInstance();
        var blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof PlantopiaSeaShellBlockEntity seaShellBlockEntity) {
            seaShellBlockEntity.saveToItem(itemStack);
        }

        return itemStack;
    }

    /**
     * Called by BlockItem after this block has been placed.
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
        if (level.getBlockEntity(pos) instanceof PlantopiaSeaShellBlockEntity seaShellBlockEntity) {
            var tag = BlockItem.getBlockEntityData(itemStack);
            if (tag != null) seaShellBlockEntity.load(tag);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var clickedPos = context.getClickedPos();
        var facing = context.getHorizontalDirection().getOpposite();

        var newState = copyWaterloggedFrom(level, clickedPos, defaultBlockState().setValue(FACING, facing));

        if (!newState.canSurvive(level, clickedPos)) {
            return null;
        }

        return newState;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos posBelow = pos.below();
        return Block.canSupportCenter(level, posBelow, Direction.UP);
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
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public boolean placeNaturally(PlantopiaNaturalBlock.PlaceContext context) {
        var level = context.level();
        var pos = context.origin();
        var state = context.state();
        var random = context.random();
        var flags = context.flags();
        var newState = state.setValue(FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));

        if (!level.setBlock(pos, copyWaterloggedFrom(level, pos, newState), flags)) {
            return false;
        }

        if (level.getBlockEntity(pos) instanceof PlantopiaSeaShellBlockEntity seaShellBlockEntity) {
            seaShellBlockEntity.setRandomColor(random);
        }

        return true;
    }
}
