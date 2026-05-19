package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaRangeFilter;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaPlacements {
	protected static final String BONEMEAL = "bonemeal";
	protected static final String MOUNTAIN = "mountain";
	protected static final String SURFACE = "surface";
	protected static final String RARE = "rare";
	protected static final String BONUS = "bonus";
	protected static final String SNOWY = "snowy";
	protected static final String SWAMP = "swamp";
	protected static final String OCEAN = "ocean";
	protected static final String CAVE = "cave";
	protected static final String MARSH = "marsh";
	protected static final String CHECKED = "checked";

	public static final PlantopiaRangeFilter WATER_PLANT_RANGE_FILTER = PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel(-1));

	public static final EnvironmentScanPlacement WATER_PLANT_FIND_WATER = EnvironmentScanPlacement.scanningFor(
		Direction.DOWN,
		BlockPredicate.matchesFluids(BlockPos.ZERO, Fluids.WATER),
		4
	);

	public static final Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
		PlantopiaVegetationPlacements.DECLARATION,
		PlantopiaMiscOverworldPlacements.DECLARATION,
		PlantopiaCavePlacements.DECLARATION,
		PlantopiaMarshPlacements.DECLARATION,
		PlantopiaSeasonalPlacements.DECLARATION
	));

	public static void bootstrap(BootstapContext<PlacedFeature> context) {
		DECLARATION.forEach((key, declaration) -> {
			var placedFeature = declaration.getPlacedFeature(context);

			context.register(key, placedFeature);
		});
	}

	protected static @NotNull ResourceKey<PlacedFeature> createKey(String name) {
		return ResourceKey.create(Registries.PLACED_FEATURE, plantopia(name));
	}

	/* CONTEXT *************************************************/

	protected static @NotNull HolderGetter<ConfiguredFeature<?, ?>> lookupFeatures(@NotNull BootstapContext<PlacedFeature> context) {
		return context.lookup(Registries.CONFIGURED_FEATURE);
	}

	protected static @NotNull HolderGetter<PlacedFeature> lookupPlacements(@NotNull BootstapContext<PlacedFeature> context) {
		return context.lookup(Registries.PLACED_FEATURE);
	}

	protected static @NotNull HolderGetter<Biome> lookupBiomes(@NotNull BootstapContext<PlacedFeature> context) {
		return context.lookup(Registries.BIOME);
	}

	@SafeVarargs
    protected static @NotNull HolderSet<Biome> directBiomes(@NotNull BootstapContext<PlacedFeature> context, ResourceKey<Biome> ...biomeKeys) {
		var biomes = lookupBiomes(context);
		return HolderSet.direct(Arrays.stream(biomeKeys).map(biomes::getOrThrow).toList());
	}

	/* HELPER METHODS ******************************************/

	protected static void addMountainBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		tagSet
			.add(Biomes.PLAINS, Biomes.MEADOW)
			.add(Biomes.STONY_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS);
	}

	protected static void addVanillaOldGrowthBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		tagSet
			.add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST);
	}

	protected static void addVanillaSwampBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
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
