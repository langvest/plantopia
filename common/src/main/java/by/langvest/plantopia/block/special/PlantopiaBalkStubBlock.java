package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaStrippableBlock;
import by.langvest.plantopia.util.helper.PlantopiaFluidHelper;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.block.special.PlantopiaBalkBlock.getSegmentProperty;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.scheduleWaterTickIfNeeded;

@ParametersAreNonnullByDefault
public class PlantopiaBalkStubBlock extends Block implements SimpleWaterloggedBlock, PlantopiaStrippableBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    protected static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 8, 12);

    protected final Supplier<Block> bulkBlock;

    public PlantopiaBalkStubBlock(Properties properties, Supplier<Block> bulkBlock) {
        super(properties);
        this.bulkBlock = bulkBlock;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false).setValue(PERSISTENT, false));
    }

    public Block getBulkBlock() {
        return bulkBlock.get();
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return getBulkBlock().asItem().getDefaultInstance();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, PERSISTENT);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return PlantopiaShapeHelper.orientShape(SHAPE, state.getValue(FACING));
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        scheduleWaterTickIfNeeded(state, level, pos);

        if (!state.canSurvive(level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
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
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(PERSISTENT)) return true;

        var facing = state.getValue(FACING);
        var supportPos = pos.relative(facing.getOpposite());
        var supportState = level.getBlockState(supportPos);

        if (supportState.getBlock() instanceof PlantopiaBalkBlock) {
            return supportState.getValue(getSegmentProperty(facing));
        }

        return supportState.isFaceSturdy(level, supportPos, facing, SupportType.RIGID);
    }

    @Override
    public @Nullable BlockState getStrippedState(UseOnContext context, BlockState unstrippedState, Block strippedBlock) {
        return strippedBlock.defaultBlockState()
            .setValue(FACING, unstrippedState.getValue(FACING))
            .setValue(WATERLOGGED, unstrippedState.getValue(WATERLOGGED));
    }
}
