package by.langvest.plantopia.client.render;

import by.langvest.plantopia.client.render.item.PlantopiaItemStuckRenderer;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.toolkit.event.RegisterRenderersEvent;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class PlantopiaItemRenderers {
    private static final Set<Item> CUSTOM = Sets.newHashSet();

    public static void setup(RegisterRenderersEvent.@NotNull ItemEvent event) {
        generateAll();

        event.registerAll(CUSTOM, PlantopiaItemStuckRenderer.getInstance());
    }

    private static void generateAll() {
        PlantopiaMetaBuckets.ITEM.forEach(itemMeta -> {
            if(itemMeta.hasCustomRenderer()) CUSTOM.add(itemMeta.get());
        });
    }
}
