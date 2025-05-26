package by.langvest.plantopia.datagen.lang;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.meta.object.PlantopiaAdvancementMeta;
import by.langvest.plantopia.meta.store.PlantopiaMetaStore;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.util.PlantopiaStringHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static by.langvest.plantopia.util.PlantopiaContentHelper.nameOf;
import static by.langvest.plantopia.util.PlantopiaTemplateHelper.creativeModeTabTitle;

public class PlantopiaLanguageProvider extends LanguageProvider {
	public PlantopiaLanguageProvider(PackOutput output) {
		super(output, Plantopia.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		generateAll();

		add(PlantopiaCreativeModeTabs.PLANTOPIA, "Plantopia");

		add(PlantopiaAdvancements.ROOT, "Plantopia", "What a wonderful world!");
		add(PlantopiaAdvancements.COLLECT_ALL_FLOWERS, "Real Gardener", "Collect one of every flower");
	}

	@SuppressWarnings("SameParameterValue")
	private void add(@NotNull ResourceKey<CreativeModeTab> tab, String name) {
		add(creativeModeTabTitle(nameOf(tab)), name);
	}

	private void add(@NotNull PlantopiaAdvancement advancement, String title, String description) {
		PlantopiaAdvancementMeta advancementMeta = Objects.requireNonNull(PlantopiaMetaStore.getAdvancement(advancement));
		TranslatableContents titleContents = (TranslatableContents)advancementMeta.getTitle().getContents();
		TranslatableContents descriptionContents = (TranslatableContents)advancementMeta.getDescription().getContents();

		add(titleContents.getKey(), title);
		add(descriptionContents.getKey(), description);
	}

	private void generateAll() {
		PlantopiaMetaStore.getBlocks().forEach(blockMeta -> {
			if(!blockMeta.shouldGenerateTranslation()) return;

			Block block = blockMeta.getBlock();

			add(block, getDisplayNameById(blockMeta.getName()));
		});
	}

	private String getDisplayNameById(@NotNull String id) {
		return Arrays.stream(id.split("_"))
			.map(PlantopiaStringHelper::capitalize)
			.collect(Collectors.joining(" "));
	}
}