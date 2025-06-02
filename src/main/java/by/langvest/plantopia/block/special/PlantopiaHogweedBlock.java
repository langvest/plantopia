package by.langvest.plantopia.block.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaQuarter;
import by.langvest.plantopia.block.PlantopiaTripleBlockHalf;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
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