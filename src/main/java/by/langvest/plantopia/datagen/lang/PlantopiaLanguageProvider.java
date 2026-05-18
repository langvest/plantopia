package by.langvest.plantopia.datagen.lang;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.special.PlantopiaSimpleAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancements;
import by.langvest.plantopia.client.lang.PlantopiaLangKey;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.damage.PlantopiaDamageTypes;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.biome.Biome;
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

		advancement(PlantopiaAdvancements.ROOT, "Plantopia", "A plant lover's utopia!");
		advancement(PlantopiaAdvancements.COLLECT_ALL_FLOWERS, "A Million-Dollar Bouquet", "Complete your flower collection");
		advancement(PlantopiaAdvancements.COLLECT_ALL_HERBS, "Local Herbalist", "Gather every herb under the sun... almost");
		advancement(PlantopiaAdvancements.COLLECT_ALL_MUSHROOMS, "Certified 'Shroom Enjoyer", "Become a true connoisseur of fungi");
		advancement(PlantopiaAdvancements.PLACE_HOGWEED, "Secret Agent of Hogweed", "Do your humble part to help Hogweed take over the world!");
		advancement(PlantopiaAdvancements.PLACE_COBBLESTONE_SHARD_PET, "Rock Solid Friendship", "Get yourself a friend who will never run away... because it can't move!");
		advancement(PlantopiaAdvancements.WALK_ON_QUICKSAND_WITH_LEATHER_BOOTS, "Antigravity Tricks", "Walk on Quicksand... like a greatest illusionist!");
		advancement(PlantopiaAdvancements.PLUCK_LUCKY_DAISY_PETAL, "Loves me, loves me not", "Ask the Lucky Daisy and pluck your fate");
		advancement(PlantopiaAdvancements.OBTAIN_TANSY, "Tansy? You mean fancy?!", "No time to explain... just keep going");

		damageType(PlantopiaDamageTypes.THORNY_SHRUB, "%1$s was poked to death by a thorny shrub");
		damageType(PlantopiaDamageTypes.THORNY_SHRUB, "player", "%1$s was poked to death by a thorny shrub whilst trying to escape %2$s");
		damageType(PlantopiaDamageTypes.QUICKSAND, "%1$s drowned in quicksand");

		soundEvent(PlantopiaSoundEvents.DROWNED_CONVERTED_TO_ZOMBIE, "Drowned converts to Zombie");
		soundEvent(PlantopiaSoundEvents.ZOMBIE_CONVERTED_TO_HUSK, "Zombie converts to Husk");

		add(PlantopiaLangKey.TOOLTIP_RANDOM_VARIANT, "Random variant");
		add(PlantopiaLangKey.TOOLTIP_REMAINING_PETALS, "Petals left: %s");
		add(PlantopiaLangKey.TOOLTIP_NO_PETALS, "No petals left");
	}

	@SuppressWarnings("SameParameterValue")
	private void tab(@NotNull ResourceKey<CreativeModeTab> tab, String name) {
		var key = PlantopiaTemplateHelper.getCreativeModeTabTitleKey(tab.location());

		add(key, name);
	}

	private void biome(@NotNull ResourceKey<Biome> tab, String name) {
		var key = PlantopiaTemplateHelper.getBiomeTitleKey(tab.location());

		add(key, name);
	}

	private void advancement(@NotNull RegistryObject<PlantopiaSimpleAdvancement> advancement, String title, String description) {
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

	private void entityType(@NotNull RegistryObject<? extends EntityType<?>> entityType, String title) {
		add(PlantopiaTemplateHelper.getEntityTypeTitleKey(entityType.getIdentifier()), title);
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

		PlantopiaBiomes.DECLARATION.forEach((biomeKey, declaration) -> {
			biome(biomeKey, getDisplayNameById(declaration.getName()));
		});
	}

	private String getDisplayNameById(@NotNull String id) {
		return Arrays.stream(id.split("_"))
			.map(PlantopiaStringHelper::capitalize)
			.collect(Collectors.joining(" "));
	}
}
