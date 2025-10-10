package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaSeaShellBlock;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaItemTagProvider extends ItemTagsProvider implements PlantopiaTagProvider<Item> {
    private final Map<TagKey<Item>, PlantopiaTagSet<Item>> tagSets = Maps.newHashMap();

	private final PlantopiaTagSet<Item> TALL_FLOWERS = createTagSet(ItemTags.TALL_FLOWERS);
	private final PlantopiaTagSet<Item> SMALL_FLOWERS = createTagSet(ItemTags.SMALL_FLOWERS);
	private final PlantopiaTagSet<Item> LEAVES = createTagSet(ItemTags.LEAVES);
	private final PlantopiaTagSet<Item> SAPLINGS = createTagSet(ItemTags.SAPLINGS);
	private final PlantopiaTagSet<Item> IGNORED_BY_BEES = createTagSet(PlantopiaItemTags.IGNORED_BY_BEES);
	private final PlantopiaTagSet<Item> PREFERRED_BY_BEES = createTagSet(PlantopiaItemTags.PREFERRED_BY_BEES);
	private final PlantopiaTagSet<Item> BIRCH_LOGS = createTagSet(ItemTags.BIRCH_LOGS);
	private final PlantopiaTagSet<Item> DIRT = createTagSet(ItemTags.DIRT);
	private final PlantopiaTagSet<Item> SEA_SHELL = createTagSet(PlantopiaItemTags.SEA_SHELL);

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
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			if(!blockMeta.hasItem()) return;

			var block = blockMeta.get();
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
		tagSets.forEach(this::save);
	}

    private @NotNull PlantopiaTagSet<Item> createTagSet(TagKey<Item> key) {
        PlantopiaTagSet<Item> tagSet = PlantopiaTagSet.newTagSet();
        this.tagSets.put(key, tagSet);
        return tagSet;
    }

	@Override
	public @NotNull IntrinsicTagAppender<Item> getTagAppender(TagKey<Item> key) {
		return tag(key);
	}
}
