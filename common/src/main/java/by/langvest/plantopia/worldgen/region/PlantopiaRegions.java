package by.langvest.plantopia.worldgen.region;

import by.langvest.plantopia.event.PlantopiaTerraBlenderEvent;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.region.special.PlantopiaPrimaryOverworldRegion;
import by.langvest.plantopia.worldgen.region.special.PlantopiaRegion;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaRegions {
    public static final RegistryObject<PlantopiaRegion> OVERWORLD_PRIMARY = registerRegion("overworld_primary", name -> new PlantopiaPrimaryOverworldRegion(name, 10));

    public static <T extends PlantopiaRegion> RegistryObject<T> registerRegion(String name, Function<ResourceLocation, T> factory) {
        return registerRegion(plantopia(name), factory);
    }

    public static <T extends PlantopiaRegion> RegistryObject<T> registerRegion(ResourceLocation identifier, Function<ResourceLocation, T> factory) {
        return PlantopiaRegistries.REGION.register(identifier, () -> factory.apply(identifier));
    }

    public static void setup(PlantopiaTerraBlenderEvent.@NotNull Region event) {
        event.registerAll(PlantopiaRegistries.REGION);
    }
}
