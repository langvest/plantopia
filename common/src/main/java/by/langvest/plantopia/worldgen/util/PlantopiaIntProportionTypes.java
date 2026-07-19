package by.langvest.plantopia.worldgen.util;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaFixedIntProportion;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaRelativeIntProportion;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaIntProportionTypes {
    public static final RegistryObject<PlantopiaIntProportionType<PlantopiaFixedIntProportion>> FIXED = registerIntProportionType("fixed_proportion", () -> new PlantopiaIntProportionType<>(PlantopiaFixedIntProportion.CODEC));
    public static final RegistryObject<PlantopiaIntProportionType<PlantopiaRelativeIntProportion>> RELATIVE = registerIntProportionType("relative_proportion", () -> new PlantopiaIntProportionType<>(PlantopiaRelativeIntProportion.CODEC));

    private static <T extends PlantopiaIntProportion> @NotNull RegistryObject<PlantopiaIntProportionType<T>> registerIntProportionType(String name, Supplier<PlantopiaIntProportionType<T>> supplier) {
        return registerIntProportionType(plantopia(name), supplier);
    }

    private static <T extends PlantopiaIntProportion> RegistryObject<PlantopiaIntProportionType<T>> registerIntProportionType(ResourceLocation identifier, Supplier<PlantopiaIntProportionType<T>> supplier) {
        return PlantopiaRegistries.INT_PROPORTION_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {}
}
