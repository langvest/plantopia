package by.langvest.plantopia.datagen.adv;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.adv.trigger.special.PlantopiaBlockInteractTrigger;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.util.helper.PlantopiaContentHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
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
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider.AdvancementGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.idOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static net.minecraft.advancements.critereon.PlayerTrigger.TriggerInstance.walkOnBlockWithEquipment;

public class PlantopiaAdvancementSubProvider implements AdvancementGenerator {
	private static final List<ItemLike> ALL_FLOWERS = PlantopiaContentHelper.getAllFlowers();
	private Consumer<Advancement> consumer;

	@Override
	public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper existingFileHelper) {
		setConsumer(consumer);

		PlantopiaAdvancements.ROOT.get()
			.getBuilder()
			.requirements(RequirementsStrategy.OR)
			.addCriterion(getHasName(ItemTags.DIRT), has(ItemTags.DIRT))
			.addCriterion(getHasName(ItemTags.SAND), has(ItemTags.SAND))
			.addCriterion(getHasName(ItemTags.LOGS), has(ItemTags.LOGS))
			.addCriterion(getHasName(ItemTags.FLOWERS), has(ItemTags.FLOWERS))
			.addCriterion(getHasName(Items.GRAVEL), has(Items.GRAVEL));

		PlantopiaAdvancements.COLLECT_ALL_FLOWERS.get()
			.apply(PlantopiaAdvancementSubProvider::addFlowersToCollect)
			.getBuilder()
			.rewards(experience(100));

		PlantopiaAdvancements.PLACE_HOGWEED.get()
			.getBuilder()
			.requirements(RequirementsStrategy.OR)
			.addCriterion(getPlaceName(PlantopiaBlocks.HOGWEED.get()), place(PlantopiaBlocks.HOGWEED.get()))
			.addCriterion(getPlaceName(PlantopiaBlocks.INFESTED_DIRT.get()), place(PlantopiaBlocks.INFESTED_DIRT.get()))
			.addCriterion(getPlaceName(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()), place(PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()));

		PlantopiaAdvancements.PLACE_COBBLESTONE_SHARD_PET.get()
			.getBuilder()
			.requirements(RequirementsStrategy.OR)
			.addCriterion(getPlaceName(PlantopiaBlocks.COBBLESTONE_SHARD_PET.get()), place(PlantopiaBlocks.COBBLESTONE_SHARD_PET.get()))
			.addCriterion(getPlaceName(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get()), place(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get()));

		PlantopiaAdvancements.WALK_ON_QUICKSAND_WITH_LEATHER_BOOTS.get()
			.getBuilder()
			.addCriterion(getWalkOnBlockWithEquipmentName(PlantopiaBlocks.QUICKSAND.get(), Items.LEATHER_BOOTS), walkOnBlockWithEquipment(PlantopiaBlocks.QUICKSAND.get(), Items.LEATHER_BOOTS));

		PlantopiaAdvancements.PLUCK_LUCKY_DAISY_PETAL.get()
			.getBuilder()
			.requirements(RequirementsStrategy.OR)
			.addCriterion(getInteractWithName(PlantopiaBlocks.WHITE_LUCKY_DAISY.get()), interactWith(PlantopiaBlocks.WHITE_LUCKY_DAISY.get()))
			.addCriterion(getInteractWithName(PlantopiaBlocks.PINK_LUCKY_DAISY.get()), interactWith(PlantopiaBlocks.PINK_LUCKY_DAISY.get()));

		saveAll();
	}

	private void setConsumer(@NotNull Consumer<Advancement> consumer) {
		this.consumer = consumer;
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

			if(parent != null && parent.getInstance() != null) builder.parent(parent.getInstance());

			advancement.save(consumer);
		});
	}

	/* CRITERIA GENERATION ******************************************/

	private static void addFlowersToCollect(PlantopiaAdvancement advancement) {
		for(ItemLike itemLike : ALL_FLOWERS) advancement.getBuilder().addCriterion(idOf(itemLike), has(itemLike));
	}

	/* HELPER METHODS ******************************************/

	private static @NotNull String getInteractWithName(@NotNull Block block) {
		return "interact_with_" + nameOf(block);
	}

	private static @NotNull String getHasName(@NotNull ItemLike item) {
		return "has_" + nameOf(item.asItem());
	}

	private static @NotNull String getPlaceName(@NotNull Block block) {
		return "place_" + nameOf(block);
	}

	private static @NotNull String getHasName(@NotNull TagKey<Item> tag) {
		return "has_" + nameOf(tag);
	}

	private static @NotNull String getWalkOnBlockWithEquipmentName(@NotNull Block block, @NotNull Item item) {
		return "walk_on_" + nameOf(block) + "_with_" + nameOf(item);
	}

	private static InventoryChangeTrigger.@NotNull TriggerInstance has(ItemLike... items) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(items);
	}

	@SafeVarargs
	private static InventoryChangeTrigger.@NotNull TriggerInstance has(TagKey<Item>... tags) {
		ItemPredicate[] predicates = Arrays.stream(tags).map(tag -> ItemPredicate.Builder.item().of(tag).build()).toArray(ItemPredicate[]::new);
		return InventoryChangeTrigger.TriggerInstance.hasItems(predicates);
	}

	private static ItemUsedOnLocationTrigger.@NotNull TriggerInstance place(Block block) {
		return ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(block);
	}

	private static PlantopiaBlockInteractTrigger.@NotNull TriggerInstance interactWith(Block block) {
		return PlantopiaBlockInteractTrigger.TriggerInstance.interactedWith(block);
	}

	private static AdvancementRewards.@NotNull Builder experience(int amount) {
		return AdvancementRewards.Builder.experience(amount);
	}
}
