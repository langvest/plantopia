package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaHogweedBlock extends PlantopiaWideTriplePlantBlock {
	public PlantopiaHogweedBlock(Properties properties) {
		super(properties);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
		super.randomTick(state, level, pos, random);
	}

	@Override
	public boolean isRandomlyTicking(@NotNull BlockState state) {
		return state.getValue(HALF) == PlantopiaTripleBlockHalf.LOWER
			&& state.getValue(QUARTER) == PlantopiaQuarter.SOUTH_WEST;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
		if(level.isClientSide()) return;
		if(level.getDifficulty() == Difficulty.PEACEFUL) return;
		if(!(entity instanceof LivingEntity livingEntity)) return;
		if(livingEntity.isInvulnerable()) return;
		if(livingEntity instanceof Player player && player.isCreative()) return;
		livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 500));
	}
}