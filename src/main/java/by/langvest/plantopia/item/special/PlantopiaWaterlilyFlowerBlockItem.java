package by.langvest.plantopia.item.special;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class PlantopiaWaterlilyFlowerBlockItem extends BlockItem {
	protected static final Map<Pair<Block, Block>, BlockState> FLOWERING_WATERLILIES = Maps.newHashMap();

	public PlantopiaWaterlilyFlowerBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	public static void addFloweringWaterlily(Pair<Block, Block> key, BlockState state) {
		FLOWERING_WATERLILIES.put(key, state);
	}

	@Nullable
	public static BlockState getFloweringState(Block waterlilyBlock, Block originBlock) {
		return FLOWERING_WATERLILIES.get(Pair.of(waterlilyBlock, originBlock));
	}

	@Override
	@SuppressWarnings("deprecation")
	protected @NotNull SoundEvent getPlaceSound(@NotNull BlockState state) {
		return getBlock().defaultBlockState().getSoundType().getPlaceSound();
	}

	@Override
	protected @NotNull SoundEvent getPlaceSound(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player entity) {
		return getPlaceSound(state);
	}

	@Nullable
	protected BlockState getPlacementState(@NotNull BlockPlaceContext context) {
		var targetPos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
		var level = context.getLevel();
		var targetState = level.getBlockState(targetPos);

		var floweringState = getFloweringState(getBlock(), targetState.getBlock());

		if(floweringState != null) {
			context.replaceClicked = true;
			return floweringState;
		}

		return super.getPlacementState(context);
	}
}
