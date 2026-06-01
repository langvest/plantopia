package by.langvest.plantopia.block.special;

import by.langvest.plantopia.worldgen.damage.PlantopiaDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

@ParametersAreNonnullByDefault
public class PlantopiaThornyShrubBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PlantopiaThornyShrubBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var superState = super.getStateForPlacement(context);

        if (superState == null) return null;

        var level = context.getLevel();
        var pos = context.getClickedPos();

        return copyWaterloggedFrom(level, pos, superState);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
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
    @SuppressWarnings("deprecation")
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(entity instanceof LivingEntity)) return;

        entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));

        if (!level.isClientSide && (entity.xOld != entity.getX() || entity.zOld != entity.getZ() || entity.yOld != entity.getY())) {
            double dx = Math.abs(entity.getX() - entity.xOld);
            double dz = Math.abs(entity.getZ() - entity.zOld);
            double dy = Math.abs(entity.getY() - entity.yOld);
            double xzThreshold = 0.003D;
            double yThreshold = 0.04D;

            if (dx >= xzThreshold || dz >= xzThreshold || dy >= yThreshold) {
                entity.hurt(level.damageSources().source(PlantopiaDamageTypes.THORNY_SHRUB), 1.0F);
            }
        }
    }
}
