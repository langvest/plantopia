package by.langvest.plantopia.forge.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.entity.ai.goal.PlantopiaClimbOnTopOfQuicksandGoal;
import by.langvest.plantopia.entity.ai.goal.PlantopiaZombieFindHogweedGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class PlantopiaEntitySpawnHandler {
    @SubscribeEvent
    public static void onEntityJoinWorld(@NotNull EntityJoinLevelEvent event) {
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
