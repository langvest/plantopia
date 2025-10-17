package by.langvest.plantopia.meta;

import by.langvest.toolkit.meta.MetaObject;
import by.langvest.toolkit.meta.MetaBucket;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.object.PlantopiaSoundEventMeta;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public final class PlantopiaMetaBuckets {
	public static final MetaBucket<PlantopiaBlockMeta> BLOCK = createBucket("block");
	public static final MetaBucket<PlantopiaItemMeta> ITEM = createBucket("item");
	public static final MetaBucket<PlantopiaSoundEventMeta> SOUND_EVENT = createBucket("sound_event");
	public static final MetaBucket<PlantopiaAdvancementMeta> ADVANCEMENT = createBucket("advancement");

	private static <Meta extends MetaObject<?>> @NotNull MetaBucket<Meta> createBucket(String name) {
		return new MetaBucket<>(plantopia(name));
	}
}