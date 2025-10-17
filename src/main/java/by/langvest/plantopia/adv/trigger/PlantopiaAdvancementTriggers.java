package by.langvest.plantopia.adv.trigger;

import by.langvest.plantopia.adv.trigger.special.PlantopiaBlockInteractTrigger;
import by.langvest.toolkit.event.LifecycleEvent;
import net.minecraft.advancements.CriteriaTriggers;

public class PlantopiaAdvancementTriggers {
	public static final PlantopiaBlockInteractTrigger BLOCK_INTERACT = PlantopiaBlockInteractTrigger.INSTANCE;

	public static void setup(LifecycleEvent.CommonSetupEvent event) {
		CriteriaTriggers.register(BLOCK_INTERACT);
	}
}
