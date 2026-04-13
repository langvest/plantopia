package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaAbsoluteVerticalAnchor;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaHeightmapVerticalAnchor;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaVerticalAnchorTypes {
    public static final RegistryObject<PlantopiaVerticalAnchorType<PlantopiaAbsoluteVerticalAnchor>> ABSOLUTE = registerVerticalAnchorType("absolute_anchor", () -> new PlantopiaVerticalAnchorType<>(PlantopiaAbsoluteVerticalAnchor.CODEC));
    public static final RegistryObject<PlantopiaVerticalAnchorType<PlantopiaHeightmapVerticalAnchor>> HEIGHTMAP = registerVerticalAnchorType("heightmap_anchor", () -> new PlantopiaVerticalAnchorType<>(PlantopiaHeightmapVerticalAnchor.CODEC));

    private static <T extends PlantopiaVerticalAnchor> @NotNull RegistryObject<PlantopiaVerticalAnchorType<T>> registerVerticalAnchorType(String name, Supplier<PlantopiaVerticalAnchorType<T>> supplier) {
        return registerVerticalAnchorType(plantopia(name), supplier);
    }

    private static <T extends PlantopiaVerticalAnchor> RegistryObject<PlantopiaVerticalAnchorType<T>> registerVerticalAnchorType(ResourceLocation identifier, Supplier<PlantopiaVerticalAnchorType<T>> supplier) {
        return PlantopiaRegistries.VERTICAL_ANCHOR_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {}
}
