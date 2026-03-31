package by.langvest.plantopia.block.special;

import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaFluffyDandelionBlock extends FlowerBlock {
	public PlantopiaFluffyDandelionBlock(Supplier<MobEffect> effectSupplier, int effectDuration, Properties properties) {
		super(effectSupplier, effectDuration, properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if(!(level instanceof ServerLevel serverLevel)) return;
		if(serverLevel.random.nextDouble() > 0.3D) return;
		if(!(entity instanceof LivingEntity)) return;
		if(entity.getType() == EntityType.BEE) return;
		if(entity.isShiftKeyDown()) return;
		if(level.isRainingAt(pos)) return;

		if((entity.xOld != entity.getX() || entity.zOld != entity.getZ() || entity.yOld != entity.getY())) {
			double dx = Math.abs(entity.getX() - entity.xOld);
			double dz = Math.abs(entity.getZ() - entity.zOld);
			double dy = Math.abs(entity.getY() - entity.yOld);
			double threshold = 0.1D;

			if(dx >= threshold || dz >= threshold || dy >= threshold) {
				var shape = state.getShape(level, pos, CollisionContext.of(entity)).move(pos.getX(), pos.getY(), pos.getZ());
				boolean isColliding = Shapes.joinIsNotEmpty(shape, Shapes.create(entity.getBoundingBox()), BooleanOp.AND);

				if(!isColliding) return;

				spawnBatchedParticles(state, serverLevel, pos);
			}
		}
	}

	@Override
	protected void spawnDestroyParticles(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state) {
		super.spawnDestroyParticles(level, player, pos, state);

		if(level instanceof ServerLevel serverLevel) {
			spawnBatchedParticles(state, serverLevel, pos);
		}
	}

	public void spawnBatchedParticles(BlockState state, @NotNull ServerLevel level, BlockPos pos) {
		var random = level.random;
		int count = 2 + random.nextInt(2);
		var particlePos = getRandomParticlePos(state, level, pos);

		level.sendParticles(PlantopiaParticleTypes.FLUFFY_DANDELION_SEED.get(), particlePos.x(), particlePos.y(), particlePos.z(), count, 0, 0, 0, 0);
	}

	@Override
	public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(random.nextDouble() > 0.09D) return;
		if(level.isRainingAt(pos)) return;

		spawnSingleParticle(state, level, pos);
	}

	protected void spawnSingleParticle(@NotNull BlockState state, Level level, BlockPos pos) {
		var particlePos = getRandomParticlePos(state, level, pos);

		level.addParticle(PlantopiaParticleTypes.FLUFFY_DANDELION_SEED.get(), particlePos.x(), particlePos.y(), particlePos.z(), 0, 0, 0);
	}

	protected Vec3 getRandomParticlePos(@NotNull BlockState state, Level level, BlockPos pos) {
		var blockOffset = state.getOffset(level, pos);
		var random = level.random;
		double xzFactor = 0.22D;
		double yFactor = 0.08D;

		double x = pos.getX() + 0.50D + blockOffset.x() + (random.nextDouble() * xzFactor - xzFactor / 2);
		double y = pos.getY() + 0.45D + blockOffset.y() + (random.nextDouble() * yFactor - yFactor / 2);
		double z = pos.getZ() + 0.50D + blockOffset.z() + (random.nextDouble() * xzFactor - xzFactor / 2);

		return new Vec3(x, y, z);
	}
}