package by.langvest.plantopia.datagen.lang;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.util.helper.PlantopiaStringHelper;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

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
		var key = PlantopiaTemplateHelper.getCreativeModeTabTitleKey(nameOf(tab));

		add(key, name);
	}

	private void add(@NotNull PlantopiaAdvancement advancement, String title, String description) {
		var advancementMeta = PlantopiaMetaRegistries.ADVANCEMENTS.getValueOrThrow(advancement);

		add(advancementMeta.getTitleKey(), title);
		add(advancementMeta.getDescriptionKey(), description);
	}

	private void generateAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
			if(!blockMeta.shouldGenerateTranslation()) return;

			var block = blockMeta.getBlock();

			add(block, getDisplayNameById(blockMeta.getName()));
		});
	}

	private String getDisplayNameById(@NotNull String id) {
		return Arrays.stream(id.split("_"))
			.map(PlantopiaStringHelper::capitalize)
			.collect(Collectors.joining(" "));
	}
}