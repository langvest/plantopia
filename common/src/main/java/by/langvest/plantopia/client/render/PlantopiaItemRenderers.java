package by.langvest.plantopia.client.render;

import by.langvest.plantopia.client.render.item.PlantopiaIconItemRenderer;
import by.langvest.plantopia.item.special.PlantopiaIconItem;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.toolkit.event.client.RegisterRenderersEvent;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlantopiaItemRenderers {
    private static final Set<Item> ICON = Sets.newHashSet();

    public static void setup(RegisterRenderersEvent.@NotNull ItemEvent event) {
        generateAll();

        event.registerAll(ICON, new PlantopiaIconItemRenderer());
    }

    private static void generateAll() {
        PlantopiaMetaBuckets.ITEM.forEach(itemMeta -> {
            if (!itemMeta.hasCustomRenderer()) return;

            var item = itemMeta.get();

            if (item instanceof PlantopiaIconItem) {
                ICON.add(item);
            }
        });
    }
}
