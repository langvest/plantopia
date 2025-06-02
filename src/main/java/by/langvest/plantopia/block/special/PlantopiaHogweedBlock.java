package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHogweedBlock extends PlantopiaWideTriplePlantBlock {
	public PlantopiaHogweedBlock(Properties properties) {
		super(properties);
	}

	public static @NotNull Block getDirtBlock() {
		return PlantopiaBlocks.INFESTED_DIRT.get();
	}

	public static @NotNull Block getGrassBlock() {
		return PlantopiaBlocks.INFESTED_GRASS_BLOCK.get();
	}

	/**
	 * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
	 */
	@Override
	public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		var baseBlockPos = getBaseBlockPos(state, pos);

		if(pos != baseBlockPos) return;
		if(random.nextInt(16) != 0) return;

		var offset = state.getOffset(level, pos);
		double x = baseBlockPos.getX() + 1.0D + offset.x;
		double y = baseBlockPos.getY() + 1.5D + offset.y;
		double z = baseBlockPos.getZ() + 0.0D + offset.z;

		level.addParticle(
			ParticleTypes.SPORE_BLOSSOM_AIR,
			x + Mth.nextDouble(random, -0.25D, 0.25D),
			y + Mth.nextDouble(random, -1.0D, 1.0D),
			z + Mth.nextDouble(random, -0.25D, 0.25D),
			0.0D,
			0.0D,
			0.0D
		);
	}

	@Override
	protected boolean canNaturallyPlaceQuarterColumnAt(@NotNull BlockGetter level, @NotNull BlockPos pos) {
		var posAbove2 = pos.above(2);

		return super.canNaturallyPlaceQuarterColumnAt(level, pos)
			&& level.getBlockState(posAbove2).isAir();
	}

	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		if(random.nextInt(3) == 0) {
			var posBelow = pos.below();
			var stateBelow = level.getBlockState(posBelow);

			if(stateBelow.is(Blocks.GRASS_BLOCK)) {
				level.setBlockAndUpdate(posBelow, getGrassBlock().defaultBlockState());
			} else if(stateBelow.is(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO)) {
				level.setBlockAndUpdate(posBelow, getDirtBlock().defaultBlockState());
			}
		}
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState state) {
		return state.getValue(HALF) == PlantopiaTripleBlockHalf.LOWER;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if(level.isClientSide()) return;
		if(level.getDifficulty() == Difficulty.PEACEFUL) return;
		if(!(entity instanceof LivingEntity livingEntity)) return;
		if(livingEntity.isInvulnerable()) return;
		if(livingEntity instanceof Player player && player.isCreative()) return;

		if(livingEntity instanceof Zombie || livingEntity instanceof ZombieHorse) {
			var random = livingEntity.getRandom();

			if(random.nextInt(3) == 0 && livingEntity.getHealth() < livingEntity.getMaxHealth()) {
				livingEntity.heal(0.02F);
			}
		} else {
			if(livingEntity instanceof Enemy) return;

			livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 500));
		}
	}
}