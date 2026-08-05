package by.langvest.toolkit.event.client;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public abstract class ModifyCreativeModeTabEvent extends ClientEvent implements CreativeModeTab.Output {
    public abstract CreativeModeTab getTab();

    public abstract ResourceKey<CreativeModeTab> getTabKey();

    public boolean is(List<ResourceKey<CreativeModeTab>> keys) {
        return keys.contains(getTabKey());
    }

    @SafeVarargs
    public final boolean is(ResourceKey<CreativeModeTab>... keys) {
        return is(List.of(keys));
    }

    @Override
    public void accept(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility) {
        append(Collections.singleton(stack), tabVisibility);
    }

    public abstract void prepend(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility);

    public abstract void append(Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility);

    public void append(ItemStack stack) {
        append(Collections.singleton(stack), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    public void append(ItemLike itemLike) {
        append(itemLike.asItem().getDefaultInstance());
    }

    public abstract boolean addBefore(Predicate<ItemStack> targetPredicate, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility);

    public abstract boolean addAfter(Predicate<ItemStack> targetPredicate, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility tabVisibility);

    public boolean addAfter(ItemLike anchor, Collection<? extends ItemLike> items) {
        return addAfter(
            stack -> stack.is(anchor.asItem()),
            items.stream().map(ItemLike::asItem).map(Item::getDefaultInstance).toList(),
            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
        );
    }
}
