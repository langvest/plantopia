package by.langvest.plantopia.datagen.lang;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.misc.PlantopiaDamageTypes;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.plantopia.util.helper.PlantopiaStringHelper;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
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

		tab(PlantopiaCreativeModeTabs.MAIN, "Plantopia");

		advancement(PlantopiaAdvancements.ROOT, "Plantopia", "What a wonderful world!");
		advancement(PlantopiaAdvancements.COLLECT_ALL_FLOWERS, "Real Gardener", "Collect one of every flower");
		advancement(PlantopiaAdvancements.PLACE_HOGWEED, "Hogweed every day", "Do your part to help Hogweed take over the world!");
		advancement(PlantopiaAdvancements.PLACE_COBBLESTONE_SHARD_PET, "A Stone is a Man's Best Friend", "Get yourself the most devoted friend!");
		advancement(PlantopiaAdvancements.WALK_ON_QUICKSAND_WITH_LEATHER_BOOTS, "Light as a Grain of Sand", "Walk on Quicksand as if it were just ordinary Sand");
		advancement(PlantopiaAdvancements.PLUCK_LUCKY_DAISY_PETAL, "Loves me, loves me not", "Ask the Lucky Daisy and pluck your fate!");

		damageType(PlantopiaDamageTypes.THORNY_SHRUB, "%1$s was poked to death by a thorny shrub");
		damageType(PlantopiaDamageTypes.THORNY_SHRUB, "player", "%1$s was poked to death by a thorny shrub whilst trying to escape %2$s");
		damageType(PlantopiaDamageTypes.QUICKSAND, "%1$s drowned in quicksand");

		soundEvent(PlantopiaSoundEvents.DROWNED_CONVERTED_TO_ZOMBIE, "Drowned converts to Zombie");
		soundEvent(PlantopiaSoundEvents.ZOMBIE_CONVERTED_TO_HUSK, "Zombie converts to Husk");

		add(PlantopiaTemplateHelper.TOOLTIP_RANDOM_VARIANT_KEY, "Random variant");
	}

	@SuppressWarnings("SameParameterValue")
	private void tab(@NotNull ResourceKey<CreativeModeTab> tab, String name) {
		var key = PlantopiaTemplateHelper.getCreativeModeTabTitleKey(tab.location());

		add(key, name);
	}

	private void advancement(@NotNull RegistryObject<PlantopiaAdvancement> advancement, String title, String description) {
		var advancementMeta = PlantopiaMetaBuckets.ADVANCEMENT.getValueOrThrow(advancement.getIdentifier());

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

	private void soundEvent(@NotNull RegistryObject<SoundEvent> soundEvent, String subtitle) {
		add(PlantopiaTemplateHelper.getSoundEventSubtitleKey(nameOf(soundEvent)), subtitle);
	}

	private void generateAll() {
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			if(!blockMeta.shouldGenerateTranslation()) return;

			var block = blockMeta.get();

			add(block, getDisplayNameById(blockMeta.getName()));
		});

		PlantopiaMetaBuckets.ITEM.forEach(itemMeta -> {
			if(!itemMeta.shouldGenerateTranslation()) return;

			var item = itemMeta.get();

			add(item, getDisplayNameById(itemMeta.getName()));
		});
	}

	private String getDisplayNameById(@NotNull String id) {
		return Arrays.stream(id.split("_"))
			.map(PlantopiaStringHelper::capitalize)
			.collect(Collectors.joining(" "));
	}
}
