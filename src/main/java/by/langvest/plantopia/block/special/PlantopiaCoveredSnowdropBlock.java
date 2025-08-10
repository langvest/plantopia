package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaPollinableBlock;
import by.langvest.plantopia.block.entity.special.PlantopiaCoveredSnowdropBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlantopiaCoveredSnowdropBlock extends SnowLayerBlock implements EntityBlock, PlantopiaPollinableBlock {
	public PlantopiaCoveredSnowdropBlock(Properties properties) {
		super(properties);
	}

	public Block getFlowerBlock() {
		return PlantopiaBlocks.SNOWDROP.get();
	}

	@Override
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(level.getBrightness(LightLayer.BLOCK, pos) > 11) {
			level.setBlock(pos, getFlowerBlock().defaultBlockState(), level.isClientSide ? 11 : 3);
		}
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		return super.canSurvive(state, level, pos)
			&& getFlowerBlock().defaultBlockState().canSurvive(level, pos);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return getFlowerBlock().asItem().getDefaultInstance();
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new PlantopiaCoveredSnowdropBlockEntity(pos, state);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		playerWillDestroy(level, pos, state, player);
		return level.setBlock(pos, getFlowerBlock().defaultBlockState(), level.isClientSide ? 11 : 3);
	}

	@Override
	public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
		var snowState = Blocks.SNOW.defaultBlockState().setValue(LAYERS, state.getValue(LAYERS));

		super.playerDestroy(level, player, pos, snowState, null, tool);
	}

	@Override
	public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext context) {
		int layers = state.getValue(LAYERS);

		if(context.getItemInHand().is(Items.SNOW) && layers < 8) {
			if(context.replacingClickedOnBlock()) {
				return context.getClickedFace() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean isValidPollinationTarget(Level level, @NotNull BlockState state, BlockPos pos, LivingEntity entity) {
		return state.getValue(LAYERS) == 1;
	}

	public boolean skipFlowerRendering(@NotNull BlockState state) {
		return state.getValue(LAYERS) >= 6;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
		var itemInHand = player.getItemInHand(hand);

		if(!itemInHand.canPerformAction(ToolActions.SHOVEL_FLATTEN)) {
			return InteractionResult.PASS;
		}

		var layers = state.getValue(LAYERS);

		if(layers != 1) {
			return InteractionResult.PASS;
		}

		if(!level.isClientSide()) {
			Direction up = Direction.UP;

			level.playSound(null, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlock(pos, Blocks.SNOW.defaultBlockState(), 11);

			ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.2D, (double)pos.getZ() + 0.5D, getFlowerBlock().asItem().getDefaultInstance());
			itementity.setDeltaMovement(0.05D * (double)up.getStepX() + level.random.nextDouble() * 0.02D, 0.06D, 0.05D * (double)up.getStepZ() + level.random.nextDouble() * 0.02D);
			level.addFreshEntity(itementity);
			itemInHand.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(hand));
			level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
			player.awardStat(Stats.ITEM_USED.get(itemInHand.getItem()));
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}
