package by.langvest.plantopia.datagen.lang;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.entity.PlantopiaDamageTypes;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.util.helper.PlantopiaStringHelper;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
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

		tab(PlantopiaCreativeModeTabs.PLANTOPIA, "Plantopia");

		advancement(PlantopiaAdvancements.ROOT, "Plantopia", "What a wonderful world!");
		advancement(PlantopiaAdvancements.COLLECT_ALL_FLOWERS, "Real Gardener", "Collect one of every flower");
		advancement(PlantopiaAdvancements.PLACE_HOGWEED, "Ecological Disaster", "Let the hogweed take over more and more territories");
		advancement(PlantopiaAdvancements.PLACE_COBBLESTONE_SHARD_PET, "A stone is a man's best friend", "Get yourself the most devoted friend!");

		damageType(PlantopiaDamageTypes.THORNY_SHRUB, "%1$s was poked to death by a thorny shrub");
		damageType(PlantopiaDamageTypes.THORNY_SHRUB, "player", "%1$s was poked to death by a thorny shrub whilst trying to escape %2$s");
	}

	@SuppressWarnings("SameParameterValue")
	private void tab(@NotNull ResourceKey<CreativeModeTab> tab, String name) {
		var key = PlantopiaTemplateHelper.getCreativeModeTabTitleKey(nameOf(tab));

		add(key, name);
	}

	private void advancement(@NotNull PlantopiaAdvancement advancement, String title, String description) {
		var advancementMeta = PlantopiaMetaRegistries.ADVANCEMENTS.getValueOrThrow(advancement);

		add(advancementMeta.getTitleKey(), title);
		add(advancementMeta.getDescriptionKey(), description);
	}

	private void damageType(@NotNull ResourceKey<DamageType> damageType, String title) {
		damageType(damageType, null, title);
	}

	private void damageType(@NotNull ResourceKey<DamageType> damageType, String qualifier, String title) {
		String messageId = PlantopiaStringHelper.toCamelCase(nameOf(damageType));

		add(PlantopiaTemplateHelper.getDamageTypeTitleKey(messageId, qualifier), title);
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