package by.langvest.plantopia.block.special;

import by.langvest.plantopia.entity.PlantopiaDamageTypes;
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

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaThornyShrubBlock extends Block implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PlantopiaThornyShrubBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var superState = super.getStateForPlacement(context);

		if(superState == null) return null;

		var level = context.getLevel();
		var pos = context.getClickedPos();

		return copyWaterloggedFrom(level, pos, superState);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		if(state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if(!(entity instanceof LivingEntity livingEntity)) return;

		livingEntity.makeStuckInBlock(state, new Vec3(0.8F, 0.75D, 0.8F));

		if(!level.isClientSide && (livingEntity.xOld != livingEntity.getX() || livingEntity.zOld != livingEntity.getZ())) {
			double dx = Math.abs(livingEntity.getX() - livingEntity.xOld);
			double dz = Math.abs(livingEntity.getZ() - livingEntity.zOld);

			if(dx >= (double)0.003F || dz >= (double)0.003F) {
				livingEntity.hurt(level.damageSources().source(PlantopiaDamageTypes.THORNY_SHRUB), 1.0F);
			}
		}
	}
}
