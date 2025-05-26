package by.langvest.plantopia.datagen.sound;

import by.langvest.plantopia.Plantopia;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition.Sound;
import net.minecraftforge.common.data.SoundDefinition.SoundType;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaContentHelper.plantopia;

public class PlantopiaSoundProvider extends SoundDefinitionsProvider {
	public PlantopiaSoundProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	public void registerSounds() {
		registerAll();
	}

	private void registerAll() {}

	@Contract("_ -> new")
	protected static @NotNull Sound sound(String name) {
		return Sound.sound(plantopia(name), SoundType.SOUND);
	}

	@Contract("_ -> new")
	protected static @NotNull Sound event(String name) {
		return Sound.sound(plantopia(name), SoundType.EVENT);
	}
}