package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.trunkplacer.PlantopiaStraightTrunkPlacer;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaTrunkPlacerTypes {
    public static final RegistryObject<TrunkPlacerType<PlantopiaStraightTrunkPlacer>> STRAIGHT_TRUNK_PLACER = registerTrunkPlacerType("straight_trunk_placer", () -> new TrunkPlacerType<>(PlantopiaStraightTrunkPlacer.CODEC));

    private static <T extends TrunkPlacer> RegistryObject<TrunkPlacerType<T>> registerTrunkPlacerType(String name, Supplier<TrunkPlacerType<T>> supplier) {
        return registerTrunkPlacerType(plantopia(name), supplier);
    }

    private static <T extends TrunkPlacer> RegistryObject<TrunkPlacerType<T>> registerTrunkPlacerType(ResourceLocation identifier, Supplier<TrunkPlacerType<T>> supplier) {
        return PlantopiaRegistries.TRUNK_PLACER_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.TRUNK_PLACER_TYPE, PlantopiaRegistries.TRUNK_PLACER_TYPE);
    }
}
