package by.langvest.plantopia.sound;

import by.langvest.plantopia.Plantopia;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaSoundEvents {
	private static final DeferredRegister<SoundEvent> SOUND_EVENT_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Plantopia.MOD_ID);

	public static final RegistryObject<SoundEvent> DROWNED_CONVERTED_TO_ZOMBIE = registerSoundEvent("entity.drowned.converted_to_zombie");
	public static final RegistryObject<SoundEvent> ZOMBIE_CONVERTED_TO_HUSK = registerSoundEvent("entity.zombie.converted_to_husk");

	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENT_REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(plantopiaLocationFrom(name)));
	}

	public static void setup(IEventBus bus) {
		SOUND_EVENT_REGISTER.register(bus);
	}
}