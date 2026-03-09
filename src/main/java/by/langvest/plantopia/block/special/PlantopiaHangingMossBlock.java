package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlockStateProperties;
import by.langvest.plantopia.util.helper.PlantopiaShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaHangingMossBlock extends Block implements BonemealableBlock {
    protected static final VoxelShape SHAPE_BASE = PlantopiaShapeHelper.column(14.0, 0.0, 16.0);
    protected static final VoxelShape SHAPE_TIP = PlantopiaShapeHelper.column(14.0, 2.0, 16.0);
    public static final BooleanProperty TIP = PlantopiaBlockStateProperties.TIP;

    public PlantopiaHangingMossBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TIP, true));
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(TIP) ? SHAPE_TIP : SHAPE_BASE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return canStayAtPosition(level, pos);
    }

    protected boolean canStayAtPosition(@NotNull BlockGetter level, @NotNull BlockPos pos) {
        var posAbove = pos.relative(Direction.UP);
        var stateAbove = level.getBlockState(posAbove);
        return stateAbove.is(this) || MultifaceBlock.canAttachTo(level, Direction.UP, posAbove, stateAbove);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if (!canSurvive(state, level, pos)) {
            level.scheduleTick(pos, this, 1);
        }

        var newState = super.updateShape(state, direction, facingState, level, pos, facingPos);

        if (newState.is(this)) {
            boolean isTip = !level.getBlockState(pos.below()).is(this);
            newState = newState.setValue(TIP, isTip);
        }

        return copyWaterloggedFrom(level, pos, newState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(TIP);
    }

    protected boolean canGrowInto(@NotNull BlockState state) {
        return state.isAir();
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state, boolean isClient) {
        var candidatePos = getTipPos(level, pos).below();
        var candidateState = level.getBlockState(candidatePos);
        return canGrowInto(candidateState);
    }

    public BlockPos getTipPos(@NotNull BlockGetter level, @NotNull BlockPos pos) {
        var mutablePos = pos.mutable();

        BlockState state;
        do {
            mutablePos.move(Direction.DOWN);
            state = level.getBlockState(mutablePos);
        } while (state.is(this));

        return mutablePos.relative(Direction.UP).immutable();
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        var candidatePos = getTipPos(level, pos).below();
        var candidateState = level.getBlockState(candidatePos);

        if (canGrowInto(candidateState)) {
            var newState = state.setValue(TIP, true);
            level.setBlockAndUpdate(candidatePos, copyWaterloggedFrom(level, candidatePos, newState));
        }
    }
}
