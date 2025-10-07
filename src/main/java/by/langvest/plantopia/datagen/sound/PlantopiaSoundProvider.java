package by.langvest.plantopia.datagen.sound;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.sound.PlantopiaSoundEvents;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition.Sound;
import net.minecraftforge.common.data.SoundDefinition.SoundType;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaSoundProvider extends SoundDefinitionsProvider {
	public PlantopiaSoundProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	public void registerSounds() {
		registerAll();

		add(PlantopiaSoundEvents.DROWNED_CONVERTED_TO_ZOMBIE, definition()
			.subtitle(subtitleKeyOf(PlantopiaSoundEvents.DROWNED_CONVERTED_TO_ZOMBIE))
			.with(minecraftSound("mob", "zombie", "say1"))
			.with(minecraftSound("mob", "zombie", "say2"))
			.with(minecraftSound("mob", "zombie", "say3"))
		);

		add(PlantopiaSoundEvents.ZOMBIE_CONVERTED_TO_HUSK, definition()
			.subtitle(subtitleKeyOf(PlantopiaSoundEvents.ZOMBIE_CONVERTED_TO_HUSK))
			.with(minecraftSound("mob", "husk", "idle1"))
			.with(minecraftSound("mob", "husk", "idle2"))
			.with(minecraftSound("mob", "husk", "idle3"))
		);
	}

	private void registerAll() {}

	@Contract("_ -> new")
	protected static @NotNull Sound sound(String... path) {
		return Sound.sound(plantopiaLocationFrom(path), SoundType.SOUND);
	}

	@Contract("_ -> new")
	protected static @NotNull Sound minecraftSound(String... path) {
		return Sound.sound(minecraftLocationFrom(path), SoundType.SOUND);
	}

	@Contract("_ -> new")
	protected static @NotNull Sound event(String name) {
		return Sound.sound(plantopiaLocationFrom(name), SoundType.EVENT);
	}

	@Contract("_ -> new")
	protected static @NotNull String subtitleKeyOf(RegistryObject<SoundEvent> soundEvent) {
		return PlantopiaTemplateHelper.getSoundEventSubtitleKey(nameOf(soundEvent));
	}
}