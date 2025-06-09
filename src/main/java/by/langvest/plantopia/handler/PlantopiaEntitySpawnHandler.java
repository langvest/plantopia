package by.langvest.plantopia.handler;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.entity.ai.goal.PlantopiaClimbOnTopOfQuicksandGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Plantopia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlantopiaEntitySpawnHandler {
	@SubscribeEvent
	public static void onEntityJoinWorld(@NotNull EntityJoinLevelEvent event) {
		var entity = event.getEntity();

		if(entity instanceof Mob mob) {
			if(mob instanceof Rabbit || mob instanceof Endermite || mob instanceof Silverfish) {
				mob.goalSelector.addGoal(1, new PlantopiaClimbOnTopOfQuicksandGoal(mob, mob.level()));
			}
		}
	}
}
