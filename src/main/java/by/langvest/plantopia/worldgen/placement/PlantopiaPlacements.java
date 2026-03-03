package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaPlacements {
	protected static final String BONEMEAL = "bonemeal";
	protected static final String MOUNTAIN = "mountain";
	protected static final String BONUS = "bonus";
	protected static final String SNOWY = "snowy";
	protected static final String SWAMP = "swamp";
	protected static final String OCEAN = "ocean";
	protected static final String CAVE = "cave";

	public static void bootstrap(BootstapContext<PlacedFeature> context) {
		getDeclarations().forEach((key, declaration) -> {
			var placedFeature = declaration.getPlacedFeature(context);

			context.register(key, placedFeature);
		});
	}

	public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
		Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> result = Maps.newHashMap();

		result.putAll(PlantopiaVegetationPlacements.getDeclarations());
		result.putAll(PlantopiaMiscOverworldPlacements.getDeclarations());

		return result;
	}

	protected static @NotNull ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, plantopia(name));
	}

	/* HELPER METHODS ******************************************/

	protected static void addMountainBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		tagSet
			.add(Biomes.PLAINS, Biomes.MEADOW)
			.add(Biomes.STONY_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS);
	}

	protected static void addOldGrowthBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		tagSet
			.add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST);
	}

	protected static void addSwampBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		tagSet
			.add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
	}

	protected static void addCascadesBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		tagSet
			.addOptional(cascades("autumnal_forest"))
			.addOptional(cascades("rainforest"))
			.addOptional(cascades("seasonal_forest"))
			.addOptional(cascades("temperate_rainforest"));
	}
}
