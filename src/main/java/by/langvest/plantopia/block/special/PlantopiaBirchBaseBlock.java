package by.langvest.plantopia.block.special;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class PlantopiaBirchBaseBlock extends DirectionalBlock {
	protected final Supplier<Block> strippedBlock;

	public PlantopiaBirchBaseBlock(Supplier<Block> strippedBlock, Properties properties) {
		super(properties);
		this.strippedBlock = strippedBlock;
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		Direction facing = context.getClickedFace();
		return defaultBlockState().setValue(FACING, facing);
	}

	@Override
	@Nullable
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action, boolean simulate) {
		if(action != ToolActions.AXE_STRIP) return super.getToolModifiedState(state, context, action, simulate);
		Direction facing = state.getValue(FACING);
		return strippedBlock.get().defaultBlockState().setValue(BlockStateProperties.AXIS, facing.getAxis());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public static MapColor logMapColor(@NotNull BlockState state) {
		Direction facing = state.getValue(BlockStateProperties.FACING);
		return facing.getAxis() == Direction.Axis.Y ? MapColor.SAND : MapColor.QUARTZ;
	}

	public static MapColor woodMapColor(@NotNull BlockState state) {
		Direction facing = state.getValue(BlockStateProperties.FACING);
		return facing == Direction.DOWN ? MapColor.COLOR_BLACK : MapColor.QUARTZ;
	}
}
