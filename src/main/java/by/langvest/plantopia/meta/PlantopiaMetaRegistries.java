package by.langvest.plantopia.meta;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.meta.core.PlantopiaMetaObject;
import by.langvest.plantopia.meta.core.PlantopiaMetaRegistry;
import by.langvest.plantopia.meta.core.PlantopiaMetaRegistry.LocationExtractor;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.object.PlantopiaSoundEventMeta;
import by.langvest.plantopia.util.helper.PlantopiaResourceHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public final class PlantopiaMetaRegistries {
	public static final PlantopiaMetaRegistry<Block, PlantopiaBlockMeta> BLOCKS = createRegistry("block", PlantopiaResourceHelper::locationOf);
	public static final PlantopiaMetaRegistry<Item, PlantopiaItemMeta> ITEMS = createRegistry("item", PlantopiaResourceHelper::locationOf);
	public static final PlantopiaMetaRegistry<SoundEvent, PlantopiaSoundEventMeta> SOUND_EVENTS = createRegistry("sound_event", PlantopiaResourceHelper::locationOf);
	public static final PlantopiaMetaRegistry<PlantopiaAdvancement, PlantopiaAdvancementMeta> ADVANCEMENTS = createRegistry("advancement", PlantopiaResourceHelper::locationOf);

	@Contract("_, _ -> new")
	private static <Target, Meta extends PlantopiaMetaObject<?>> @NotNull PlantopiaMetaRegistry<Target, Meta> createRegistry(String name, LocationExtractor<Target> locationExtractor) {
		return new PlantopiaMetaRegistry<>(plantopiaLocationFrom(name), locationExtractor);
	}
}