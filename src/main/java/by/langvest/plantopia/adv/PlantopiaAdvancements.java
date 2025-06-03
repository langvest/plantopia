package by.langvest.plantopia.adv;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta.MetaType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaAdvancements {
	public static final PlantopiaAdvancement ROOT = registerAdvancement("root", PlantopiaAdvancement::new, MetaProperties.copy(MetaType.ROOT).group(Plantopia.MOD_ID).background("dirt").icon(PlantopiaBlocks.FIREWEED));
	public static final PlantopiaAdvancement COLLECT_ALL_FLOWERS = registerAdvancement("collect_all_flowers", PlantopiaAdvancement::new, MetaProperties.copy(MetaType.CHALLENGE).parent(ROOT).hidden().icon(PlantopiaItems.FLOWERS_ICON));
	public static final PlantopiaAdvancement PLACE_HOGWEED = registerAdvancement("place_hogweed", PlantopiaAdvancement::new, MetaProperties.copy(MetaType.CHILD).parent(ROOT).hidden().icon(PlantopiaBlocks.HOGWEED));

	public static @NotNull PlantopiaAdvancement registerAdvancement(String name, @NotNull Supplier<PlantopiaAdvancement> supplier, @NotNull MetaProperties metaProperties) {
		var id = plantopiaLocationFrom(name);
		var advancement = supplier.get().bindId(id);
		PlantopiaMetaRegistries.ADVANCEMENTS.associate(name, new PlantopiaAdvancementMeta(advancement, metaProperties));
		return advancement;
	}
}