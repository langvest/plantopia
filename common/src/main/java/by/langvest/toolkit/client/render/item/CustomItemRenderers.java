package by.langvest.toolkit.client.render.item;

import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CustomItemRenderers {
    private static final Map<Item, CustomItemRenderer> storage = Maps.newConcurrentMap();

    public static void register(Item item, CustomItemRenderer renderer) {
        storage.put(item, renderer);
    }

    @Nullable
    public static CustomItemRenderer getCustomRenderer(Item item) {
        return storage.get(item);
    }
}
