package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.misc.PlantopiaDamageTypes;
import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import by.langvest.plantopia.extension.PlantopiaZombieQuicksandExtension;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.tag.PlantopiaEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static by.langvest.plantopia.util.helper.PlantopiaColorHelper.hexToRgb;

public class PlantopiaQuicksandBlock extends SandBlock implements BucketPickup {
	public static final int DUST_COLOR = hexToRgb("#DBD3A0");
	private static final VoxelShape FALLING_COLLISION_SHAPE = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.9D, 1.0D);

	public PlantopiaQuicksandBlock(BlockBehaviour.Properties properties) {
		super(DUST_COLOR, properties);
	}

	public ItemStack getBucket() {
		return PlantopiaItems.QUICKSAND_BUCKET.get().getDefaultInstance();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean skipRendering(@NotNull BlockState state, @NotNull BlockState adjacentState, @NotNull Direction direction) {
		return adjacentState.is(this) || super.skipRendering(state, adjacentState, direction);
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public float getDestroyProgress(@NotNull BlockState state, @NotNull Player player, @NotNull BlockGetter level, @NotNull BlockPos pos) {
		var destroyProgress = super.getDestroyProgress(state, player, level, pos);

		if(player.getFeetBlockState().is(this)) destroyProgress /= 5.0F;

		return destroyProgress;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		boolean flag1 = entity instanceof Silverfish || entity instanceof Endermite;
		boolean flag2 = !(entity instanceof LivingEntity) || entity.getFeetBlockState().is(this);

		if(!flag1 && flag2) {
			double xzSpeed = 0.5F;

			if(entity.xOld != entity.getX() || entity.zOld != entity.getZ()) {
				entity.makeStuckInBlock(state, new Vec3(xzSpeed, 0.3D, xzSpeed));

				if(level.isClientSide()) {
					var random = level.getRandom();

					if(random.nextBoolean()) {
						level.addParticle(
							PlantopiaParticleTypes.QUICKSAND.get(),
							entity.getX(),
							pos.getY() + 1,
							entity.getZ(),
							Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F,
							0.05F,
							Mth.randomBetween(random, -1.0F, 1.0F) * 0.083333336F
						);
					}
				}
			} else {
				entity.makeStuckInBlock(state, new Vec3(xzSpeed, 0.04D, xzSpeed));
			}
		}

		if(!level.isClientSide() && !isEntityImmuneToQuicksand(entity) && isEntityDrownsInQuicksand(entity)) {
			float damage = 1.0F;

			if(entity instanceof PlantopiaZombieQuicksandExtension zombieQuicksandExtension) {
				if(zombieQuicksandExtension.plantopia$isInQuicksandConverting()) damage /= 2.0F;
			}

			entity.hurt(entity.damageSources().source(PlantopiaDamageTypes.QUICKSAND), damage);
		}

		if(entity instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
			quicksandExtension.plantopia$setIsInQuicksand(true);
		}
	}

	public static boolean isEntityDrownsInQuicksand(@NotNull Entity entity) {
		var level = entity.level();
		var eyeBlockPos = BlockPos.containing(entity.getEyePosition());
		var eyeBlockState = level.getBlockState(eyeBlockPos);

		return eyeBlockState.is(PlantopiaBlocks.QUICKSAND.get());
	}

	public static boolean isEntityImmuneToQuicksand(@NotNull Entity entity) {
		return entity.getType().is(PlantopiaEntityTypeTags.QUICKSAND_IMMUNE_ENTITY_TYPES);
	}

	public static boolean canEntityWalkOnQuicksand(@NotNull Entity entity) {
		if(entity.getType().is(PlantopiaEntityTypeTags.QUICKSAND_WALKABLE_MOBS)) {
			return true;
		}

		if(entity instanceof LivingEntity livingEntity) {
			return livingEntity.getItemBySlot(EquipmentSlot.FEET).is(Items.LEATHER_BOOTS);
		}

		return false;
	}

	@Override
	public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
		if(fallDistance < 4.0F) return;
		if(!(entity instanceof LivingEntity livingEntity)) return;

		var fallSounds = livingEntity.getFallSounds();
		var soundEvent = fallDistance < 7.0F ? fallSounds.small() : fallSounds.big();
		entity.playSound(soundEvent, 1.0F, 1.0F);
	}

	@Override
	protected void falling(@NotNull FallingBlockEntity entity) {
		entity.dropItem = false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		if(context instanceof EntityCollisionContext entityCollisionContext) {
			var entity = entityCollisionContext.getEntity();

			if(entity != null) {
				if(entity instanceof FallingBlockEntity || canEntityWalkOnQuicksand(entity) && context.isAbove(Shapes.block(), pos, false) && !context.isDescending()) {
					return Shapes.block();
				}

				if(entity.fallDistance > 0.5F) {
					return FALLING_COLLISION_SHAPE;
				}
			}
		}

		return Shapes.empty();
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public @NotNull ItemStack pickupBlock(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
		level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);

		if(!level.isClientSide()) {
			level.levelEvent(2001, pos, Block.getId(state));
		}

		return getBucket();
	}

	@Override
	public @NotNull Optional<SoundEvent> getPickupSound() {
		return Optional.of(SoundEvents.BUCKET_FILL_POWDER_SNOW);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
		return true;
	}
}
