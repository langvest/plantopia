package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaPlacementModifierTypes {
    public static final RegistryObject<PlacementModifierType<PlantopiaNoiseCountPlacement>> NOISE_COUNT = registerPlacementModifierType("noise_count", () -> PlantopiaNoiseCountPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaNoiseFilter>> NOISE_FILTER = registerPlacementModifierType("noise_filter", () -> PlantopiaNoiseFilter.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaRangeFilter>> RANGE_FILTER = registerPlacementModifierType("range_filter", () -> PlantopiaRangeFilter.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaRarityFilter>> RARITY_FILTER = registerPlacementModifierType("rarity_filter", () -> PlantopiaRarityFilter.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaBiomeFilter>> BIOME_FILTER = registerPlacementModifierType("biome_filter", () -> PlantopiaBiomeFilter.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaEnvironmentScanFilter>> ENVIRONMENT_SCAN_FILTER = registerPlacementModifierType("environment_scan_filter", () -> PlantopiaEnvironmentScanFilter.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaDensityPlacement>> DENSITY = registerPlacementModifierType("density", () -> PlantopiaDensityPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaSwitchPlacement>> SWITCH = registerPlacementModifierType("switch", () -> PlantopiaSwitchPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<PlantopiaConditionPlacement>> CONDITION = registerPlacementModifierType("condition", () -> PlantopiaConditionPlacement.CODEC);

    private static <P extends PlacementModifier> @NotNull RegistryObject<PlacementModifierType<P>> registerPlacementModifierType(String name, Supplier<Codec<P>> supplier) {
        return registerPlacementModifierType(plantopia(name), supplier);
    }

    private static <P extends PlacementModifier> @NotNull RegistryObject<PlacementModifierType<P>> registerPlacementModifierType(ResourceLocation identifier, @NotNull Supplier<Codec<P>> supplier) {
        return PlantopiaRegistries.PLACEMENT_MODIFIER_TYPE.register(identifier, () -> new PlacementModifierType<>() {
            @Override
            public @NotNull Codec<P> codec() {
                return supplier.get();
            }
        });
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.PLACEMENT_MODIFIER_TYPE, PlantopiaRegistries.PLACEMENT_MODIFIER_TYPE);
    }
}
