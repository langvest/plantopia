package by.langvest.plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaMapleLeavesBlock extends LeavesBlock {
	private final Supplier<SimpleParticleType> leavesParticleType;

	public PlantopiaMapleLeavesBlock(Supplier<SimpleParticleType> leavesParticleType, Properties properties) {
		super(properties);
		this.leavesParticleType = leavesParticleType;
	}

	/**
	 * Called periodically clientside on blocks near the player to show effects (like furnace fire particles).
	 */
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);

		if (random.nextInt(36) == 0) {
			var posBelow = pos.below();
			var stateBelow = level.getBlockState(posBelow);
			if (!isFaceFull(stateBelow.getCollisionShape(level, posBelow), Direction.UP)) {
				ParticleUtils.spawnParticleBelow(level, pos, random, leavesParticleType.get());
			}
		}
	}
}
