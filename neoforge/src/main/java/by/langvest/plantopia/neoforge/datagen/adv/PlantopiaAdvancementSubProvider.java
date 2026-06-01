package by.langvest.plantopia.neoforge.datagen.adv;

import by.langvest.plantopia.adv.special.PlantopiaSimpleAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.adv.PlantopiaCriteriaTriggers;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.util.helper.PlantopiaContentHelper;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.AdvancementProvider.AdvancementGenerator;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.idOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static net.minecraft.advancements.critereon.PlayerTrigger.TriggerInstance.walkOnBlockWithEquipment;

public class PlantopiaAdvancementSubProvider implements AdvancementGenerator {
    private static final List<ItemLike> allFlowers = PlantopiaContentHelper.getAllFlowers();
    private static final List<ItemLike> allHerbs = PlantopiaContentHelper.getAllHerbs();
    private static final List<ItemLike> allMushrooms = PlantopiaContentHelper.getAllMushrooms();
    private Consumer<AdvancementHolder> saver;

    @Override
    public void generate(HolderLookup.@NotNull Provider registryLookup, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
        setSaver(saver);

        PlantopiaAdvancements.ROOT.get()
            .getBuilder()
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(hasName(ItemTags.DIRT), has(ItemTags.DIRT))
            .addCriterion(hasName(ItemTags.SAND), has(ItemTags.SAND))
            .addCriterion(hasName(ItemTags.LOGS), has(ItemTags.LOGS))
            .addCriterion(hasName(ItemTags.FLOWERS), has(ItemTags.FLOWERS))
            .addCriterion(hasName(Items.GRAVEL), has(Items.GRAVEL));

        PlantopiaAdvancements.COLLECT_ALL_FLOWERS.get()
            .apply(PlantopiaAdvancementSubProvider::addFlowersToCollect)
            .getBuilder()
            .rewards(experience(100));

        PlantopiaAdvancements.COLLECT_ALL_HERBS.get()
            .apply(PlantopiaAdvancementSubProvider::addHerbsToCollect)
            .getBuilder()
            .rewards(experience(50));

        PlantopiaAdvancements.COLLECT_ALL_MUSHROOMS.get()
            .apply(PlantopiaAdvancementSubProvider::addMushroomsToCollect)
            .getBuilder()
            .rewards(experience(50));

        PlantopiaAdvancements.PLACE_HOGWEED.get()
            .getBuilder()
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(placeName(PlantopiaBlocks.HOGWEED.get()), place(PlantopiaBlocks.HOGWEED.get()))
            .addCriterion(placeName(PlantopiaBlocks.INFESTED_DIRT.get()), place(PlantopiaBlocks.INFESTED_DIRT.get()))
            .addCriterion(placeName(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()), place(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()));

        PlantopiaAdvancements.PLACE_COBBLESTONE_SHARD_PET.get()
            .getBuilder()
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(placeName(PlantopiaBlocks.COBBLESTONE_SHARD_PET.get()), place(PlantopiaBlocks.COBBLESTONE_SHARD_PET.get()))
            .addCriterion(placeName(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get()), place(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get()));

        PlantopiaAdvancements.WALK_ON_QUICKSAND_WITH_LEATHER_BOOTS.get()
            .getBuilder()
            .addCriterion(walkOnBlockWithEquipmentName(PlantopiaBlocks.QUICKSAND.get(), Items.LEATHER_BOOTS), walkOnBlockWithEquipment(PlantopiaBlocks.QUICKSAND.get(), Items.LEATHER_BOOTS));

        PlantopiaAdvancements.PLUCK_LUCKY_DAISY_PETAL.get()
            .getBuilder()
            .requirements(AdvancementRequirements.Strategy.OR)
            .addCriterion(interactWithName(PlantopiaBlocks.WHITE_LUCKY_DAISY.get()), interactWith(PlantopiaBlocks.WHITE_LUCKY_DAISY.get()))
            .addCriterion(interactWithName(PlantopiaBlocks.PINK_LUCKY_DAISY.get()), interactWith(PlantopiaBlocks.PINK_LUCKY_DAISY.get()));

        PlantopiaAdvancements.OBTAIN_TANSY.get()
            .getBuilder()
            .addCriterion(hasName(PlantopiaBlocks.TANSY.get()), has(PlantopiaBlocks.TANSY.get()));

        saveAll();
    }

    private void setSaver(@NotNull Consumer<AdvancementHolder> saver) {
        this.saver = saver;
    }

    private void saveAll() {
        PlantopiaMetaBuckets.ADVANCEMENT.forEach(advancementMeta -> {
            var advancement = advancementMeta.get();
            var parent = advancementMeta.getParent();
            var builder = advancement.getBuilder();

            builder.display(
                advancementMeta.getIcon(),
                advancementMeta.getTitle(),
                advancementMeta.getDescription(),
                advancementMeta.getBackground(),
                advancementMeta.getFrameType(),
                advancementMeta.shouldShowToast(),
                advancementMeta.shouldAnnounceToChat(),
                advancementMeta.isHidden()
            );

            if (parent != null && parent.getInstance() != null) builder.parent(parent.getInstance());

            advancement.save(saver);
        });
    }

    /* CRITERIA GENERATION ******************************************/

    private static void addFlowersToCollect(PlantopiaSimpleAdvancement advancement) {
        for (ItemLike itemLike : allFlowers) advancement.getBuilder().addCriterion(idOf(itemLike), has(itemLike));
    }

    private static void addHerbsToCollect(PlantopiaSimpleAdvancement advancement) {
        for (ItemLike itemLike : allHerbs) advancement.getBuilder().addCriterion(idOf(itemLike), has(itemLike));
    }

    private static void addMushroomsToCollect(PlantopiaSimpleAdvancement advancement) {
        for (ItemLike itemLike : allMushrooms) advancement.getBuilder().addCriterion(idOf(itemLike), has(itemLike));
    }

    /* HELPER METHODS ******************************************/

    private static @NotNull String interactWithName(@NotNull Block block) {
        return "interact_with_" + nameOf(block);
    }

    private static @NotNull String hasName(@NotNull ItemLike item) {
        return "has_" + nameOf(item.asItem());
    }

    private static @NotNull String placeName(@NotNull Block block) {
        return "place_" + nameOf(block);
    }

    private static @NotNull String hasName(@NotNull TagKey<Item> tag) {
        return "has_" + nameOf(tag);
    }

    private static @NotNull String walkOnBlockWithEquipmentName(@NotNull Block block, @NotNull Item item) {
        return "walk_on_" + nameOf(block) + "_with_" + nameOf(item);
    }

    private static @NotNull Criterion<?> has(ItemLike... items) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(items);
    }

    @SafeVarargs
    private static @NotNull Criterion<?> has(TagKey<Item>... tags) {
        ItemPredicate[] predicates = Arrays.stream(tags).map(tag -> ItemPredicate.Builder.item().of(tag).build()).toArray(ItemPredicate[]::new);
        return InventoryChangeTrigger.TriggerInstance.hasItems(predicates);
    }

    private static @NotNull Criterion<?> place(Block block) {
        return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block);
    }

    private static @NotNull Criterion<?> interactWith(Block block) {
        return PlantopiaCriteriaTriggers.interactedWith(block);
    }

    private static AdvancementRewards.@NotNull Builder experience(int amount) {
        return AdvancementRewards.Builder.experience(amount);
    }
}
