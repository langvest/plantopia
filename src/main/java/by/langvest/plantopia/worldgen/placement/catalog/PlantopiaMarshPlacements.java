package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class PlantopiaMarshPlacements extends PlantopiaVegetationPlacements {
	public static final Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

	public static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
		return DECLARATION.add(createKey(name), builder.build()).getKey();
	}

	/* MARSH PLACEMENTS ******************************************/

	public static final ResourceKey<PlacedFeature> PATCH_REED_MARSH = declarePlacement(
		compileNameFrom(PATCH_REED, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_REED)
			.modifiers(context -> List.of(
				CountPlacement.of(4),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				WATER_PLANT_RANGE_FILTER,
				BiomeFilter.biome(),
				BlockPredicateFilter.forPredicate(
					BlockPredicate.anyOf(
						BlockPredicate.matchesFluids(Fluids.WATER),
						BlockPredicate.matchesBlocks(Blocks.ICE)
					)
				)
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_AZURE_BLUET_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_AZURE_BLUET, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_AZURE_BLUET)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(6),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_MARSH = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_SUGAR_CANE, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(VegetationFeatures.PATCH_SUGAR_CANE)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(5.24F),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_GRASS_MARSH = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_GRASS, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(VegetationFeatures.PATCH_GRASS)
			.modifiers(context -> List.of(
				CountPlacement.of(12),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.addTag(PlantopiaBiomeTags.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN_MARSH = declarePlacement(
		compileNameFrom(VegetationFeatures.PATCH_PUMPKIN, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(VegetationFeatures.PATCH_PUMPKIN)
			.modifiers(context -> List.of(
				RarityFilter.onAverageOnceEvery(80),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_GIANT_GRASS_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_GIANT_GRASS, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_GIANT_GRASS)
			.modifiers(context -> List.of(
				CountPlacement.of(UniformInt.of(0, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.addTag(PlantopiaBiomeTags.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_SWEET_FLAG_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG)
			.modifiers(context -> {
				var bigNoiseConfig = PlantopiaNoiseConfig.of(0.046D, 534, 22);
				var smallNoiseConfig = PlantopiaNoiseConfig.of(0.022D, 43, 884);
				float bigNoiseLevel = -0.25F;
				float smallNoiseLevel = -0.1F;

				return List.of(
					PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 22),
					InSquarePlacement.spread(),
					PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.1F),
					PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.15F),
					PlacementUtils.HEIGHTMAP_TOP_SOLID,
					WATER_PLANT_RANGE_FILTER,
					BiomeFilter.biome(),
					BlockPredicateFilter.forPredicate(
						BlockPredicate.allOf(
							BlockPredicate.matchesFluids(Fluids.WATER),
							BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.DIRT)
						)
					)
				);
			})
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.MARSH)
			)
	);

	public static final ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_MARSH = declarePlacement(
		compileNameFrom(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB, MARSH),
		PlantopiaPlacementDeclaration.builder()
			.feature(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB)
			.modifiers(context -> List.of(
				PlantopiaRarityFilter.onAverageOnceEvery(4.42F),
				CountPlacement.of(UniformInt.of(1, 2)),
				InSquarePlacement.spread(),
				PlacementUtils.HEIGHTMAP_TOP_SOLID,
				PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
				BiomeFilter.biome()
			))
			.biomes(biomes -> biomes
				.add(PlantopiaOverworldBiomes.DEAD_MARSH)
			)
	);
}
