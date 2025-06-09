package by.langvest.plantopia.mixin;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import by.langvest.plantopia.extension.PlantopiaZombieQuicksandExtension;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public abstract class PlantopiaZombieMixin extends Monster implements PlantopiaZombieQuicksandExtension {
	@Unique
	@SuppressWarnings("WrongEntityDataParameterClass")
	private static final EntityDataAccessor<Boolean> DATA_QUICKSAND_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);

	@Unique
	private int plantopia$inQuicksandTime;
	@Unique
	private int plantopia$quicksandConversationTime;

	protected PlantopiaZombieMixin(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	protected abstract void convertToZombieType(EntityType<? extends Zombie> entityType);

	@Shadow public abstract boolean isBaby();

	@Override
	public boolean plantopia$convertsInQuicksand() {
		return getType().equals(EntityType.ZOMBIE) || getType().equals(EntityType.DROWNED);
	}

	@Override
	public boolean plantopia$isInQuicksandConverting() {
		return getEntityData().get(DATA_QUICKSAND_CONVERSION_ID);
	}

	@Override
	public int plantopia$getInQuicksandTime() {
		return plantopia$inQuicksandTime;
	}

	@Override
	public void plantopia$setInQuicksandTime(int time) {
		plantopia$inQuicksandTime = time;
	}

	@Override
	public int plantopia$getQuicksandConversationTime() {
		return plantopia$quicksandConversationTime;
	}

	@Override
	public void plantopia$startQuicksandConversion(int time) {
		plantopia$quicksandConversationTime = time;
		getEntityData().set(DATA_QUICKSAND_CONVERSION_ID, time > -1);
	}

	@Override
	public void plantopia$doQuicksandConversion() {
		if(getType().equals(EntityType.DROWNED)) {
			if(ForgeEventFactory.canLivingConvert(this, EntityType.ZOMBIE, (timer) -> plantopia$quicksandConversationTime = timer)) {
				convertToZombieType(EntityType.ZOMBIE);
				playSound(PlantopiaSoundEvents.DROWNED_CONVERTED_TO_ZOMBIE.get(), 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
			}
		} else {
			if(ForgeEventFactory.canLivingConvert(this, EntityType.HUSK, (timer) -> plantopia$quicksandConversationTime = timer)) {
				convertToZombieType(EntityType.HUSK);
				playSound(PlantopiaSoundEvents.ZOMBIE_CONVERTED_TO_HUSK.get(), 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
			}
		}
	}

	@Inject(
		method = "defineSynchedData()V",
		at = @At("TAIL")
	)
	private void defineSynchedData$addQuicksandConversion(CallbackInfo ci) {
		getEntityData().define(DATA_QUICKSAND_CONVERSION_ID, false);
	}

	@Inject(
		method = "addAdditionalSaveData",
		at = @At("TAIL")
	)
	private void addAdditionalSaveData$addQuicksandSaveData(@NotNull CompoundTag tag, CallbackInfo ci) {
		PlantopiaEntityQuicksandExtension mob = (PlantopiaEntityQuicksandExtension)this;

		tag.putInt("InQuicksandTime", mob.plantopia$isInQuicksand() ? plantopia$getInQuicksandTime() : -1);
		tag.putInt("QuicksandConversionTime", plantopia$isInQuicksandConverting() ? plantopia$getQuicksandConversationTime() : -1);
	}

	@Inject(
		method = "readAdditionalSaveData",
		at = @At("TAIL")
	)
	private void readAdditionalSaveData$readQuicksandSaveData(@NotNull CompoundTag tag, CallbackInfo ci) {
		plantopia$setInQuicksandTime(tag.getInt("InQuicksandTime"));

		if(tag.contains("QuicksandConversionTime", Tag.TAG_ANY_NUMERIC)) {
			plantopia$startQuicksandConversion(tag.getInt("QuicksandConversionTime"));
		}
	}

	@Inject(
		method = "tick()V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/monster/Zombie;isUnderWaterConverting()Z"
		)
	)
	private void tick$updateQuicksandConversion(CallbackInfo ci) {
		PlantopiaEntityQuicksandExtension mob = (PlantopiaEntityQuicksandExtension)this;

		if(!plantopia$convertsInQuicksand()) return;

		if(plantopia$isInQuicksandConverting()) {
			plantopia$quicksandConversationTime--;

			if(plantopia$quicksandConversationTime < 0) {
				plantopia$doQuicksandConversion();
			}

			return;
		}

		if(mob.plantopia$isInQuicksand()) {
			plantopia$inQuicksandTime++;

			int thresholdTime = 520;

			if(isBaby()) thresholdTime /= 2;

			if(plantopia$inQuicksandTime >= thresholdTime) {
				plantopia$startQuicksandConversion(300);
			}

			return;
		}

		plantopia$inQuicksandTime = -1;
	}

	@Inject(
		method = "tick()V",
		at = @At("HEAD")
	)
	private void tick(CallbackInfo ci) {
		Zombie mob = (Zombie)(Object)this;
		var level = mob.level();
		var random = mob.getRandom();

		if(level.isClientSide() && plantopia$isTreating() && mob.tickCount % 40 == 0) {
			var particleAmount = Mth.nextInt(random, 2, 5);

			for(int i = 0; i < particleAmount; i++) {
				double x = mob.getX() + 0.0D + Mth.nextDouble(random, -0.4D, 0.4D);
				double y = mob.getY() + 1.0D + Mth.nextDouble(random, -0.8D, 0.8D);
				double z = mob.getZ() + 0.0D + Mth.nextDouble(random, -0.4D, 0.4D);

				level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0, 0, 0);
			}
		}
	}

	@Unique
	private boolean plantopia$isTreating() {
		Zombie mob = (Zombie)(Object)this;
		var level = mob.level();
		var state = level.getBlockState(mob.blockPosition());

		return mob.getHealth() < mob.getMaxHealth() && state.is(PlantopiaBlocks.HOGWEED.get());
	}
}
