package by.langvest.plantopia.entity.goal;

import by.langvest.plantopia.block.PlantopiaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PlantopiaZombieFindHogweedGoal extends Goal {
	protected final Zombie mob;
	protected final float thresholdHealthLevel;
	protected final float thresholdHealthLevel2;
	private double wantedX;
	private double wantedY;
	private double wantedZ;
	private boolean isTreating;
	private final double speedModifier;
	private final Level level;

	public PlantopiaZombieFindHogweedGoal(@NotNull Zombie mob, double speedModifier) {
		this.mob = mob;
		this.speedModifier = speedModifier;
		this.thresholdHealthLevel = mob.getMaxHealth() / 3.0F;
		this.thresholdHealthLevel2 = thresholdHealthLevel * 2;
		this.level = mob.level();
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		if(isTreating) {
			if(shouldContinueTreating()) {
				boolean isFoundPosToGo = isAlreadyInSafeHogweedPos() || setWantedPos();
				return isTreating = isFoundPosToGo;
			} else {
				return isTreating = false;
			}
		} else {
			if(shouldBeTreated()) {
				boolean isFoundPosToGo = isAlreadyInSafeHogweedPos() || setWantedPos();
				return isTreating = isFoundPosToGo;
			} else {
				if(mob.getTarget() != null) return false;
				if(mob.getNavigation().isInProgress()) return false;
			}
		}

		var random = mob.getRandom();

		if(random.nextInt(3) == 0) {
			if(isAlreadyInSafeHogweedPos()) return true;
			return setWantedPos();
		}

		return false;
	}

	protected boolean shouldBeTreated() {
		return mob.getHealth() < thresholdHealthLevel;
	}

	protected boolean shouldContinueTreating() {
		return mob.getHealth() < thresholdHealthLevel2;
	}

	protected boolean setWantedPos() {
		var vec3 = getHogweedPos();

		if(vec3 == null) return false;

		wantedX = vec3.x;
		wantedY = vec3.y;
		wantedZ = vec3.z;

		return true;
	}

	protected boolean isSafeHogweedPos(BlockPos pos) {
		if(level.isDay() && level.canSeeSky(pos)) {
			if(!level.isRainingAt(pos)) return false;
		}

		var state = level.getBlockState(pos);

		return state.is(PlantopiaBlocks.HOGWEED.get());
	}

	protected boolean isAlreadyInSafeHogweedPos() {
		var pos = mob.blockPosition();

		return isSafeHogweedPos(pos);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return !mob.getNavigation().isDone();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		mob.getNavigation().moveTo(wantedX, wantedY, wantedZ, speedModifier);
	}

	@Nullable
	protected Vec3 getHogweedPos() {
		var random = mob.getRandom();
		var pos = mob.blockPosition();

		for(int i = 0; i < 10; i++) {
			var candidatePos = pos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);

			if(isSafeHogweedPos(candidatePos) && mob.getWalkTargetValue(candidatePos) > 0.0D) {
				return Vec3.atBottomCenterOf(candidatePos);
			}
		}

		return null;
	}
}
