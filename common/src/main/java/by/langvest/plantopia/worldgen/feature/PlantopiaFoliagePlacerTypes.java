package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaCypressFoliagePlacer;
import by.langvest.plantopia.worldgen.feature.foliageplacer.PlantopiaLushFoliagePlacer;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaFoliagePlacerTypes {
    public static final RegistryObject<FoliagePlacerType<PlantopiaLushFoliagePlacer>> LUSH_FOLIAGE_PLACER = registerFoliagePlacerType("lush_foliage_placer", () -> new FoliagePlacerType<>(PlantopiaLushFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<PlantopiaCypressFoliagePlacer>> CYPRESS_FOLIAGE_PLACER = registerFoliagePlacerType("cypress_foliage_placer", () -> new FoliagePlacerType<>(PlantopiaCypressFoliagePlacer.CODEC));

    private static <T extends FoliagePlacer> RegistryObject<FoliagePlacerType<T>> registerFoliagePlacerType(String name, Supplier<FoliagePlacerType<T>> supplier) {
        return registerFoliagePlacerType(plantopia(name), supplier);
    }

    private static <T extends FoliagePlacer> RegistryObject<FoliagePlacerType<T>> registerFoliagePlacerType(ResourceLocation identifier, Supplier<FoliagePlacerType<T>> supplier) {
        return PlantopiaRegistries.FOLIAGE_PLACER_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.FOLIAGE_PLACER_TYPE, PlantopiaRegistries.FOLIAGE_PLACER_TYPE);
    }
}
