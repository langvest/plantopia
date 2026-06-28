package by.langvest.plantopia.entity;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBoatTypes {
    public static RegistryObject<PlantopiaBoatType> registerBoatType(String name, Supplier<PlantopiaBoatType> supplier) {
        return registerBoatType(plantopia(name), supplier);
    }

    public static RegistryObject<PlantopiaBoatType> registerBoatType(ResourceLocation identifier, Supplier<PlantopiaBoatType> supplier) {
        return PlantopiaRegistries.BOAT_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {}
}
