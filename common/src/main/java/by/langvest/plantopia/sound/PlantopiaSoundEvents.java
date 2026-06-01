package by.langvest.plantopia.sound;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaSoundEvents {
    public static final RegistryObject<SoundEvent> DROWNED_CONVERTED_TO_ZOMBIE = registerSoundEvent("entity.drowned.converted_to_zombie");
    public static final RegistryObject<SoundEvent> ZOMBIE_CONVERTED_TO_HUSK = registerSoundEvent("entity.zombie.converted_to_husk");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return registerSoundEvent(plantopia(name));
    }

    private static RegistryObject<SoundEvent> registerSoundEvent(ResourceLocation identifier) {
        return PlantopiaRegistries.SOUND_EVENT.register(identifier, () -> SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.SOUND_EVENT, PlantopiaRegistries.SOUND_EVENT);
    }
}
