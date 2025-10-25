package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.worldgen.feature.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaHeightRangeFilter;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseCountPlacement;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseFilter;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class PlantopiaVegetationPlacements extends PlantopiaPlacements {
	private static final Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> declarations = Maps.newHashMap();

	public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
		return declarations;
	}

	private static @NotNull ResourceKey<PlacedFeature> declarePlacedFeature(String name, PlantopiaPlacedFeatureDeclaration.@NotNull Builder builder) {
		var key = createKey(name);
		declarations.put(key, builder.build());
		return key;
	}

	public static final ResourceKey<PlacedFeature> HOGWEED_BONEMEAL = declarePlacedFeature(
		compileNameFrom(PlantopiaBlocks.HOGWEED, BONEMEAL),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SINGLE_HOGWEED)
			.modifiers(context -> List.of(
				RarityFilter.onAverageOnceEvery(20),
				PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get())
			))
	);

	public static final ResourceKey<PlacedFeature> HOGWEED_INFESTED_GRASS_BLOCK = declarePlacedFeature(
		compileNameFrom(PlantopiaBlocks.HOGWEED, PlantopiaBlocks.INFESTED_GRASS_BLOCK),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.SINGLE_HOGWEED)
			.modifiers(context -> List.of(
				BlockPredicateFilter.forPredicate(
					BlockPredicate.allOf(
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
						BlockPredicate.matchesBlocks(BlockPos.ZERO.below().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get())
					)
				)
			))
	);

	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_FIREWEED, MOUNTAIN),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FIREWEED)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(0.614D, 501, 402);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.102D, 274, 148);
				float bigNoiseLevel = -0.815F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.belowLevel(bigNoiseConfig, bigNoiseLevel, 25),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.belowLevel(bigNoiseConfig, bigNoiseLevel, 0.1F),
					PlantopiaNoiseFilter.aboveLevel(smallNoiseConfig, smallNoiseLevel, 0.15F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(86), VerticalAnchor.TOP),
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
				);
			})
			.biomes(tagSet -> addMountainBiomes(tagSet)
				.add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAIN_2 = declarePlacedFeature(
		compileNameFrom(PATCH_FIREWEED_MOUNTAIN, 2),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_FIREWEED)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(2.826D, 528, 811);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.138D, 332, 643);
				float bigNoiseLevel = -0.44F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.belowLevel(bigNoiseConfig, bigNoiseLevel, 17),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.belowLevel(bigNoiseConfig, bigNoiseLevel, 0.18F),
					PlantopiaNoiseFilter.aboveLevel(smallNoiseConfig, smallNoiseLevel, 0.12F),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(122), VerticalAnchor.TOP),
					BiomeFilter.biome(),
					PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
				);
			})
			.biomes(PlantopiaVegetationPlacements::addMountainBiomes)
	);

	public static final ResourceKey<PlacedFeature> PATCH_TINY_CACTUS = declarePlacedFeature(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS_ON_SAND),
		PlantopiaPlacedFeatureDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS_ON_SAND)
			.modifiers(context -> List.of(
				RarityFilter.onAverageOnceEvery(4),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(tagSet -> tagSet
				.add(Biomes.DESERT, Biomes.BADLANDS, Biomes.ERODED_BADLANDS)
			)
	);

	/* HELPER METHODS ******************************************/

	protected static PlantopiaTagSet<Biome> addMountainBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
		return tagSet
			.add(Biomes.PLAINS, Biomes.MEADOW)
			.add(Biomes.STONY_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS);
	}
}
