package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static by.langvest.plantopia.util.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaBranchingShrubBlock extends PlantopiaBaseBranchingShrubBlock {
	public PlantopiaBranchingShrubBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		DeadBushBlock deadBushBlock = (DeadBushBlock)Blocks.DEAD_BUSH;

		return isBranchingShrubLikeBlock(state)
			|| deadBushBlock.mayPlaceOn(state, level, pos);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockPos posBelow = pos.below();
		BlockState stateBelow = level.getBlockState(posBelow);

		Block block = isBranchingShrubLikeBlock(stateBelow) ? getBodyBlock() : getHeadBlock();

		return copyWaterloggedFrom(level, pos, block.defaultBlockState());
	}
}
