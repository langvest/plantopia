package by.langvest.plantopia.item.special;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class PlantopiaWaterlilyItem extends Item implements SuspiciousEffectHolder {
	protected static final Map<Pair<Item, Block>, BlockState> FLOWERING_WATERLILIES = Maps.newHashMap();

	public PlantopiaWaterlilyItem(Properties properties) {
		super(properties);
	}

	public static void addFloweringWaterlily(Pair<Item, Block> key, BlockState state) {
		FLOWERING_WATERLILIES.put(key, state);
	}

	@Nullable
	public static BlockState getFloweringState(Item waterlilyItem, Block originBlock) {
		return FLOWERING_WATERLILIES.get(Pair.of(waterlilyItem, originBlock));
	}

	/**
	 * Called when this item is used when targeting a Block
	 */
	@Override
	public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
		return place(new BlockPlaceContext(context));
	}

	protected BlockPos getTargetPos(@NotNull BlockPlaceContext context) {
		return context.getClickedPos().relative(context.getClickedFace().getOpposite());
	}

	protected boolean placeBlock(@NotNull BlockPlaceContext context, BlockState state) {
		return context.getLevel().setBlock(getTargetPos(context), state, 11);
	}

	@Nullable
	protected BlockState getPlacementState(@NotNull BlockPlaceContext context) {
		var targetPos = getTargetPos(context);
		var level = context.getLevel();
		var state = level.getBlockState(targetPos);

		return getFloweringState(this, state.getBlock());
	}

	public InteractionResult place(@NotNull BlockPlaceContext context) {
		var stateToPlace = getPlacementState(context);

		if(stateToPlace == null) {
			return InteractionResult.FAIL;
		}

		if(!stateToPlace.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
			return InteractionResult.FAIL;
		}

		if(!placeBlock(context, stateToPlace)) {
			return InteractionResult.FAIL;
		}

		var targetPos = getTargetPos(context);
		var level = context.getLevel();
		var player = context.getPlayer();
		var itemStack = context.getItemInHand();
		var actualState = level.getBlockState(targetPos);

		if(actualState.is(stateToPlace.getBlock())) {
			actualState.getBlock().setPlacedBy(level, targetPos, actualState, player, itemStack);

			if(player instanceof ServerPlayer serverPlayer) {
				CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, targetPos, itemStack);
			}
		}

		var soundType = actualState.getSoundType(level, targetPos, context.getPlayer());

		level.playSound(player, targetPos, getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
		level.gameEvent(GameEvent.BLOCK_PLACE, targetPos, GameEvent.Context.of(player, actualState));

		if(player == null || !player.getAbilities().instabuild) {
			itemStack.shrink(1);
		}

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	protected SoundEvent getPlaceSound() {
		return SoundEvents.CHERRY_LEAVES_PLACE;
	}

	@Override
	public @NotNull MobEffect getSuspiciousEffect() {
		return MobEffects.CONFUSION;
	}

	@Override
	public int getEffectDuration() {
		return 180; // 9 * 20
	}
}
