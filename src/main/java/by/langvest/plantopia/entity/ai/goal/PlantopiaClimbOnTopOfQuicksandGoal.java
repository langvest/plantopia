package by.langvest.plantopia.entity.ai.goal;

import by.langvest.plantopia.block.special.PlantopiaQuicksandBlock;
import by.langvest.plantopia.extension.PlantopiaEntityQuicksandExtension;
import by.langvest.plantopia.tag.PlantopiaEntityTypeTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.EnumSet;

/**
 * @see ClimbOnTopOfPowderSnowGoal
 */
public class PlantopiaClimbOnTopOfQuicksandGoal extends Goal {
	protected final Mob mob;
	protected final Level level;

	public PlantopiaClimbOnTopOfQuicksandGoal(Mob mob, Level level) {
		this.mob = mob;
		this.level = level;

		setFlags(EnumSet.of(Goal.Flag.JUMP));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	public boolean canUse() {
		if(mob instanceof PlantopiaEntityQuicksandExtension quicksandExtension) {
			boolean flag = quicksandExtension.plantopia$isInQuicksand() || quicksandExtension.plantopia$wasInQuicksand();

			if(flag && mob.getType().is(PlantopiaEntityTypeTags.QUICKSAND_WALKABLE_MOBS)) {
				var posAbove = mob.blockPosition().above();
				var stateAbove = level.getBlockState(posAbove);
				return stateAbove.getBlock() instanceof PlantopiaQuicksandBlock || stateAbove.getCollisionShape(level, posAbove) == Shapes.empty();
			}
		}

		return false;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		mob.getJumpControl().jump();
	}
}
