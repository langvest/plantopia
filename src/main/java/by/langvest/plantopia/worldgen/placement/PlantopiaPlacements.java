package by.langvest.plantopia.worldgen.placement;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaPlacements {
	public static void bootstrap(@NotNull BootstapContext<PlacedFeature> context) {
		PlantopiaVegetationPlacements.bootstrap(context);
	}

	protected static @NotNull ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, plantopiaLocationFrom(name));
	}

	protected static void register(@NotNull BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, PlacementModifier... placements) {
		context.register(key, new PlacedFeature(configuration, List.of(placements)));
	}
}
