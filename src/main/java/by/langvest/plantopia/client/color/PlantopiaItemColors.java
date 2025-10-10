package by.langvest.plantopia.client.color;

import by.langvest.toolkit.event.RegisterColorsEvent;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class PlantopiaItemColors {
	private static final List<Pair<Set<Item>, ItemColor>> ITEM_COLORS = Lists.newArrayList();

	public static void add(Item item, ItemColor itemColor) {
		add(Set.of(item), itemColor);
	}

	public static void add(Set<Item> items, ItemColor itemColor) {
		ITEM_COLORS.add(Pair.of(items, itemColor));
	}

	public static void setup(RegisterColorsEvent.@NotNull ItemEvent event) {
		PlantopiaColors.setupCommonColors();
		PlantopiaColors.setupItemColors(event.getBlockColors());

		event.registerAll(ITEM_COLORS);
	}
}
