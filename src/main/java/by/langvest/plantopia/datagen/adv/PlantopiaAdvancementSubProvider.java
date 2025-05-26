package by.langvest.plantopia.datagen.adv;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.meta.PlantopiaMetaStore;
import by.langvest.plantopia.util.helper.PlantopiaContentHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
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

public class PlantopiaAdvancementSubProvider implements AdvancementGenerator {
	private static final List<Block> ALL_FLOWERS = PlantopiaContentHelper.getAllFlowers();
	private Consumer<Advancement> consumer;

	@Override
	public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<Advancement> consumer, @NotNull ExistingFileHelper existingFileHelper) {
		setConsumer(consumer);

		PlantopiaAdvancements.ROOT
			.getBuilder()
			.requirements(RequirementsStrategy.OR)
			.addCriterion(getHasName(ItemTags.DIRT), has(ItemTags.DIRT))
			.addCriterion(getHasName(ItemTags.SAND), has(ItemTags.SAND))
			.addCriterion(getHasName(ItemTags.LOGS), has(ItemTags.LOGS))
			.addCriterion(getHasName(ItemTags.FLOWERS), has(ItemTags.FLOWERS))
			.addCriterion(getHasName(Items.GRAVEL), has(Items.GRAVEL));

		PlantopiaAdvancements.COLLECT_ALL_FLOWERS
			.apply(PlantopiaAdvancementSubProvider::addFlowersToCollect)
			.getBuilder()
			.rewards(experience(100));

		saveAll();
	}

	private void setConsumer(@NotNull Consumer<Advancement> consumer) {
		this.consumer = consumer;
	}

	private void saveAll() {
		PlantopiaMetaStore.getAdvancements().forEach(advancementMeta -> {
			PlantopiaAdvancement advancement = advancementMeta.getAdvancement();
			PlantopiaAdvancement parent = advancementMeta.getParent();
			Advancement.Builder builder = advancement.getBuilder();

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
		for(Block block : ALL_FLOWERS) advancement.getBuilder().addCriterion(idOf(block), has(block));
	}

	/* HELPER METHODS ******************************************/

	private static @NotNull String getHasName(@NotNull ItemLike item) {
		return "has_" + nameOf(item.asItem());
	}

	private static @NotNull String getHasName(@NotNull TagKey<Item> tag) {
		return "has_" + nameOf(tag);
	}

	private static InventoryChangeTrigger.@NotNull TriggerInstance has(ItemLike... items) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(items);
	}

	@SafeVarargs
	private static InventoryChangeTrigger.@NotNull TriggerInstance has(TagKey<Item>... tags) {
		ItemPredicate[] predicates = Arrays.stream(tags).map(tag -> ItemPredicate.Builder.item().of(tag).build()).toArray(ItemPredicate[]::new);
		return InventoryChangeTrigger.TriggerInstance.hasItems(predicates);
	}

	private static AdvancementRewards.@NotNull Builder experience(int amount) {
		return AdvancementRewards.Builder.experience(amount);
	}
}
