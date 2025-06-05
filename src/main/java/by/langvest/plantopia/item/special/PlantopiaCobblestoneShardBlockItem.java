package by.langvest.plantopia.item.special;

import by.langvest.plantopia.entity.special.PlantopiaCobblestoneShardProjectileEntity;
import by.langvest.plantopia.item.PlantopiaItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlantopiaCobblestoneShardBlockItem extends BlockItem {
	protected Block petBlock;

	public PlantopiaCobblestoneShardBlockItem(Block block, Block petBlock, Properties properties) {
		super(block, properties);
		this.petBlock = petBlock;
	}

	public Block getPetBlock() {
		return petBlock;
	}

	@Nullable
	@Override
	protected BlockState getPlacementState(@NotNull BlockPlaceContext context) {
		var itemStack = context.getItemInHand();

		if(itemStack.hasCustomHoverName()) {
			var state = getPetBlock().getStateForPlacement(context);
			return state != null && canPlace(context, state) ? state : null;
		}

		return super.getPlacementState(context);
	}

	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		var itemStack = player.getItemInHand(hand);

		level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		player.getCooldowns().addCooldown(PlantopiaItems.COBBLESTONE_SHARD.get(), 20);
		player.getCooldowns().addCooldown(PlantopiaItems.MOSSY_COBBLESTONE_SHARD.get(), 20);

		if(!level.isClientSide()) {
			var projectileEntity = new PlantopiaCobblestoneShardProjectileEntity(level, player);
			projectileEntity.setItem(itemStack);
			projectileEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
			level.addFreshEntity(projectileEntity);
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		if(!player.getAbilities().instabuild) {
			itemStack.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
	}
}
