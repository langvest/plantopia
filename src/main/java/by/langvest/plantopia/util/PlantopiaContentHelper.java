package by.langvest.plantopia.util;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancementTab;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.PlantopiaMetaStore;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class PlantopiaContentHelper {
	public static final String POTTED_PREFIX = "potted_";
	public static final FlowerPotBlock FLOWER_POT_BLOCK = (FlowerPotBlock)Blocks.FLOWER_POT;
	public static final FireBlock FIRE_BLOCK = (FireBlock)Blocks.FIRE;
	public static final Object2FloatMap<ItemLike> COMPOSTABLES = ComposterBlock.COMPOSTABLES;
	private static List<Block> allFlowersCache = null;
	private static List<Block> allFlowersOrderedByIdCache = null;

	public static List<Block> getAllFlowers() {
		if(allFlowersCache != null) return allFlowersCache;
		Set<Block> allFlowers = Sets.newLinkedHashSet();

		allFlowers.add(Blocks.FLOWERING_AZALEA);
		allFlowers.add(Blocks.FLOWERING_AZALEA_LEAVES);
		allFlowers.add(Blocks.SPORE_BLOSSOM);
		allFlowers.add(Blocks.CHORUS_FLOWER);

		ForgeRegistries.BLOCKS.getEntries().stream().filter(blockEntry -> {
			Block block = blockEntry.getValue();
			PlantopiaBlockMeta blockMeta = PlantopiaMetaStore.getBlock(block);
			if(blockMeta != null) return blockMeta.hasItem() && blockMeta.getType().isFlowerLike();
			return block instanceof FlowerBlock || block instanceof TallFlowerBlock;
		}).forEach(blockEntry -> allFlowers.add(blockEntry.getValue()));

		return allFlowersCache = allFlowers.stream().toList();
	}

	public static List<Block> getAllFlowersOrderedById() {
		if(allFlowersOrderedByIdCache != null) return allFlowersOrderedByIdCache;
		return allFlowersOrderedByIdCache = getAllFlowers().stream().sorted(Comparator.comparing(PlantopiaContentHelper::idOf)).toList();
	}

	/* POTTED *************************************************************************************/

	@Nullable
	public static Block pottedBlockOf(Block plant) {
		Supplier<? extends Block> supplier = FLOWER_POT_BLOCK.getFullPotsView().get(locationOf(plant));
		if(supplier != null) return supplier.get();

		PlantopiaBlockMeta pottedBlockMeta = PlantopiaMetaStore.getBlock(blockMeta -> {
			Block block = blockMeta.getBlock();
			if(!(block instanceof FlowerPotBlock flowerPotBlock)) return false;
			return flowerPotBlock.getContent().equals(plant);
		});

		if(pottedBlockMeta != null) return pottedBlockMeta.getBlock();
		return null;
	}

	@Contract(pure = true)
	public static @NotNull String pottedNameOf(String baseName) {
		return POTTED_PREFIX + baseName;
	}

	public static @NotNull String pottedNameOf(Block plant) {
		String baseName = nameOf(plant);
		return pottedNameOf(baseName);
	}

	/* LOCATION *************************************************************************************/

	public static @NotNull ResourceLocation location(String namespace, String name) {
		return ResourceLocation.fromNamespaceAndPath(namespace, name);
	}

	public static @NotNull ResourceLocation location(String namespace, String... path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, String.join("/", path));
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraft(String name) {
		return location("minecraft", name);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraft(String... path) {
		return location("minecraft", path);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopia(String name) {
		return location(Plantopia.MOD_ID, name);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopia(String... path) {
		return location(Plantopia.MOD_ID, path);
	}

	public static @NotNull ResourceLocation locationOf(@NotNull RegistryObject<?> registryObject) {
		return registryObject.getId();
	}

	public static @NotNull ResourceLocation locationOf(@NotNull TagKey<?> tag) {
		return tag.location();
	}

	public static @NotNull ResourceLocation locationOf(@NotNull ResourceKey<?> key) {
		return key.location();
	}

	public static <T extends Block> @NotNull ResourceLocation locationOf(@NotNull T block) {
		return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
	}

	public static <T extends ItemLike> @NotNull ResourceLocation locationOf(@NotNull T itemLike) {
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemLike.asItem()));
	}

	public static @NotNull ResourceLocation locationOf(@NotNull PlantopiaAdvancement advancement) {
		return advancement.location();
	}

	public static @NotNull ResourceLocation locationOf(@NotNull PlantopiaAdvancementTab tab) {
		return tab.location();
	}

	/* KEY ************************************************************************************/

	public static <T extends Block> @NotNull ResourceKey<Block> keyOf(@NotNull T block) {
		return ResourceKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), locationOf(block));
	}

	public static <T extends ItemLike> @NotNull ResourceKey<Item> keyOf(@NotNull T itemLike) {
		return ResourceKey.create(ForgeRegistries.ITEMS.getRegistryKey(), locationOf(itemLike));
	}

	/* ID *************************************************************************************/

	public static @NotNull String idOf(@NotNull RegistryObject<?> registryObject) {
		return locationOf(registryObject).toString();
	}

	public static @NotNull String idOf(@NotNull TagKey<?> tag) {
		return locationOf(tag).toString();
	}

	public static @NotNull String idOf(@NotNull ResourceKey<?> key) {
		return locationOf(key).toString();
	}

	public static <T extends Block> @NotNull String idOf(@NotNull T block) {
		return locationOf(block).toString();
	}

	public static <T extends ItemLike> @NotNull String idOf(@NotNull T itemLike) {
		return locationOf(itemLike).toString();
	}

	public static @NotNull String idOf(@NotNull PlantopiaAdvancement advancement) {
		return locationOf(advancement).toString();
	}

	/* NAME *************************************************************************************/

	public static @NotNull String nameOf(@NotNull RegistryObject<?> registryObject) {
		return locationOf(registryObject).getPath();
	}

	public static @NotNull String nameOf(@NotNull TagKey<?> tag) {
		return locationOf(tag).getPath();
	}

	public static @NotNull String nameOf(@NotNull ResourceKey<?> key) {
		return locationOf(key).getPath();
	}

	public static <T extends Block> @NotNull String nameOf(@NotNull T block) {
		return locationOf(block).getPath();
	}

	public static <T extends ItemLike> @NotNull String nameOf(@NotNull T itemLike) {
		return locationOf(itemLike).getPath();
	}

	public static @NotNull String nameOf(@NotNull PlantopiaAdvancement advancement) {
		return locationOf(advancement).getPath();
	}

	public static @NotNull String nameOf(@NotNull PlantopiaAdvancementTab tab) {
		return locationOf(tab).getPath();
	}
}
