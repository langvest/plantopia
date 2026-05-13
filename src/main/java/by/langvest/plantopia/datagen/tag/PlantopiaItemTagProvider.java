package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlantopiaItemTagProvider extends ItemTagsProvider implements PlantopiaTagProvider<Item> {
    private static final Map<TagKey<Item>, PlantopiaTagSet<Item>> byTagKeys = Maps.newHashMap();
    private static final Map<PlantopiaItemMeta.MetaType, PlantopiaTagSet<Item>> byItemMetaTypes = Maps.newHashMap();
    private static final Map<PlantopiaBlockMeta.MetaType, PlantopiaTagSet<Item>> byBlockMetaTypes = Maps.newHashMap();

    public static final PlantopiaTagSet<Item> TALL_FLOWERS = getOrCreateTagSet(ItemTags.TALL_FLOWERS);
    public static final PlantopiaTagSet<Item> SMALL_FLOWERS = getOrCreateTagSet(ItemTags.SMALL_FLOWERS);
    public static final PlantopiaTagSet<Item> LEAVES = getOrCreateTagSet(ItemTags.LEAVES);
    public static final PlantopiaTagSet<Item> SAPLINGS = getOrCreateTagSet(ItemTags.SAPLINGS);
    public static final PlantopiaTagSet<Item> IGNORED_BY_BEES = getOrCreateTagSet(PlantopiaItemTags.IGNORED_BY_BEES);
    public static final PlantopiaTagSet<Item> PREFERRED_BY_BEES = getOrCreateTagSet(PlantopiaItemTags.PREFERRED_BY_BEES);
    public static final PlantopiaTagSet<Item> BIRCH_LOGS = getOrCreateTagSet(ItemTags.BIRCH_LOGS);
    public static final PlantopiaTagSet<Item> DIRT = getOrCreateTagSet(ItemTags.DIRT);
    public static final PlantopiaTagSet<Item> PLANKS = getOrCreateTagSet(ItemTags.PLANKS);
    public static final PlantopiaTagSet<Item> STAIRS = getOrCreateTagSet(ItemTags.STAIRS);
    public static final PlantopiaTagSet<Item> WOODEN_STAIRS = getOrCreateTagSet(ItemTags.WOODEN_STAIRS);
    public static final PlantopiaTagSet<Item> SLABS = getOrCreateTagSet(ItemTags.SLABS);
    public static final PlantopiaTagSet<Item> WOODEN_SLABS = getOrCreateTagSet(ItemTags.WOODEN_SLABS);
    public static final PlantopiaTagSet<Item> FENCES = getOrCreateTagSet(ItemTags.FENCES);
    public static final PlantopiaTagSet<Item> WOODEN_FENCES = getOrCreateTagSet(ItemTags.WOODEN_FENCES);
    public static final PlantopiaTagSet<Item> FENCE_GATES = getOrCreateTagSet(ItemTags.FENCE_GATES);
    public static final PlantopiaTagSet<Item> DOORS = getOrCreateTagSet(ItemTags.DOORS);
    public static final PlantopiaTagSet<Item> WOODEN_DOORS = getOrCreateTagSet(ItemTags.WOODEN_DOORS);
    public static final PlantopiaTagSet<Item> TRAPDOORS = getOrCreateTagSet(ItemTags.TRAPDOORS);
    public static final PlantopiaTagSet<Item> WOODEN_TRAPDOORS = getOrCreateTagSet(ItemTags.WOODEN_TRAPDOORS);
    public static final PlantopiaTagSet<Item> WOODEN_PRESSURE_PLATES = getOrCreateTagSet(ItemTags.WOODEN_PRESSURE_PLATES);
    public static final PlantopiaTagSet<Item> BUTTONS = getOrCreateTagSet(ItemTags.BUTTONS);
    public static final PlantopiaTagSet<Item> WOODEN_BUTTONS = getOrCreateTagSet(ItemTags.WOODEN_BUTTONS);
    public static final PlantopiaTagSet<Item> SIGNS = getOrCreateTagSet(ItemTags.SIGNS);
    public static final PlantopiaTagSet<Item> HANGING_SIGNS = getOrCreateTagSet(ItemTags.HANGING_SIGNS);
    public static final PlantopiaTagSet<Item> LOGS_THAT_BURN = getOrCreateTagSet(ItemTags.LOGS_THAT_BURN);

    public PlantopiaItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PlantopiaBlockTagProvider.getInstance().contentsGetter(), Plantopia.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        generateAll();

        IGNORED_BY_BEES.add(Blocks.WITHER_ROSE.asItem());
        BIRCH_LOGS.add(PlantopiaBlocks.BIRCH_BASE_LOG.get().asItem(), PlantopiaBlocks.BIRCH_BASE_WOOD.get().asItem());

        saveAll();
    }

    private void generateAll() {
        var workScheduler = Plantopia.getPlatform().getWorkScheduler();

        workScheduler.executeWork("item_tag_datagen");

        PlantopiaMetaBuckets.ITEM.forEach(itemMeta -> {
            var item = itemMeta.get();
            var type = itemMeta.getType();

            if (itemMeta.shouldGenerateTag()) {
                byItemMetaTypes.computeIfAbsent(type, key -> PlantopiaTagSet.newTagSet()).add(item);
            }
        });

        PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
            if (!blockMeta.hasItem()) return;

            var block = blockMeta.get();
            var item = block.asItem();
            var type = blockMeta.getType();
            int baseHeight = blockMeta.getBlockHeightType().getBaseHeight();

            if (blockMeta.isIgnoredByBees()) IGNORED_BY_BEES.add(item);
            if (blockMeta.isPreferredByBees()) PREFERRED_BY_BEES.add(item);

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.FLOWER)) {
                if (baseHeight > 1) TALL_FLOWERS.add(item);
                else SMALL_FLOWERS.add(item);
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.LEAVES)) LEAVES.add(item);
            if (type.instanceOf(PlantopiaBlockMeta.MetaType.SAPLING)) SAPLINGS.add(item);
            if (type.instanceOf(PlantopiaBlockMeta.MetaType.DIRT)) DIRT.add(item);
            if (type.instanceOf(PlantopiaBlockMeta.MetaType.PLANKS)) PLANKS.add(item);

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.STAIRS)) {
                if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_STAIRS)) {
                    WOODEN_STAIRS.add(item);
                } else {
                    STAIRS.add(item);
                }
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.SLAB)) {
                if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_SLAB)) {
                    WOODEN_SLABS.add(item);
                } else {
                    SLABS.add(item);
                }
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.FENCE)) {
                if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_FENCE)) {
                    WOODEN_FENCES.add(item);
                } else {
                    FENCES.add(item);
                }
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.FENCE_GATE)) FENCE_GATES.add(item);

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.DOOR)) {
                if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_DOOR)) {
                    WOODEN_DOORS.add(item);
                } else {
                    DOORS.add(item);
                }
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.TRAPDOOR)) {
                if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_TRAPDOOR)) {
                    WOODEN_TRAPDOORS.add(item);
                } else {
                    TRAPDOORS.add(item);
                }
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_PRESSURE_PLATE)) WOODEN_PRESSURE_PLATES.add(item);

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.BUTTON)) {
                if (type.instanceOf(PlantopiaBlockMeta.MetaType.WOODEN_BUTTON)) {
                    WOODEN_BUTTONS.add(item);
                } else {
                    BUTTONS.add(item);
                }
            }

            if (type.instanceOf(PlantopiaBlockMeta.MetaType.SIGN)) SIGNS.add(item);
            if (type.instanceOf(PlantopiaBlockMeta.MetaType.HANGING_SIGN)) HANGING_SIGNS.add(item);

            if (blockMeta.shouldGenerateTag()) {
                byBlockMetaTypes.computeIfAbsent(type, key -> PlantopiaTagSet.newTagSet()).add(item);
            }
        });
    }

    private void saveAll() {
        saveByTagKeys(byTagKeys);
        saveByMetaTypes(byItemMetaTypes);
        saveByMetaTypes(byBlockMetaTypes);
    }

    public static @NotNull PlantopiaTagSet<Item> getOrCreateTagSet(TagKey<Item> key) {
        return byTagKeys.computeIfAbsent(key, k -> PlantopiaTagSet.newTagSet());
    }

    @Override
    public @NotNull IntrinsicTagAppender<Item> getTagAppender(TagKey<Item> key) {
        return tag(key);
    }

    @Override
    public ResourceKey<? extends Registry<Item>> getRegistryKey() {
        return Registries.ITEM;
    }
}
