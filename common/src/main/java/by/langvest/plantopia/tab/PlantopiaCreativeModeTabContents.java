package by.langvest.plantopia.tab;

import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.toolkit.event.client.ModifyCreativeModeTabEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class PlantopiaCreativeModeTabContents {
    public static void setup(ModifyCreativeModeTabEvent event) {
        var tabKey = event.getTabKey();

        List<PlantopiaItemMeta> initialList = PlantopiaMetaBuckets.ITEM.getAll()
            .stream()
            .filter(itemMeta -> itemMeta.isBelongsToGroup(tabKey))
            .sorted(Comparator.comparingInt(itemMeta -> itemMeta.getOrderType().getOrder()))
            .toList();

        List<PlantopiaItemMeta> fluentList = Lists.newArrayList();
        Map<Item, LinkedList<PlantopiaItemMeta>> dependentsMap = Maps.newHashMap();

        for (var itemMeta : initialList) {
            var goesAfter = itemMeta.getGoesAfter();

            if (goesAfter != null) {
                dependentsMap.computeIfAbsent(goesAfter.asItem(), k -> Lists.newLinkedList()).addFirst(itemMeta);
            } else {
                fluentList.add(itemMeta);
            }
        }

        for (var itemMeta : fluentList) {
            event.append(itemMeta.get());
        }

        for (var entry : dependentsMap.entrySet()) {
            event.addAfter(entry.getKey(), entry.getValue().stream().map(PlantopiaItemMeta::get).toList());
        }
    }
}
