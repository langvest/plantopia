package by.langvest.plantopia.adv;

import by.langvest.plantopia.Plantopia;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaContentHelper.plantopia;

public class PlantopiaAdvancementTabs {
	public static final PlantopiaAdvancementTab PLANTOPIA = PlantopiaAdvancementTabs.create(Plantopia.MOD_ID);

	private static @NotNull PlantopiaAdvancementTab create(String name) {
		return new PlantopiaAdvancementTab(plantopia(name));
	}
}
