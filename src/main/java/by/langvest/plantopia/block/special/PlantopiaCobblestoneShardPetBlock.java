package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.entity.special.PlantopiaCobblestoneShardPetBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaCobblestoneShardPetBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 4.0D, 11.0D);
	protected final Supplier<Block> originBlock;

	public PlantopiaCobblestoneShardPetBlock(Supplier<Block> originBlock, Properties properties) {
		super(properties);
		this.originBlock = originBlock;
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, @NotNull BlockGetter level, BlockPos pos, Player player) {
		var itemStack = getOriginBlock().asItem().getDefaultInstance();
		var blockEntity = level.getBlockEntity(pos);

		if(blockEntity instanceof PlantopiaCobblestoneShardPetBlockEntity petBlockEntity) {
			var customName = petBlockEntity.getCustomName();

			if(customName != null) {
				itemStack.setHoverName(customName);
			}
		}

		return itemStack;
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity player, @NotNull ItemStack itemStack) {
		if(!itemStack.hasCustomHoverName()) return;

		var blockEntity = level.getBlockEntity(pos);

		if(!(blockEntity instanceof PlantopiaCobblestoneShardPetBlockEntity petBlockEntity)) return;

		petBlockEntity.setCustomName(itemStack.getHoverName());
	}

	public Block getOriginBlock() {
		return this.originBlock.get();
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		var superState = super.getStateForPlacement(context);

		if(superState == null) return null;

		var level = context.getLevel();
		var pos = context.getClickedPos();
		var facing = context.getHorizontalDirection();

		return copyWaterloggedFrom(level, pos, superState.setValue(FACING, facing));
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		return Block.canSupportCenter(level, pos.below(), Direction.UP);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		if(direction == Direction.DOWN && !canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
		if(state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	@Override
	public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new PlantopiaCobblestoneShardPetBlockEntity(pos, state);
	}
}
