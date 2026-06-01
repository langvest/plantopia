package by.langvest.plantopia.tab;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import by.langvest.toolkit.event.RegisterEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> MAIN = registerTab("main", () -> PlantopiaBlocks.FIREWEED.get().asItem().getDefaultInstance());

    private static @NotNull ResourceKey<CreativeModeTab> registerTab(String name, Supplier<ItemStack> iconSupplier) {
        return registerTab(plantopia(name), iconSupplier);
    }

    private static @NotNull ResourceKey<CreativeModeTab> registerTab(ResourceLocation identifier, Supplier<ItemStack> iconSupplier) {
        var key = ResourceKey.create(Registries.CREATIVE_MODE_TAB, identifier);

        CreativeModeTab.DisplayItemsGenerator displayItemsGenerator = (parameters, output) -> {
            // 1. Filter items for the current tab and perform initial sort by OrderType
            List<PlantopiaItemMeta> initialList = PlantopiaMetaBuckets.ITEM.getAll()
                .stream()
                .filter(itemMeta -> itemMeta.getGroups().contains(key))
                .sorted(Comparator.comparingInt(itemMeta -> itemMeta.getOrderType().getOrder()))
                .toList();

            // 2. Separate items into roots (no dependency) and dependents (with 'goesAfter')
            List<PlantopiaItemMeta> resultList = Lists.newLinkedList();
            Map<Item, List<PlantopiaItemMeta>> dependentsMap = Maps.newHashMap();

            for (var itemMeta : initialList) {
                var goesAfter = itemMeta.getGoesAfter();

                if (goesAfter != null) {
                    var targetItem = goesAfter.asItem();
                    dependentsMap.computeIfAbsent(targetItem, k -> Lists.newArrayList()).add(itemMeta);
                } else {
                    resultList.add(itemMeta);
                }
            }

            // 3. Insert dependents into the list
            // We use a ListIterator to safely add elements while iterating
            ListIterator<PlantopiaItemMeta> iterator = resultList.listIterator();
            while (iterator.hasNext()) {
                var currentItemMeta = iterator.next();
                var currentItem = currentItemMeta.get();
                var children = dependentsMap.get(currentItem);

                if (children != null) {
                    // Add children immediately after the current item.
                    // To respect "last registered is closest", we add them in reverse order of their registration.
                    // Since initialList is sorted by registration (within an order type), we can just reverse the children list.
                    Collections.reverse(children);
                    for (var child : children) {
                        iterator.add(child);
                    }
                }
            }

            // 4. Add all sorted items to the creative tab output
            for (var itemMeta : resultList) {
                output.accept(itemMeta.get());
            }
        };

        PlantopiaRegistries.CREATIVE_MODE_TAB.register(identifier, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(iconSupplier)
            .title(Component.translatable(PlantopiaTemplateHelper.getCreativeModeTabTitleKey(identifier)))
            .displayItems(displayItemsGenerator)
            .build()
        );

        return key;
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.CREATIVE_MODE_TAB, PlantopiaRegistries.CREATIVE_MODE_TAB);
    }
}
