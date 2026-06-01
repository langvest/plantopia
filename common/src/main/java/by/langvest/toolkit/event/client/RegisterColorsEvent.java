package by.langvest.toolkit.event.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public abstract class RegisterColorsEvent extends ClientEvent {
    public static abstract class BlockEvent extends RegisterColorsEvent {
        protected final BlockColors blockColors;

        public BlockEvent(@NotNull BlockColors blockColors) {
            this.blockColors = blockColors;
        }

        public BlockColors getBlockColors() {
            return blockColors;
        }

        protected abstract void register(BlockColor color, Block... blocks);

        public void register(Block block, BlockColor color) {
            register(color, block);
        }

        public void registerAll(@NotNull Set<Block> blocks, BlockColor color) {
            if (blocks.isEmpty()) return;
            register(color, blocks.toArray(Block[]::new));
        }

        public void registerAll(@NotNull Collection<Pair<Set<Block>, BlockColor>> colors) {
            for (var pair : colors) {
                registerAll(pair.getFirst(), pair.getSecond());
            }
        }
    }

    public static abstract class ItemEvent extends RegisterColorsEvent {
        private final ItemColors itemColors;
        private final BlockColors blockColors;

        public ItemEvent(@NotNull ItemColors itemColors, BlockColors blockColors) {
            this.itemColors = itemColors;
            this.blockColors = blockColors;
        }

        public ItemColors getItemColors() {
            return itemColors;
        }

        public BlockColors getBlockColors() {
            return blockColors;
        }

        protected abstract void register(ItemColor color, Item... items);

        public void register(Item item, ItemColor color) {
            register(color, item);
        }

        public void registerAll(@NotNull Set<Item> items, ItemColor color) {
            if (items.isEmpty()) return;
            register(color, items.toArray(Item[]::new));
        }

        public void registerAll(@NotNull Collection<Pair<Set<Item>, ItemColor>> colors) {
            for (var pair : colors) {
                registerAll(pair.getFirst(), pair.getSecond());
            }
        }
    }
}
