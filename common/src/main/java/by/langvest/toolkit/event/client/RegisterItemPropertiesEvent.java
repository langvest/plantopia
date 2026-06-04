package by.langvest.toolkit.event.client;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class RegisterItemPropertiesEvent extends ClientEvent {
    public abstract void registerGeneric(ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property);

    public abstract void register(Item item, ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property);

    public void registerAll(@NotNull Set<Item> items, ResourceLocation propertyIdentifier, ClampedItemPropertyFunction property) {
        for (var item : items) {
            register(item, propertyIdentifier, property);
        }
    }
}
