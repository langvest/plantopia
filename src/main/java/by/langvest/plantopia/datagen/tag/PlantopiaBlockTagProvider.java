package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.util.helper.PlantopiaResourceHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaBlockTagProvider extends BlockTagsProvider {
	private final PlantopiaTagSet<Block> REPLACEABLE = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> TALL_FLOWERS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> SMALL_FLOWERS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> LEAVES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> SAPLINGS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> MINEABLE_WITH_AXE = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> MINEABLE_WITH_HOE = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> MINEABLE_WITH_PICKAXE = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> ENDERMAN_HOLDABLE = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> FLOWER_POTS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> IGNORED_BY_BEES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> PREFERRED_BY_BEES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> REPLACEABLE_BY_TREES = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<Block> SWORD_EFFICIENT = PlantopiaTagSet.newTagSet();
	private static PlantopiaBlockTagProvider instance;

	public PlantopiaBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
		instance = this;
	}

	public static PlantopiaBlockTagProvider getInstance() {
		return instance;
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		generateAll();

		add(IGNORED_BY_BEES, Blocks.WITHER_ROSE);

		saveAll();
	}

	private void add(@NotNull PlantopiaTagSet<Block> tagSet, Block... blocks) {
		tagSet.add(blocks);
	}

	@SafeVarargs
	@SuppressWarnings("unused")
	private void add(@NotNull PlantopiaTagSet<Block> tagSet, TagKey<Block>... tags) {
		tagSet.addTags(tags);
	}

	private void generateAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
			var block = blockMeta.getBlock();
			var type = blockMeta.getType();
			boolean replaceable = block.properties.replaceable;
			int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();

			if(blockMeta.isIgnoredByBees()) IGNORED_BY_BEES.add(block);
			if(blockMeta.isPreferredByBees()) PREFERRED_BY_BEES.add(block);
			if(type.instanceOf(MetaType.STONE)) MINEABLE_WITH_PICKAXE.add(block);
			if(type.instanceOf(MetaType.POTTED)) FLOWER_POTS.add(block);

			if(type.isSimplePlantLike()) {
				MINEABLE_WITH_AXE.add(block);

				if(replaceable) {
					REPLACEABLE.add(block);
					REPLACEABLE_BY_TREES.add(block);
				}
			}

			if(type.instanceOf(MetaType.FLOWER)) {
				if(baseHeight > 1) {
					TALL_FLOWERS.add(block);
					REPLACEABLE_BY_TREES.add(block);
				} else {
					SMALL_FLOWERS.add(block);
				}
			}

			if(type.instanceOf(MetaType.PLANT)) {
				SWORD_EFFICIENT.add(block);
			}

			if(type.instanceOf(MetaType.LEAVES)) {
				LEAVES.add(block);
				MINEABLE_WITH_HOE.add(block);
			}

			if(type.instanceOf(MetaType.SAPLING)) {
				SAPLINGS.add(block);
				MINEABLE_WITH_AXE.add(block);
			}

			if(type.isMushroomLike()) {
				MINEABLE_WITH_AXE.add(block);
				if(type.instanceOf(MetaType.MUSHROOM_PLANT)) ENDERMAN_HOLDABLE.add(block);
			}
		});
	}

	private void saveAll() {
		save(BlockTags.REPLACEABLE, REPLACEABLE);
		save(BlockTags.SWORD_EFFICIENT, SWORD_EFFICIENT);
		save(BlockTags.REPLACEABLE_BY_TREES, REPLACEABLE_BY_TREES);
		save(BlockTags.TALL_FLOWERS, TALL_FLOWERS);
		save(BlockTags.SMALL_FLOWERS, SMALL_FLOWERS);
		save(BlockTags.LEAVES, LEAVES);
		save(BlockTags.SAPLINGS, SAPLINGS);
		save(BlockTags.MINEABLE_WITH_AXE, MINEABLE_WITH_AXE);
		save(BlockTags.MINEABLE_WITH_HOE, MINEABLE_WITH_HOE);
		save(BlockTags.MINEABLE_WITH_PICKAXE, MINEABLE_WITH_PICKAXE);
		save(BlockTags.ENDERMAN_HOLDABLE, ENDERMAN_HOLDABLE);
		save(BlockTags.FLOWER_POTS, FLOWER_POTS);
		save(PlantopiaBlockTags.IGNORED_BY_BEES, IGNORED_BY_BEES);
		save(PlantopiaBlockTags.PREFERRED_BY_BEES, PREFERRED_BY_BEES);
	}

	private void save(TagKey<Block> key, @NotNull PlantopiaTagSet<Block> tagSet) {
		if(tagSet.isEmpty()) return;

		var targetTag = tag(key);

		var tags = tagSet.getTags();
		var blocks = tagSet.getElements();

		tags.sort(Comparator.comparing(PlantopiaResourceHelper::idOf));
		blocks.sort(Comparator.comparing(PlantopiaResourceHelper::idOf));

		for(var tag : tags) targetTag.addTag(tag);
		for(var block : blocks) targetTag.add(block);
	}
}