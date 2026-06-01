package by.langvest.plantopia.client.color;

import by.langvest.toolkit.event.client.RegisterColorsEvent;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public class PlantopiaItemColors {
    private static final List<Pair<Set<Item>, ItemColor>> ITEM_COLORS = Lists.newArrayList();

    public static void add(Item item, ItemColor itemColor) {
        add(Set.of(item), itemColor);
    }

    public static void add(Set<Item> items, ItemColor itemColor) {
        ITEM_COLORS.add(Pair.of(items, itemColor));
    }

    public static void setup(RegisterColorsEvent.ItemEvent event) {
        PlantopiaColors.setupCommonColors();
        PlantopiaColors.setupItemColors(event.getBlockColors());

        event.registerAll(ITEM_COLORS);
    }
}
