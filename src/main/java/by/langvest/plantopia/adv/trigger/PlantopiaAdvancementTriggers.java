package by.langvest.plantopia.adv.trigger;

import by.langvest.plantopia.adv.trigger.special.PlantopiaBlockInteractTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class PlantopiaAdvancementTriggers {
	public static final PlantopiaBlockInteractTrigger BLOCK_INTERACT = PlantopiaBlockInteractTrigger.INSTANCE;

	public static void setup() {
		CriteriaTriggers.register(BLOCK_INTERACT);
	}
}
