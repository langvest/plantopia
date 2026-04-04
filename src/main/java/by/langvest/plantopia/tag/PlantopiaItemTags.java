package by.langvest.plantopia.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaItemTags {
	public static final TagKey<Item> IGNORED_BY_BEES = createItemTag("ignored_by_bees");
	public static final TagKey<Item> PREFERRED_BY_BEES = createItemTag("preferred_by_bees");

	public static @NotNull TagKey<Item> createItemTag(String name) {
		return TagKey.create(Registries.ITEM, plantopia(name));
	}
}