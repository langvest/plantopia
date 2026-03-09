package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaBlockTagProvider extends BlockTagsProvider implements PlantopiaTagProvider<Block> {
    private final Map<TagKey<Block>, PlantopiaTagSet<Block>> byTagKeys = Maps.newHashMap();
    private final Map<MetaType, PlantopiaTagSet<Block>> byMetaTypes = Maps.newHashMap();

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
    private final PlantopiaTagSet<Block> POLAR_BEARS_SPAWNABLE_ON_ALTERNATE = createTagSet(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
    private final PlantopiaTagSet<Block> GEODE_INVALID_BLOCKS = createTagSet(BlockTags.GEODE_INVALID_BLOCKS);
    private final PlantopiaTagSet<Block> VALID_SPAWN = createTagSet(BlockTags.VALID_SPAWN);
    private final PlantopiaTagSet<Block> BONEMEAL_SPREAD_GROWABLE = createTagSet(PlantopiaBlockTags.BONEMEAL_SPREAD_GROWABLE);
    private final PlantopiaTagSet<Block> BONEMEAL_SPREAD_ON = createTagSet(PlantopiaBlockTags.BONEMEAL_SPREAD_ON);
    private final PlantopiaTagSet<Block> INFESTED_DIRT_CAN_SPREAD_TO = createTagSet(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO);
    private final PlantopiaTagSet<Block> FROG_PREFER_JUMP_TO = createTagSet(BlockTags.FROG_PREFER_JUMP_TO);
    private final PlantopiaTagSet<Block> INSIDE_STEP_SOUND_BLOCKS = createTagSet(BlockTags.INSIDE_STEP_SOUND_BLOCKS);
    private final PlantopiaTagSet<Block> SNOW = createTagSet(BlockTags.SNOW);
    private final PlantopiaTagSet<Block> ICE = createTagSet(BlockTags.ICE);
    private final PlantopiaTagSet<Block> COMBINATION_STEP_SOUND_BLOCKS = createTagSet(BlockTags.COMBINATION_STEP_SOUND_BLOCKS);
    private final PlantopiaTagSet<Block> BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS = createTagSet(PlantopiaBlockTags.BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS);
    private final PlantopiaTagSet<Block> BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS = createTagSet(PlantopiaBlockTags.BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS);
    private final PlantopiaTagSet<Block> COBBLESTONE_SHARD_CAN_GENERATE_ON = createTagSet(PlantopiaBlockTags.COBBLESTONE_SHARD_CAN_GENERATE_ON);
    private final PlantopiaTagSet<Block> SEA_MOSS_REPLACEABLE = createTagSet(PlantopiaBlockTags.SEA_MOSS_REPLACEABLE);

    private static PlantopiaBlockTagProvider instance;

    public PlantopiaBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
        instance = this;
    }

    public static PlantopiaBlockTagProvider getInstance() {
        return instance;
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        generateAll();

        DIRT.add(PlantopiaBlocks.SEA_MOSS_BLOCK.get());
        MINEABLE_WITH_HOE.add(PlantopiaBlocks.SEA_MOSS_BLOCK.get(), PlantopiaBlocks.SEA_MOSS_CARPET.get());
        SWORD_EFFICIENT.add(PlantopiaBlocks.SEA_MOSS_CARPET.get());
        COMBINATION_STEP_SOUND_BLOCKS.add(PlantopiaBlocks.SEA_MOSS_CARPET.get());
        IGNORED_BY_BEES.add(Blocks.WITHER_ROSE);
        BIRCH_LOGS.add(PlantopiaBlocks.BIRCH_BASE_LOG.get(), PlantopiaBlocks.BIRCH_BASE_WOOD.get());
        OVERWORLD_NATURAL_LOGS.add(PlantopiaBlocks.BIRCH_BASE_LOG.get());
        CONVERTABLE_TO_MUD.add(PlantopiaBlocks.INFESTED_DIRT.get());
        BONEMEAL_SPREAD_GROWABLE.add(Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN, PlantopiaBlocks.CLOVER.get());
        BONEMEAL_SPREAD_ON.add(Blocks.GRASS_BLOCK, PlantopiaBlocks.INFESTED_GRASS_BLOCK.get());
        INFESTED_DIRT_CAN_SPREAD_TO.add(Blocks.DIRT, Blocks.FARMLAND, Blocks.DIRT_PATH);
        BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS.addOptionalTag(forge("glass"), forge("glass_panes"));
        BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS.add(Blocks.ICE);
        COBBLESTONE_SHARD_CAN_GENERATE_ON.add(Blocks.GRAVEL, Blocks.CLAY).addTag(BlockTags.DIRT, BlockTags.SAND, BlockTags.BASE_STONE_OVERWORLD).apply(this::addOverworldOres);
        SEA_MOSS_REPLACEABLE.addTag(BlockTags.BASE_STONE_OVERWORLD, BlockTags.DIRT, BlockTags.SAND);

        saveAll();
    }

    private void generateAll() {
        var registryHelper = Plantopia.getPlatform().getRegistryHelper();

        registryHelper.getKnownRegistryOrThrow(Registries.BLOCK).forEach(block -> {
            String name = nameOf(block);

            if(name.matches(".*(^|_)glass(_|$).*")) {
                BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS.add(block);
            }
        });

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

            if(type.instanceOf(MetaType.ICE)) {
                MINEABLE_WITH_PICKAXE.add(block);
                ICE.add(block);
                POLAR_BEARS_SPAWNABLE_ON_ALTERNATE.add(block);
                GEODE_INVALID_BLOCKS.add(block);
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

                if(type.instanceOf(MetaType.MUSHROOM_PLANT)) {
                    ENDERMAN_HOLDABLE.add(block);
                }
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

            if(type.instanceOf(MetaType.SNOW)) {
                SNOW.add(block);
                MINEABLE_WITH_SHOVEL.add(block);
                COMBINATION_STEP_SOUND_BLOCKS.add(block);
            }

            if(blockMeta.shouldGenerateTag()) {
                byMetaTypes.computeIfAbsent(type, key -> PlantopiaTagSet.newTagSet()).add(block);
            }
        });
    }

    private void saveAll() {
        saveByTagKeys(byTagKeys);
        saveByMetaTypes(byMetaTypes);
    }

    private void addOverworldOres(@NotNull PlantopiaTagSet<Block> tagSet) {
        tagSet.addTag(
            BlockTags.COAL_ORES,
            BlockTags.EMERALD_ORES,
            BlockTags.DIAMOND_ORES,
            BlockTags.COPPER_ORES,
            BlockTags.GOLD_ORES,
            BlockTags.IRON_ORES,
            BlockTags.LAPIS_ORES,
            BlockTags.REDSTONE_ORES
        );
    }

    private @NotNull PlantopiaTagSet<Block> createTagSet(TagKey<Block> key) {
        PlantopiaTagSet<Block> tagSet = PlantopiaTagSet.newTagSet();
        byTagKeys.put(key, tagSet);
        return tagSet;
    }

    @Override
    public @NotNull IntrinsicTagAppender<Block> getTagAppender(TagKey<Block> key) {
        return tag(key);
    }

    @Override
    public ResourceKey<? extends Registry<Block>> getRegistryKey() {
        return Registries.BLOCK;
    }
}
