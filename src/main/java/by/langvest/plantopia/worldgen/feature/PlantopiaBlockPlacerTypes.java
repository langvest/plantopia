package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaGradientBlockPlacer;
import by.langvest.plantopia.worldgen.feature.blockplacer.PlantopiaSimpleBlockPlacer;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockPlacerTypes {
    public static final RegistryObject<PlantopiaBlockPlacerType<PlantopiaSimpleBlockPlacer>> SIMPLE = registerGaussianBlockType("simple_block", () -> new PlantopiaBlockPlacerType<>(PlantopiaSimpleBlockPlacer.CODEC));
    public static final RegistryObject<PlantopiaBlockPlacerType<PlantopiaGradientBlockPlacer>> GRADIENT = registerGaussianBlockType("gradient_block", () -> new PlantopiaBlockPlacerType<>(PlantopiaGradientBlockPlacer.CODEC));

    private static <T extends PlantopiaBlockPlacer> @NotNull RegistryObject<PlantopiaBlockPlacerType<T>> registerGaussianBlockType(String name, Supplier<PlantopiaBlockPlacerType<T>> supplier) {
        return registerGaussianBlockType(plantopia(name), supplier);
    }

    private static <T extends PlantopiaBlockPlacer> RegistryObject<PlantopiaBlockPlacerType<T>> registerGaussianBlockType(ResourceLocation identifier, Supplier<PlantopiaBlockPlacerType<T>> supplier) {
        return PlantopiaRegistries.BLOCK_PLACER_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {}
}
