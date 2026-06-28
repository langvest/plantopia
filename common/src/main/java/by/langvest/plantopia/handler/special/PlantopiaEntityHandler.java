package by.langvest.plantopia.handler.special;

import by.langvest.plantopia.entity.ai.goal.PlantopiaClimbOnTopOfQuicksandGoal;
import by.langvest.plantopia.entity.ai.goal.PlantopiaZombieFindHogweedGoal;
import by.langvest.toolkit.event.game.EntitySpawnEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Zombie;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaEntityHandler {
    public static void onEntitySpawn(EntitySpawnEvent event) {
        var entity = event.getEntity();

        if (entity instanceof Mob mob) {
            if (mob instanceof Rabbit || mob instanceof Endermite || mob instanceof Silverfish) {
                mob.goalSelector.addGoal(1, new PlantopiaClimbOnTopOfQuicksandGoal(mob, mob.level()));
            }

            if (mob instanceof Zombie zombie) {
                zombie.goalSelector.addGoal(1, new PlantopiaZombieFindHogweedGoal(zombie, 1.0D));
            }
        }
    }
}
