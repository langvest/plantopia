package by.langvest.plantopia.worldgen.util;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.util.floatprovider.PlantopiaWeightedListFloat;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviderType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaFloatProviderTypes {
    public static final RegistryObject<FloatProviderType<PlantopiaWeightedListFloat>> WEIGHTED_LIST = registerFloatProviderType("weighted_list", () -> PlantopiaWeightedListFloat.CODEC);

    private static <T extends FloatProvider> RegistryObject<FloatProviderType<T>> registerFloatProviderType(String name, FloatProviderType<T> type) {
        return registerFloatProviderType(plantopia(name), type);
    }

    private static <T extends FloatProvider> RegistryObject<FloatProviderType<T>> registerFloatProviderType(ResourceLocation identifier, FloatProviderType<T> type) {
        return PlantopiaRegistries.FLOAT_PROVIDER_TYPE.register(identifier, () -> type);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.FLOAT_PROVIDER_TYPE, PlantopiaRegistries.FLOAT_PROVIDER_TYPE);
    }
}
