package by.langvest.plantopia.neoforge.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.toolkit.platform.EventEmitter;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaBlockTagProvider extends BlockTagsProvider implements PlantopiaTagProvider<Block> {
    private static final Map<TagKey<Block>, PlantopiaTagSet<Block>> byTagKeys = Maps.newHashMap();
    private static final Map<MetaType, PlantopiaTagSet<Block>> byMetaTypes = Maps.newHashMap();

    public static final PlantopiaTagSet<Block> REPLACEABLE = getOrCreateTagSet(BlockTags.REPLACEABLE);
    public static final PlantopiaTagSet<Block> TALL_FLOWERS = getOrCreateTagSet(BlockTags.TALL_FLOWERS);
    public static final PlantopiaTagSet<Block> SMALL_FLOWERS = getOrCreateTagSet(BlockTags.SMALL_FLOWERS);
    public static final PlantopiaTagSet<Block> LEAVES = getOrCreateTagSet(BlockTags.LEAVES);
    public static final PlantopiaTagSet<Block> SAPLINGS = getOrCreateTagSet(BlockTags.SAPLINGS);
    public static final PlantopiaTagSet<Block> MINEABLE_WITH_AXE = getOrCreateTagSet(BlockTags.MINEABLE_WITH_AXE);
    public static final PlantopiaTagSet<Block> MINEABLE_WITH_HOE = getOrCreateTagSet(BlockTags.MINEABLE_WITH_HOE);
    public static final PlantopiaTagSet<Block> MINEABLE_WITH_PICKAXE = getOrCreateTagSet(BlockTags.MINEABLE_WITH_PICKAXE);
    public static final PlantopiaTagSet<Block> MINEABLE_WITH_SHOVEL = getOrCreateTagSet(BlockTags.MINEABLE_WITH_SHOVEL);
    public static final PlantopiaTagSet<Block> ENDERMAN_HOLDABLE = getOrCreateTagSet(BlockTags.ENDERMAN_HOLDABLE);
    public static final PlantopiaTagSet<Block> FLOWER_POTS = getOrCreateTagSet(BlockTags.FLOWER_POTS);
    public static final PlantopiaTagSet<Block> IGNORED_BY_BEES = getOrCreateTagSet(PlantopiaBlockTags.IGNORED_BY_BEES);
    public static final PlantopiaTagSet<Block> PREFERRED_BY_BEES = getOrCreateTagSet(PlantopiaBlockTags.PREFERRED_BY_BEES);
    public static final PlantopiaTagSet<Block> REPLACEABLE_BY_TREES = getOrCreateTagSet(BlockTags.REPLACEABLE_BY_TREES);
    public static final PlantopiaTagSet<Block> SWORD_EFFICIENT = getOrCreateTagSet(BlockTags.SWORD_EFFICIENT);
    public static final PlantopiaTagSet<Block> BIRCH_LOGS = getOrCreateTagSet(BlockTags.BIRCH_LOGS);
    public static final PlantopiaTagSet<Block> COMPLETES_FIND_TREE_TUTORIAL = getOrCreateTagSet(BlockTags.COMPLETES_FIND_TREE_TUTORIAL);
    public static final PlantopiaTagSet<Block> LAVA_POOL_STONE_CANNOT_REPLACE = getOrCreateTagSet(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE);
    public static final PlantopiaTagSet<Block> OVERWORLD_NATURAL_LOGS = getOrCreateTagSet(BlockTags.OVERWORLD_NATURAL_LOGS);
    public static final PlantopiaTagSet<Block> DIRT = getOrCreateTagSet(BlockTags.DIRT);
    public static final PlantopiaTagSet<Block> SNIFFER_DIGGABLE_BLOCK = getOrCreateTagSet(BlockTags.SNIFFER_DIGGABLE_BLOCK);
    public static final PlantopiaTagSet<Block> ENCHANTMENT_POWER_TRANSMITTER = getOrCreateTagSet(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    public static final PlantopiaTagSet<Block> CONVERTABLE_TO_MUD = getOrCreateTagSet(BlockTags.CONVERTABLE_TO_MUD);
    public static final PlantopiaTagSet<Block> WOLVES_SPAWNABLE_ON = getOrCreateTagSet(BlockTags.WOLVES_SPAWNABLE_ON);
    public static final PlantopiaTagSet<Block> ANIMALS_SPAWNABLE_ON = getOrCreateTagSet(BlockTags.ANIMALS_SPAWNABLE_ON);
    public static final PlantopiaTagSet<Block> FOXES_SPAWNABLE_ON = getOrCreateTagSet(BlockTags.FOXES_SPAWNABLE_ON);
    public static final PlantopiaTagSet<Block> FROGS_SPAWNABLE_ON = getOrCreateTagSet(BlockTags.FROGS_SPAWNABLE_ON);
    public static final PlantopiaTagSet<Block> PARROTS_SPAWNABLE_ON = getOrCreateTagSet(BlockTags.PARROTS_SPAWNABLE_ON);
    public static final PlantopiaTagSet<Block> RABBITS_SPAWNABLE_ON = getOrCreateTagSet(BlockTags.RABBITS_SPAWNABLE_ON);
    public static final PlantopiaTagSet<Block> POLAR_BEARS_SPAWNABLE_ON_ALTERNATE = getOrCreateTagSet(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE);
    public static final PlantopiaTagSet<Block> GEODE_INVALID_BLOCKS = getOrCreateTagSet(BlockTags.GEODE_INVALID_BLOCKS);
    public static final PlantopiaTagSet<Block> VALID_SPAWN = getOrCreateTagSet(BlockTags.VALID_SPAWN);
    public static final PlantopiaTagSet<Block> BONEMEAL_SPREAD_GROWABLE = getOrCreateTagSet(PlantopiaBlockTags.BONEMEAL_SPREAD_GROWABLE);
    public static final PlantopiaTagSet<Block> BONEMEAL_SPREAD_ON = getOrCreateTagSet(PlantopiaBlockTags.BONEMEAL_SPREAD_ON);
    public static final PlantopiaTagSet<Block> INFESTED_DIRT_CAN_SPREAD_TO = getOrCreateTagSet(PlantopiaBlockTags.INFESTED_DIRT_CAN_SPREAD_TO);
    public static final PlantopiaTagSet<Block> FROG_PREFER_JUMP_TO = getOrCreateTagSet(BlockTags.FROG_PREFER_JUMP_TO);
    public static final PlantopiaTagSet<Block> INSIDE_STEP_SOUND_BLOCKS = getOrCreateTagSet(BlockTags.INSIDE_STEP_SOUND_BLOCKS);
    public static final PlantopiaTagSet<Block> SNOW = getOrCreateTagSet(BlockTags.SNOW);
    public static final PlantopiaTagSet<Block> ICE = getOrCreateTagSet(BlockTags.ICE);
    public static final PlantopiaTagSet<Block> PLANKS = getOrCreateTagSet(BlockTags.PLANKS);
    public static final PlantopiaTagSet<Block> STAIRS = getOrCreateTagSet(BlockTags.STAIRS);
    public static final PlantopiaTagSet<Block> WOODEN_STAIRS = getOrCreateTagSet(BlockTags.WOODEN_STAIRS);
    public static final PlantopiaTagSet<Block> SLABS = getOrCreateTagSet(BlockTags.SLABS);
    public static final PlantopiaTagSet<Block> WOODEN_SLABS = getOrCreateTagSet(BlockTags.WOODEN_SLABS);
    public static final PlantopiaTagSet<Block> FENCES = getOrCreateTagSet(BlockTags.FENCES);
    public static final PlantopiaTagSet<Block> WOODEN_FENCES = getOrCreateTagSet(BlockTags.WOODEN_FENCES);
    public static final PlantopiaTagSet<Block> FENCE_GATES = getOrCreateTagSet(BlockTags.FENCE_GATES);
    public static final PlantopiaTagSet<Block> DOORS = getOrCreateTagSet(BlockTags.DOORS);
    public static final PlantopiaTagSet<Block> WOODEN_DOORS = getOrCreateTagSet(BlockTags.WOODEN_DOORS);
    public static final PlantopiaTagSet<Block> TRAPDOORS = getOrCreateTagSet(BlockTags.TRAPDOORS);
    public static final PlantopiaTagSet<Block> WOODEN_TRAPDOORS = getOrCreateTagSet(BlockTags.WOODEN_TRAPDOORS);
    public static final PlantopiaTagSet<Block> PRESSURE_PLATES = getOrCreateTagSet(BlockTags.PRESSURE_PLATES);
    public static final PlantopiaTagSet<Block> WOODEN_PRESSURE_PLATES = getOrCreateTagSet(BlockTags.WOODEN_PRESSURE_PLATES);
    public static final PlantopiaTagSet<Block> BUTTONS = getOrCreateTagSet(BlockTags.BUTTONS);
    public static final PlantopiaTagSet<Block> WOODEN_BUTTONS = getOrCreateTagSet(BlockTags.WOODEN_BUTTONS);
    public static final PlantopiaTagSet<Block> STANDING_SIGNS = getOrCreateTagSet(BlockTags.STANDING_SIGNS);
    public static final PlantopiaTagSet<Block> WALL_SIGNS = getOrCreateTagSet(BlockTags.WALL_SIGNS);
    public static final PlantopiaTagSet<Block> CEILING_HANGING_SIGNS = getOrCreateTagSet(BlockTags.CEILING_HANGING_SIGNS);
    public static final PlantopiaTagSet<Block> WALL_HANGING_SIGNS = getOrCreateTagSet(BlockTags.WALL_HANGING_SIGNS);
    public static final PlantopiaTagSet<Block> COMBINATION_STEP_SOUND_BLOCKS = getOrCreateTagSet(BlockTags.COMBINATION_STEP_SOUND_BLOCKS);
    public static final PlantopiaTagSet<Block> BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS = getOrCreateTagSet(PlantopiaBlockTags.BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS);
    public static final PlantopiaTagSet<Block> BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS = getOrCreateTagSet(PlantopiaBlockTags.BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS);
    public static final PlantopiaTagSet<Block> COBBLESTONE_SHARD_CAN_GENERATE_ON = getOrCreateTagSet(PlantopiaBlockTags.COBBLESTONE_SHARD_CAN_GENERATE_ON);
    public static final PlantopiaTagSet<Block> BRANCHING_SHRUB_CAN_GENERATE_ON = getOrCreateTagSet(PlantopiaBlockTags.BRANCHING_SHRUB_CAN_GENERATE_ON);
    public static final PlantopiaTagSet<Block> TOADSTOOL_CAN_GENERATE_ON = getOrCreateTagSet(PlantopiaBlockTags.TOADSTOOL_CAN_GENERATE_ON);
    public static final PlantopiaTagSet<Block> SEA_MOSS_REPLACEABLE = getOrCreateTagSet(PlantopiaBlockTags.SEA_MOSS_REPLACEABLE);
    public static final PlantopiaTagSet<Block> ORES_OVERWORLD = getOrCreateTagSet(PlantopiaBlockTags.ORES_OVERWORLD);
    public static final PlantopiaTagSet<Block> GROUND_OVERWORLD = getOrCreateTagSet(PlantopiaBlockTags.GROUND_OVERWORLD);
    public static final PlantopiaTagSet<Block> PACKED_ICE_REPLACEABLE_BLOCKS = getOrCreateTagSet(PlantopiaBlockTags.PACKED_ICE_REPLACEABLE_BLOCKS);
    public static final PlantopiaTagSet<Block> SEA_MOSS_REPLACEABLE_BLOCKS = getOrCreateTagSet(PlantopiaBlockTags.SEA_MOSS_REPLACEABLE_BLOCKS);
    public static final PlantopiaTagSet<Block> LEAVES_CAN_SURVIVE_ON = getOrCreateTagSet(PlantopiaBlockTags.LEAVES_CAN_SURVIVE_ON);

    private static PlantopiaBlockTagProvider instance;

    public PlantopiaBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup, ExistingFileHelper existingFileHelper, @NotNull EventEmitter eventEmitter) {
        super(output, registryLookup, Plantopia.MOD_ID, existingFileHelper);
        PlantopiaBlockTagProvider.instance = this;
        eventEmitter.subscribe(this::listenBridge);
    }

    public static PlantopiaBlockTagProvider getInstance() {
        return instance;
    }

    protected void listenBridge(PlantopiaDatagenBridgeEvent.@NotNull BlockTagEvent event) {
        event.provide(PlantopiaBlockTagProvider::getOrCreateTagSet);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registryLookup) {
        generateAll();

        DIRT.add(PlantopiaBlocks.SEA_MOSS_BLOCK.get());
        MINEABLE_WITH_HOE.add(PlantopiaBlocks.SEA_MOSS_BLOCK.get(), PlantopiaBlocks.SEA_MOSS_CARPET.get());
        MINEABLE_WITH_AXE.addTag(PlantopiaBlockTags.BALKS);
        SWORD_EFFICIENT.add(PlantopiaBlocks.SEA_MOSS_CARPET.get());
        COMBINATION_STEP_SOUND_BLOCKS.add(PlantopiaBlocks.SEA_MOSS_CARPET.get());
        IGNORED_BY_BEES.add(Blocks.WITHER_ROSE);
        BIRCH_LOGS.add(PlantopiaBlocks.BIRCH_BASE_LOG.get(), PlantopiaBlocks.BIRCH_BASE_WOOD.get());
        OVERWORLD_NATURAL_LOGS.add(PlantopiaBlocks.BIRCH_BASE_LOG.get());
        CONVERTABLE_TO_MUD.add(PlantopiaBlocks.INFESTED_DIRT.get());
        BONEMEAL_SPREAD_ON.add(Blocks.GRASS_BLOCK, PlantopiaBlocks.INFESTED_GRASS_BLOCK.get());
        INFESTED_DIRT_CAN_SPREAD_TO.add(Blocks.DIRT, Blocks.FARMLAND, Blocks.DIRT_PATH);
        COBBLESTONE_SHARD_CAN_GENERATE_ON.addTag(PlantopiaBlockTags.GROUND_OVERWORLD);
        SEA_MOSS_REPLACEABLE.addTag(BlockTags.BASE_STONE_OVERWORLD, BlockTags.DIRT, BlockTags.SAND);
        PACKED_ICE_REPLACEABLE_BLOCKS.addTag(BlockTags.BASE_STONE_OVERWORLD);
        INSIDE_STEP_SOUND_BLOCKS.add(PlantopiaBlocks.ICE_CRUST.get());
        COMPLETES_FIND_TREE_TUTORIAL.addTag(PlantopiaBlockTags.BALKS);
        LAVA_POOL_STONE_CANNOT_REPLACE.addTag(PlantopiaBlockTags.BALKS);
        LEAVES_CAN_SURVIVE_ON.addTag(BlockTags.LOGS, PlantopiaBlockTags.BALKS);

        BREAKS_INTO_WATER_BY_COBBLESTONE_SHARDS
            .add(Blocks.ICE)
            .add(PlantopiaBlocks.FROZEN_REED.get());

        BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS
            .add(PlantopiaBlocks.ICICLE.get(), PlantopiaBlocks.ICE_CRUST.get())
            .addOptionalTag(forge("glass"), forge("glass_panes"));

        BONEMEAL_SPREAD_GROWABLE
            .add(Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN)
            .add(PlantopiaBlocks.CLOVER.get());

        GROUND_OVERWORLD
            .add(Blocks.GRAVEL, Blocks.CLAY, Blocks.SANDSTONE, Blocks.DRIPSTONE_BLOCK)
            .addTag(BlockTags.DIRT, BlockTags.SAND, BlockTags.BASE_STONE_OVERWORLD, BlockTags.TERRACOTTA)
            .addTag(PlantopiaBlockTags.ORES_OVERWORLD);

        ORES_OVERWORLD
            .add(Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.COPPER_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE)
            .add(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE);

        BRANCHING_SHRUB_CAN_GENERATE_ON
            .add(Blocks.RED_SAND)
            .addTag(BlockTags.DIRT, BlockTags.TERRACOTTA);

        TOADSTOOL_CAN_GENERATE_ON
            .add(Blocks.CLAY, Blocks.GRAVEL, Blocks.DRIPSTONE_BLOCK)
            .addTag(BlockTags.DIRT, BlockTags.BASE_STONE_OVERWORLD)
            .addTag(BlockTags.PLANKS, BlockTags.LOGS)
            .addTag(PlantopiaBlockTags.ORES_OVERWORLD);

        REPLACEABLE_BY_TREES
            .add(PlantopiaBlocks.BRANCHING_SHRUB.get())
            .add(PlantopiaBlocks.BIRCH_CATKIN.get(), PlantopiaBlocks.PINE_CONE.get());

        SEA_MOSS_REPLACEABLE_BLOCKS
            .addTag(BlockTags.BASE_STONE_OVERWORLD, BlockTags.DIRT)
            .add(Blocks.SANDSTONE, Blocks.GRAVEL);

        saveAll();
    }

    private void generateAll() {
        var workScheduler = Plantopia.getPlatform().getWorkScheduler();
        var registryHelper = Plantopia.getPlatform().getRegistryHelper();

        workScheduler.executeWork("block_tag_datagen");

        registryHelper.getKnownRegistryOrThrow(Registries.BLOCK).forEach(block -> {
            String name = nameOf(block);

            if (name.matches(".*(^|_)glass(_|$).*")) {
                BREAKS_INTO_AIR_BY_COBBLESTONE_SHARDS.add(block);
            }
        });

        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            var block = blockMeta.get();
            var type = blockMeta.getType();
            boolean replaceable = block.properties.replaceable;
            int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();

            if (blockMeta.isIgnoredByBees()) IGNORED_BY_BEES.add(block);
            if (blockMeta.isPreferredByBees()) PREFERRED_BY_BEES.add(block);
            if (type.instanceOf(MetaType.STONE)) MINEABLE_WITH_PICKAXE.add(block);
            if (type.instanceOf(MetaType.POTTED)) FLOWER_POTS.add(block);

            if (type.instanceOf(MetaType.SEA_SHELL)) {
                INSIDE_STEP_SOUND_BLOCKS.add(block);
            }

            if (type.isSimplePlantLike()) {
                MINEABLE_WITH_AXE.add(block);

                if (replaceable) {
                    REPLACEABLE.add(block);
                    REPLACEABLE_BY_TREES.add(block);
                }
            }

            if (type.instanceOf(MetaType.PLANKS)) {
                PLANKS.add(block);
            }

            if (type.instanceOf(MetaType.STAIRS)) {
                if (type.instanceOf(MetaType.WOODEN_STAIRS)) {
                    WOODEN_STAIRS.add(block);
                } else {
                    STAIRS.add(block);
                }
            }

            if (type.instanceOf(MetaType.SLAB)) {
                if (type.instanceOf(MetaType.WOODEN_SLAB)) {
                    WOODEN_SLABS.add(block);
                } else {
                    SLABS.add(block);
                }
            }

            if (type.instanceOf(MetaType.FENCE)) {
                if (type.instanceOf(MetaType.WOODEN_FENCE)) {
                    WOODEN_FENCES.add(block);
                } else {
                    FENCES.add(block);
                }
            }

            if (type.instanceOf(MetaType.FENCE_GATE)) {
                FENCE_GATES.add(block);
            }

            if (type.instanceOf(MetaType.DOOR)) {
                if (type.instanceOf(MetaType.WOODEN_DOOR)) {
                    WOODEN_DOORS.add(block);
                } else {
                    DOORS.add(block);
                }
            }

            if (type.instanceOf(MetaType.TRAPDOOR)) {
                if (type.instanceOf(MetaType.WOODEN_TRAPDOOR)) {
                    WOODEN_TRAPDOORS.add(block);
                } else {
                    TRAPDOORS.add(block);
                }
            }

            if (type.instanceOf(MetaType.PRESSURE_PLATE)) {
                if (type.instanceOf(MetaType.WOODEN_PRESSURE_PLATE)) {
                    WOODEN_PRESSURE_PLATES.add(block);
                } else {
                    PRESSURE_PLATES.add(block);
                }
            }

            if (type.instanceOf(MetaType.BUTTON)) {
                if (type.instanceOf(MetaType.WOODEN_BUTTON)) {
                    WOODEN_BUTTONS.add(block);
                } else {
                    BUTTONS.add(block);
                }
            }

            if (type.instanceOf(MetaType.SIGN)) {
                if (block instanceof StandingSignBlock) {
                    STANDING_SIGNS.add(block);
                }

                if (block instanceof WallSignBlock) {
                    WALL_SIGNS.add(block);
                }
            }

            if (type.instanceOf(MetaType.HANGING_SIGN)) {
                if (block instanceof CeilingHangingSignBlock) {
                    CEILING_HANGING_SIGNS.add(block);
                }

                if (block instanceof WallHangingSignBlock) {
                    WALL_HANGING_SIGNS.add(block);
                }
            }

            if (type.instanceOf(MetaType.ICE)) {
                MINEABLE_WITH_PICKAXE.add(block);

                if (type.equals(MetaType.ICE)) {
                    ICE.add(block);
                    POLAR_BEARS_SPAWNABLE_ON_ALTERNATE.add(block);
                    GEODE_INVALID_BLOCKS.add(block);
                }
            }

            if (type.instanceOf(MetaType.FLOWER)) {
                REPLACEABLE_BY_TREES.add(block);

                if (baseHeight > 1) {
                    TALL_FLOWERS.add(block);
                } else {
                    SMALL_FLOWERS.add(block);
                }
            }

            if (type.instanceOf(MetaType.PLANT) && !type.instanceOf(MetaType.UNDERWATER_PLANT)) {
                SWORD_EFFICIENT.add(block);
            }

            if (type.instanceOf(MetaType.LEAVES)) {
                LEAVES.add(block);
                MINEABLE_WITH_HOE.add(block);
            }

            if (type.instanceOf(MetaType.LEAF_LITTER)) {
                INSIDE_STEP_SOUND_BLOCKS.add(block);
                ENCHANTMENT_POWER_TRANSMITTER.add(block);

                if (replaceable) {
                    REPLACEABLE.add(block);
                    REPLACEABLE_BY_TREES.add(block);
                }
            }

            if (type.instanceOf(MetaType.SAPLING)) {
                SAPLINGS.add(block);
                MINEABLE_WITH_AXE.add(block);
            }

            if (type.isMushroomLike()) {
                MINEABLE_WITH_AXE.add(block);

                if (type.instanceOf(MetaType.MUSHROOM)) {
                    ENDERMAN_HOLDABLE.add(block);
                    SWORD_EFFICIENT.add(block);
                }
            }

            if (type.instanceOf(MetaType.DIRT)) {
                SNIFFER_DIGGABLE_BLOCK.add(block);
                DIRT.add(block);
                MINEABLE_WITH_SHOVEL.add(block);
            }

            if (type.instanceOf(MetaType.GRASS_BLOCK)) {
                VALID_SPAWN.add(block);
                RABBITS_SPAWNABLE_ON.add(block);
                PARROTS_SPAWNABLE_ON.add(block);
                FROGS_SPAWNABLE_ON.add(block);
                FOXES_SPAWNABLE_ON.add(block);
                ANIMALS_SPAWNABLE_ON.add(block);
                WOLVES_SPAWNABLE_ON.add(block);
            }

            if (type.instanceOf(MetaType.WATERLILY)) {
                MINEABLE_WITH_AXE.add(block);
                FROG_PREFER_JUMP_TO.add(block);
                INSIDE_STEP_SOUND_BLOCKS.add(block);
            }

            if (type.instanceOf(MetaType.SNOW)) {
                SNOW.add(block);
                MINEABLE_WITH_SHOVEL.add(block);
                COMBINATION_STEP_SOUND_BLOCKS.add(block);
            }

            if (blockMeta.shouldGenerateTag()) {
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

    public static @NotNull PlantopiaTagSet<Block> getOrCreateTagSet(TagKey<Block> key) {
        return byTagKeys.computeIfAbsent(key, k -> PlantopiaTagSet.newTagSet());
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
