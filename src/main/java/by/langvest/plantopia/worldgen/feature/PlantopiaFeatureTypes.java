package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.worldgen.feature.special.PlantopiaHogweedFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PlantopiaFeatureTypes {
	private static final DeferredRegister<Feature<?>> FEATURE_REGISTER = DeferredRegister.create(ForgeRegistries.FEATURES, Plantopia.MOD_ID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> HOGWEED = register("hogweed", () -> new PlantopiaHogweedFeature(NoneFeatureConfiguration.CODEC));

	private static <C extends FeatureConfiguration, F extends Feature<C>> RegistryObject<F> register(String name, Supplier<F> supplier) {
		return FEATURE_REGISTER.register(name, supplier);
	}

	public static void setup(IEventBus bus) {
		FEATURE_REGISTER.register(bus);
	}
}
