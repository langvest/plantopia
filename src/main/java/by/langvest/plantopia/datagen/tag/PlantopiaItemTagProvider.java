package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaSeaShellBlock;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.util.helper.PlantopiaResourceHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaItemTagProvider extends ItemTagsProvider {
	private final PlantopiaTagSet<Item> TALL_FLOWERS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> SMALL_FLOWERS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> LEAVES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> SAPLINGS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> IGNORED_BY_BEES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> PREFERRED_BY_BEES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> BIRCH_LOGS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> DIRT = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Item> SEA_SHELL = PlantopiaTagSet.newTagSet();

	public PlantopiaItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, PlantopiaBlockTagProvider.getInstance().contentsGetter(), Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		generateAll();

		add(IGNORED_BY_BEES, Blocks.WITHER_ROSE);
		add(BIRCH_LOGS, PlantopiaBlocks.BIRCH_BASE_LOG.get(), PlantopiaBlocks.BIRCH_BASE_WOOD.get());

		saveAll();
	}

	private void add(@NotNull PlantopiaTagSet<Item> tagSet, ItemLike... items) {
		tagSet.add(Arrays.stream(items).map(ItemLike::asItem).toArray(Item[]::new));
	}

	@SafeVarargs
	@SuppressWarnings("unused")
	private void add(@NotNull PlantopiaTagSet<Item> tagSet, TagKey<Item>... tags) {
		tagSet.addTags(tags);
	}

	private void generateAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
			if(!blockMeta.hasItem()) return;

			var block = blockMeta.getBlock();
			var item = block.asItem();
			var type = blockMeta.getType();
			int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();

			if(blockMeta.isIgnoredByBees()) IGNORED_BY_BEES.add(item);
			if(blockMeta.isPreferredByBees()) PREFERRED_BY_BEES.add(item);

			if(type.instanceOf(PlantopiaBlockMeta.MetaType.FLOWER)) {
				if(baseHeight > 1) TALL_FLOWERS.add(item);
				else SMALL_FLOWERS.add(item);
			}

			if(type.instanceOf(PlantopiaBlockMeta.MetaType.LEAVES)) LEAVES.add(item);
			if(type.instanceOf(PlantopiaBlockMeta.MetaType.SAPLING)) SAPLINGS.add(item);
			if(type.instanceOf(PlantopiaBlockMeta.MetaType.DIRT)) DIRT.add(item);

			if(block instanceof PlantopiaSeaShellBlock) {
				SEA_SHELL.add(item);
			}
		});
	}

	private void saveAll() {
		save(ItemTags.DIRT, DIRT);
		save(ItemTags.BIRCH_LOGS, BIRCH_LOGS);
		save(ItemTags.TALL_FLOWERS, TALL_FLOWERS);
		save(ItemTags.SMALL_FLOWERS, SMALL_FLOWERS);
		save(ItemTags.LEAVES, LEAVES);
		save(ItemTags.SAPLINGS, SAPLINGS);
		save(PlantopiaItemTags.IGNORED_BY_BEES, IGNORED_BY_BEES);
		save(PlantopiaItemTags.PREFERRED_BY_BEES, PREFERRED_BY_BEES);
		save(PlantopiaItemTags.SEA_SHELL, SEA_SHELL);
	}

	private void save(TagKey<Item> key, @NotNull PlantopiaTagSet<Item> tagSet) {
		if(tagSet.isEmpty()) return;

		var targetTag = tag(key);

		var tags = tagSet.getTags();
		var items = tagSet.getElements();

		tags.sort(Comparator.comparing(PlantopiaResourceHelper::idOf));
		items.sort(Comparator.comparing(PlantopiaResourceHelper::idOf));

		for(var tag : tags) targetTag.addTag(tag);
		for(var item : items) targetTag.add(item);
	}
}