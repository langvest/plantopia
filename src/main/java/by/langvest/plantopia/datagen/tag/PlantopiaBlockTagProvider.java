package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.store.PlantopiaMetaStore;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaContentHelper;
import by.langvest.plantopia.util.PlantopiaTagSet;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

public class PlantopiaBlockTagProvider extends BlockTagsProvider {
	private final PlantopiaTagSet<Block> REPLACEABLE_PLANTS = PlantopiaTagSet.newTagSet();
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
	private static PlantopiaBlockTagProvider instance;

	public PlantopiaBlockTagProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Plantopia.MOD_ID, existingFileHelper);
		instance = this;
	}

	public static PlantopiaBlockTagProvider getInstance() {
		return instance;
	}

	@Override
	protected void addTags() {
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
		PlantopiaMetaStore.getBlocks().forEach(blockMeta -> {
			Block block = blockMeta.getBlock();
			MetaType type = blockMeta.getType();
			Material material = blockMeta.getMaterial();
			int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();

			if(blockMeta.isIgnoredByBees()) IGNORED_BY_BEES.add(block);
			if(blockMeta.isPreferredByBees()) PREFERRED_BY_BEES.add(block);
			if(type.isStoneLike()) MINEABLE_WITH_PICKAXE.add(block);
			if(type == MetaType.POTTED) FLOWER_POTS.add(block);

			if(type.isPlantLike()) {
				MINEABLE_WITH_AXE.add(block);
				if(material == Material.REPLACEABLE_PLANT) REPLACEABLE_PLANTS.add(block);
			}

			if(type.isFlowerLike()) {
				if(baseHeight > 1) {
					TALL_FLOWERS.add(block);
					REPLACEABLE_PLANTS.add(block);
				} else {
					SMALL_FLOWERS.add(block);
				}
			}

			if(type.isLeavesLike()) {
				LEAVES.add(block);
				MINEABLE_WITH_HOE.add(block);
			}

			if(type.isSaplingLike()) {
				SAPLINGS.add(block);
				MINEABLE_WITH_AXE.add(block);
			}

			if(type.isMushroomLike()) {
				MINEABLE_WITH_AXE.add(block);
				if(block instanceof BushBlock) ENDERMAN_HOLDABLE.add(block);
			}
		});
	}

	private void saveAll() {
		save(BlockTags.REPLACEABLE_PLANTS, REPLACEABLE_PLANTS);
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

		TagsProvider.TagAppender<Block> targetTag = tag(key);

		ArrayList<TagKey<Block>> tags = tagSet.getTags();
		ArrayList<Block> blocks = tagSet.getElements();

		tags.sort(Comparator.comparing(PlantopiaContentHelper::idOf));
		blocks.sort(Comparator.comparing(PlantopiaContentHelper::idOf));

		for(TagKey<Block> tag : tags) targetTag.addTag(tag);
		for(Block block : blocks) targetTag.add(block);
	}
}