package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBalkLikeBlock;
import by.langvest.plantopia.block.PlantopiaStrippableBlock;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

import static by.langvest.plantopia.block.special.PlantopiaBalkBlock.getSegmentProperty;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;
import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.scheduleWaterTickIfNeeded;

@ParametersAreNonnullByDefault
public class PlantopiaStraightBalkBlock extends Block implements SimpleWaterloggedBlock, PlantopiaStrippableBlock, PlantopiaBalkLikeBlock {
    protected static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12);

    protected final Supplier<Block> stubBlock;

    public PlantopiaStraightBalkBlock(Properties properties, Supplier<Block> stubBlock) {
        super(properties);
        this.stubBlock = stubBlock;
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false).setValue(PERSISTENT, false));
    }

    public Block getStubBlock() {
        return stubBlock.get();
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
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var clickedFace = context.getClickedFace();
        boolean isSneaking = context.getPlayer() != null && context.getPlayer().isSecondaryUseActive();
        var newState = isSneaking ? getStubBlock().defaultBlockState() : defaultBlockState();
        return copyWaterloggedFrom(level, pos, newState.setValue(FACING, clickedFace).setValue(PERSISTENT, true));
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
        var newState = strippedBlock.defaultBlockState();

        if (strippedBlock instanceof PlantopiaBalkBlock) {
            newState = PlantopiaBalkBlock.getDirectedStraightState(strippedBlock.defaultBlockState(), unstrippedState.getValue(FACING));
        }

        return newState
            .setValue(FACING, unstrippedState.getValue(FACING))
            .setValue(WATERLOGGED, unstrippedState.getValue(WATERLOGGED))
            .setValue(PERSISTENT, unstrippedState.getValue(PERSISTENT));
    }
}
