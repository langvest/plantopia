package by.langvest.plantopia.entity.special;

import by.langvest.plantopia.entity.PlantopiaEntities;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaFluidHelper.copyWaterloggedFrom;

public class PlantopiaCobblestoneShardProjectileEntity extends ThrowableItemProjectile {
	public static final float DAMAGE_AMOUNT = 2.25F;

	public PlantopiaCobblestoneShardProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
		super(entityType, level);
	}

	public PlantopiaCobblestoneShardProjectileEntity(Level level) {
		super(PlantopiaEntities.COBBLESTONE_SHARD.get(), level);
	}

	public PlantopiaCobblestoneShardProjectileEntity(Level level, LivingEntity shooter) {
		super(PlantopiaEntities.COBBLESTONE_SHARD.get(), shooter, level);
	}

	@Override
	protected @NotNull Item getDefaultItem() {
		return PlantopiaItems.COBBLESTONE_SHARD.get();
	}

	protected ItemParticleOption getDefaultItemParticleOption() {
		return new ItemParticleOption(PlantopiaParticleTypes.BREAKING_ITEM.get(), new ItemStack(this::getDefaultItem));
	}

	private @NotNull ParticleOptions getParticle() {
		ItemStack itemStack = this.getItemRaw();

		if(itemStack.isEmpty()) return getDefaultItemParticleOption();

		return new ItemParticleOption(PlantopiaParticleTypes.BREAKING_ITEM.get(), itemStack);
	}

	public void handleEntityEvent(byte pId) {
		if(pId == 3) {
			ParticleOptions particleoptions = getParticle();

			for(int i = 0; i < 8; i++) {
				level().addParticle(particleoptions, getX(), getY(), getZ(), 0, 0, 0);
			}
		}
	}

	protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);

		var entity = entityHitResult.getEntity();
		entity.hurt(damageSources().thrown(this, getOwner()), DAMAGE_AMOUNT);
	}

	@Override
	protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);

		var level = level();

		if(!level.isClientSide()) {
			var pos = blockHitResult.getBlockPos();
			var state = level.getBlockState(pos);

			boolean breaksIntoAir = state.is(PlantopiaBlockTags.BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS);
			boolean breaksIntoWater = state.is(PlantopiaBlockTags.BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS);

			if(breaksIntoAir || breaksIntoWater) {
				var stateToReplace = breaksIntoWater ? Blocks.WATER.defaultBlockState() : copyWaterloggedFrom(level, pos, Blocks.AIR.defaultBlockState());

				level.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.BLOCKS, 1.0F, 0.8F + level.getRandom().nextFloat() * 0.4F);
				level.levelEvent(2001, pos, Block.getId(state));
				level.setBlockAndUpdate(pos, stateToReplace);

				return;
			}

			if(state.is(Blocks.TURTLE_EGG)) {
				TurtleEggBlock turtleEggBlock = (TurtleEggBlock) Blocks.TURTLE_EGG;
				turtleEggBlock.decreaseEggs(level, pos, state);
			}
		}
	}

	protected void onHit(@NotNull HitResult hitResult) {
		super.onHit(hitResult);

		var level = level();

		if(!level.isClientSide()) {
			level.broadcastEntityEvent(this, (byte)3);
			level.playSound(null, getX(), getY(), getZ(), SoundEvents.DRIPSTONE_BLOCK_BREAK, SoundSource.NEUTRAL, 0.4F, 0.4F / level().getRandom().nextFloat() * 0.4F + 0.4F);
			discard();
		}
	}
}
