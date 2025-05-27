package by.langvest.plantopia.sound;

import by.langvest.plantopia.Plantopia;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PlantopiaSoundEvents {
	private static final DeferredRegister<SoundEvent> SOUND_EVENT_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Plantopia.MOD_ID);

	// public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
	// 	return SOUND_EVENT_REGISTER.register(name, () -> new SoundEvent(new PlantopiaIdentifier(name)));
	// }

	public static void setup(IEventBus bus) {
		SOUND_EVENT_REGISTER.register(bus);
	}
}