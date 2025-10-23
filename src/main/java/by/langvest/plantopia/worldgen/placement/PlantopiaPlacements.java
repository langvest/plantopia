package by.langvest.plantopia.worldgen.placement;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaPlacements {
	protected static final String BONEMEAL = "bonemeal";
	protected static final String MOUNTAIN = "mountain";
	protected static final String UNDERGROUND = "underground";
	protected static final String UNDERWATER = "underwater";

	public static void bootstrap(BootstapContext<PlacedFeature> context) {
		getDeclarations().forEach((key, declaration) -> {
			var placedFeature = declaration.getPlacedFeature(context);

			context.register(key, placedFeature);
		});
	}

	public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
		Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> result = Maps.newHashMap();

		result.putAll(PlantopiaVegetationPlacements.getDeclarations());

		return result;
	}

	protected static @NotNull ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, plantopia(name));
	}
}
