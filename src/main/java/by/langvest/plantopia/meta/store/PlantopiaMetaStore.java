package by.langvest.plantopia.meta.store;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.object.PlantopiaSoundEventMeta;
import by.langvest.plantopia.util.PlantopiaStoreSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static by.langvest.plantopia.util.PlantopiaContentHelper.idOf;

@SuppressWarnings("unused")
public class PlantopiaMetaStore {
	private static final PlantopiaStoreSet<PlantopiaBlockMeta> blocks = PlantopiaStoreSet.newStoreSet();
	private static final PlantopiaStoreSet<PlantopiaItemMeta> items = PlantopiaStoreSet.newStoreSet();
	private static final PlantopiaStoreSet<PlantopiaSoundEventMeta> soundEvents = PlantopiaStoreSet.newStoreSet();
	private static final PlantopiaStoreSet<PlantopiaAdvancementMeta> advancements = PlantopiaStoreSet.newStoreSet();

	public static List<PlantopiaBlockMeta> getBlocks() {
		return blocks.getAll();
	}

	public static List<PlantopiaBlockMeta> getBlocks(Predicate<PlantopiaBlockMeta> predicate) {
		return blocks.findAll(predicate);
	}

	@Nullable
	public static PlantopiaBlockMeta getBlock(Predicate<PlantopiaBlockMeta> predicate) {
		return blocks.find(predicate);
	}

	@Nullable
	public static PlantopiaBlockMeta getBlock(Block block) {
		return blocks.getById(idOf(block));
	}

	public static <T extends Block> @NotNull PlantopiaBlockMeta add(String name, RegistryObject<T> object, PlantopiaBlockMeta.MetaProperties metaProperties) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(object);
		Objects.requireNonNull(metaProperties);
		PlantopiaBlockMeta blockMeta = new PlantopiaBlockMeta(name, object, metaProperties);
		blocks.add(idOf(object), blockMeta);
		return blockMeta;
	}

	public static List<PlantopiaSoundEventMeta> getSoundEvents() {
		return soundEvents.getAll();
	}

	public static <T extends SoundEvent> @NotNull PlantopiaSoundEventMeta add(String name, RegistryObject<T> object, PlantopiaSoundEventMeta.MetaProperties metaProperties) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(object);
		Objects.requireNonNull(metaProperties);
		PlantopiaSoundEventMeta soundEventMeta = new PlantopiaSoundEventMeta(name, object, metaProperties);
		soundEvents.add(idOf(object), soundEventMeta);
		return soundEventMeta;
	}

	public static List<PlantopiaAdvancementMeta> getAdvancements() {
		return advancements.getAll();
	}

	@Nullable
	public static PlantopiaAdvancementMeta getAdvancement(PlantopiaAdvancement advancement) {
		return advancements.getById(idOf(advancement));
	}

	public static @NotNull PlantopiaAdvancementMeta add(String name, PlantopiaAdvancement object, PlantopiaAdvancementMeta.MetaProperties metaProperties) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(object);
		Objects.requireNonNull(metaProperties);
		PlantopiaAdvancementMeta advancementMeta = new PlantopiaAdvancementMeta(name, object, metaProperties);
		advancements.add(idOf(object), advancementMeta);
		return advancementMeta;
	}

	public static List<PlantopiaItemMeta> getItems() {
		return items.getAll();
	}

	@Nullable
	public static PlantopiaItemMeta getItem(Item item) {
		return items.getById(idOf(item));
	}

	public static <T extends Item> @NotNull PlantopiaItemMeta add(String name, RegistryObject<T> object, PlantopiaItemMeta.MetaProperties metaProperties) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(object);
		Objects.requireNonNull(metaProperties);
		PlantopiaItemMeta itemMeta = new PlantopiaItemMeta(name, object, metaProperties);
		items.add(idOf(object), itemMeta);
		return itemMeta;
	}
}