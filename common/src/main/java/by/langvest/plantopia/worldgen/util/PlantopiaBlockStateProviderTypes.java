package by.langvest.plantopia.worldgen.util;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.util.stateprovider.PlantopiaTiltedLayeredBlockStateProvider;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockStateProviderTypes {
    public static final RegistryObject<BlockStateProviderType<?>> TILTED_LAYERED_PROVIDER = registerBlockStateProviderType("tilted_layered_provider", () -> new BlockStateProviderType<>(PlantopiaTiltedLayeredBlockStateProvider.CODEC));

    private static RegistryObject<BlockStateProviderType<?>> registerBlockStateProviderType(String name, Supplier<BlockStateProviderType<?>> supplier) {
        return registerBlockStateProviderType(plantopia(name), supplier);
    }

    private static RegistryObject<BlockStateProviderType<?>> registerBlockStateProviderType(ResourceLocation identifier, Supplier<BlockStateProviderType<?>> supplier) {
        return PlantopiaRegistries.BLOCK_STATE_PROVIDER_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.BLOCK_STATE_PROVIDER_TYPE, PlantopiaRegistries.BLOCK_STATE_PROVIDER_TYPE);
    }
}
