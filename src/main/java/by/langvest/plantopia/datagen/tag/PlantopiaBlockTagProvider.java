package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaSeaShellBlock;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaBlockTagProvider extends BlockTagsProvider implements PlantopiaTagProvider<Block> {
    private final Map<TagKey<Block>, PlantopiaTagSet<Block>> tagSets = Maps.newHashMap();

	private final PlantopiaTagSet<Block> REPLACEABLE = createTagSet(BlockTags.REPLACEABLE);
	private final PlantopiaTagSet<Block> TALL_FLOWERS = createTagSet(BlockTags.TALL_FLOWERS);
	private final PlantopiaTagSet<Block> SMALL_FLOWERS = createTagSet(BlockTags.SMALL_FLOWERS);
	private final PlantopiaTagSet<Block> LEAVES = createTagSet(BlockTags.LEAVES);
	private final PlantopiaTagSet<Block> SAPLINGS = createTagSet(BlockTags.SAPLINGS);
	private final PlantopiaTagSet<Block> MINEABLE_WITH_AXE = createTagSet(BlockTags.MINEABLE_WITH_AXE);
	private final PlantopiaTagSet<Block> MINEABLE_WITH_HOE = createTagSet(BlockTags.MINEABLE_WITH_HOE);
	private final PlantopiaTagSet<Block> MINEABLE_WITH_PICKAXE = createTagSet(BlockTags.MINEABLE_WITH_PICKAXE);
	private final PlantopiaTagSet<Block> MINEABLE_WITH_SHOVEL = createTagSet(BlockTags.MINEABLE_WITH_SHOVEL);
	private final PlantopiaTagSet<Block> ENDERMAN_HOLDABLE = createTagSet(BlockTags.ENDERMAN_HOLDABLE);
	private final PlantopiaTagSet<Block> FLOWER_POTS = createTagSet(BlockTags.FLOWER_POTS);
	private final PlantopiaTagSet<Block> IGNORED_BY_BEES = createTagSet(PlantopiaBlockTags.IGNORED_BY_BEES);
	private final PlantopiaTagSet<Block> PREFERRED_BY_BEES = createTagSet(PlantopiaBlockTags.PREFERRED_BY_BEES);
	private final PlantopiaTagSet<Block> REPLACEABLE_BY_TREES = createTagSet(BlockTags.REPLACEABLE_BY_TREES);
	private final PlantopiaTagSet<Block> SWORD_EFFICIENT = createTagSet(BlockTags.SWORD_EFFICIENT);
	private final PlantopiaTagSet<Block> BIRCH_LOGS = createTagSet(BlockTags.BIRCH_LOGS);
	private final PlantopiaTagSet<Block> OVERWORLD_NATURAL_LOGS = createTagSet(BlockTags.OVERWORLD_NATURAL_LOGS);
	private final PlantopiaTagSet<Block> DIRT = createTagSet(BlockTags.DIRT);
	private final PlantopiaTagSet<Block> SNIFFER_DIGGABLE_BLOCK = createTagSet(BlockTags.SNIFFER_DIGGABLE_BLOCK);
	private final PlantopiaTagSet<Block> CONVERTABLE_TO_MUD = createTagSet(BlockTags.CONVERTABLE_TO_MUD);
	private final PlantopiaTagSet<Block> WOLVES_SPAWNABLE_ON = createTagSet(BlockTags.WOLVES_SPAWNABLE_ON);
	private final PlantopiaTagSet<Block> ANIMALS_SPAWNABLE_ON = createTagSet(BlockTags.ANIMALS_SPAWNABLE_ON);
	private final PlantopiaTagSet<Block> FOXES_SPAWNABLE_ON = createTagSet(BlockTags.FOXES_SPAWNABLE_ON);
	private final PlantopiaTagSet<Block> FROGS_SPAWNABLE_ON = createTagSet(BlockTags.FROGS_SPAWNABLE_ON);
	private final PlantopiaTagSet<Block> PARROTS_SPAWNABLE_ON = createTagSet(BlockTags.PARROTS_SPAWNABLE_ON);
	private final PlantopiaTagSet<Block> RABBITS_SPAWNABLE_ON = createTagSet(BlockTags.RABBITS_SPAWNABLE_ON);
	private final PlantopiaTagSet<Block> VALID_SPAWN = createTagSet(BlockTags.VALID_SPAWN);
	private final PlantopiaTagSet<Block> BONEMEAL_SPREAD_GROWABLE = createTagSet(PlantopiaBlockTags.BONEMEAL_SPREAD_GROWABLE);
	private final PlantopiaTagSet<Block> BONEMEAL_SPREAD_ON = createTagSet(PlantopiaBlockTags.BONEMEAL_SPREAD_ON);
	private final PlantopiaTagSet<Block> INFESTED_DIRT_CAN_SPREAD_TO = createTagSet(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO);
	private final PlantopiaTagSet<Block> FROG_PREFER_JUMP_TO = createTagSet(BlockTags.FROG_PREFER_JUMP_TO);
	private final PlantopiaTagSet<Block> INSIDE_STEP_SOUND_BLOCKS = createTagSet(BlockTags.INSIDE_STEP_SOUND_BLOCKS);
	private final PlantopiaTagSet<Block> SEA_SHELL = createTagSet(PlantopiaBlockTags.SEA_SHELL);
	private final PlantopiaTagSet<Block> SNOW = createTagSet(BlockTags.SNOW);
	private final PlantopiaTagSet<Block> COMBINATION_STEP_SOUND_BLOCKS = createTagSet(BlockTags.COMBINATION_STEP_SOUND_BLOCKS);

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
		add(BIRCH_LOGS, PlantopiaBlocks.BIRCH_BASE_LOG.get(), PlantopiaBlocks.BIRCH_BASE_WOOD.get());
		add(OVERWORLD_NATURAL_LOGS, PlantopiaBlocks.BIRCH_BASE_LOG.get());
		add(CONVERTABLE_TO_MUD, PlantopiaBlocks.INFESTED_DIRT.get());
		add(BONEMEAL_SPREAD_GROWABLE, Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN, PlantopiaBlocks.CLOVER.get());
		add(BONEMEAL_SPREAD_ON, Blocks.GRASS_BLOCK, PlantopiaBlocks.INFESTED_GRASS_BLOCK.get());
		add(INFESTED_DIRT_CAN_SPREAD_TO, Blocks.DIRT, Blocks.FARMLAND, Blocks.DIRT_PATH);

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
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			var block = blockMeta.get();
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

			if(type.instanceOf(MetaType.PLANT) && !type.instanceOf(MetaType.UNDERWATER_PLANT)) {
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

			if(type.instanceOf(MetaType.DIRT)) {
				SNIFFER_DIGGABLE_BLOCK.add(block);
				DIRT.add(block);
				MINEABLE_WITH_SHOVEL.add(block);
			}

			if(type.instanceOf(MetaType.GRASS_BLOCK)) {
				VALID_SPAWN.add(block);
				RABBITS_SPAWNABLE_ON.add(block);
				PARROTS_SPAWNABLE_ON.add(block);
				FROGS_SPAWNABLE_ON.add(block);
				FOXES_SPAWNABLE_ON.add(block);
				ANIMALS_SPAWNABLE_ON.add(block);
				WOLVES_SPAWNABLE_ON.add(block);
			}

			if(type.instanceOf(MetaType.WATERLILY)) {
				MINEABLE_WITH_AXE.add(block);
				FROG_PREFER_JUMP_TO.add(block);
				INSIDE_STEP_SOUND_BLOCKS.add(block);
			}

			if(block instanceof PlantopiaSeaShellBlock) {
				SEA_SHELL.add(block);
			}

			if(type.instanceOf(MetaType.SNOW)) {
				SNOW.add(block);
				MINEABLE_WITH_SHOVEL.add(block);
				COMBINATION_STEP_SOUND_BLOCKS.add(block);
			}
		});
	}

	private void saveAll() {
		tagSets.forEach(this::save);
	}

    private @NotNull PlantopiaTagSet<Block> createTagSet(TagKey<Block> key) {
        PlantopiaTagSet<Block> tagSet = PlantopiaTagSet.newTagSet();
        this.tagSets.put(key, tagSet);
        return tagSet;
    }

	@Override
	public @NotNull IntrinsicTagAppender<Block> getTagAppender(TagKey<Block> key) {
		return tag(key);
	}
}
