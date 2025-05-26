package by.langvest.plantopia.adv;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta.MetaType;
import by.langvest.plantopia.meta.PlantopiaMetaStore;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaContentHelper.plantopia;

public class PlantopiaAdvancements {
	public static final PlantopiaAdvancement ROOT = registerAdvancement("root", PlantopiaAdvancement::new, MetaProperties.of(MetaType.ROOT).group(PlantopiaAdvancementTabs.PLANTOPIA).background("dirt").icon(PlantopiaBlocks.FIREWEED));
	public static final PlantopiaAdvancement COLLECT_ALL_FLOWERS = registerAdvancement("collect_all_flowers", PlantopiaAdvancement::new, MetaProperties.of(MetaType.CHALLENGE).parent(ROOT).icon(PlantopiaItems.FLOWERS_ICON));

	public static @NotNull PlantopiaAdvancement registerAdvancement(String name, @NotNull Supplier<PlantopiaAdvancement> supplier, @NotNull MetaProperties metaProperties) {
		PlantopiaAdvancement advancement = supplier.get().bindLocation(plantopia(name));
		PlantopiaMetaStore.add(name, advancement, metaProperties);
		return advancement;
	}
}