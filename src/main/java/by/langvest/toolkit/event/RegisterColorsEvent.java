package by.langvest.toolkit.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public abstract class RegisterColorsEvent<T, C> extends Event {
	protected final Registrar<T, C> registrar;

	public RegisterColorsEvent(Registrar<T, C> registrar) {
		this.registrar = registrar;
	}

	public void register(T element, C color) {
		registerAll(Set.of(element), color);
	}

	protected abstract T[] toArray(Set<T> set);

	public void registerAll(@NotNull Set<T> elements, C color) {
		if(elements.isEmpty()) return;
		registrar.register(color, toArray(elements));
	}

	public void registerAll(@NotNull List<Pair<Set<T>, C>> list) {
		for(Pair<Set<T>, C> pair : list) {
			registerAll(pair.getFirst(), pair.getSecond());
		}
	}

	public static class Block extends RegisterColorsEvent<net.minecraft.world.level.block.Block, BlockColor> {
		protected final BlockColors blockColors;

		@SuppressWarnings("deprecation")
		public Block(@NotNull BlockColors blockColors) {
			super(blockColors::register);
			this.blockColors = blockColors;
		}

		public BlockColors getBlockColors() {
			return blockColors;
		}

		@Override
		protected net.minecraft.world.level.block.Block[] toArray(@NotNull Set<net.minecraft.world.level.block.Block> set) {
			return set.toArray(net.minecraft.world.level.block.Block[]::new);
		}
	}

	public static class Item extends RegisterColorsEvent<net.minecraft.world.item.Item, ItemColor> {
		private final ItemColors itemColors;
		private final BlockColors blockColors;

		@SuppressWarnings("deprecation")
		public Item(@NotNull ItemColors itemColors, BlockColors blockColors) {
			super(itemColors::register);
			this.itemColors = itemColors;
			this.blockColors = blockColors;
		}

		public ItemColors getItemColors() {
			return itemColors;
		}

		public BlockColors getBlockColors() {
			return blockColors;
		}


		@Override
		protected net.minecraft.world.item.Item[] toArray(@NotNull Set<net.minecraft.world.item.Item> set) {
			return set.toArray(net.minecraft.world.item.Item[]::new);
		}
	}

	@FunctionalInterface
	public interface Registrar<T, C> {
		void register(C color, T[] elements);
	}
}
