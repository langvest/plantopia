package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaHeightRangeFilter;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseCountPlacement;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseFilter;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaNoiseConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacements.createKey;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacements.register;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class PlantopiaVegetationPlacements {
	public static final ResourceKey<PlacedFeature> HOGWEED_BONEMEAL = createKey("hogweed_bonemeal");
	public static final ResourceKey<PlacedFeature> PATCH_HOGWEED = createKey("patch_hogweed");
	public static final ResourceKey<PlacedFeature> PATCH_FIREWEED_MOUNTAINS = createKey("patch_fireweed_mountain");
	public static final ResourceKey<PlacedFeature> HOGWEED_INFESTED_GRASS_BLOCK = createKey("hogweed_infested_grass_block");
	public static final ResourceKey<PlacedFeature> PATCH_TINY_CACTUS = createKey("patch_tiny_cactus");
	public static final ResourceKey<PlacedFeature> PATCH_COBBLESTONE_SHARD = createKey("patch_cobblestone_shard");
	public static final ResourceKey<PlacedFeature> PATCH_COBBLESTONE_SHARD_IN_WATER = createKey("patch_cobblestone_shard_in_water");
	public static final ResourceKey<PlacedFeature> PATCH_MOSSY_COBBLESTONE_SHARD = createKey("patch_mossy_cobblestone_shard");
	public static final ResourceKey<PlacedFeature> PATCH_MOSSY_COBBLESTONE_SHARD_2 = createKey("patch_mossy_cobblestone_shard_2");
	public static final ResourceKey<PlacedFeature> PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER = createKey("patch_mossy_cobblestone_shard_in_water");

	public static void bootstrap(@NotNull BootstapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

		register(context, HOGWEED_BONEMEAL, features.getOrThrow(PlantopiaVegetationFeatures.SINGLE_HOGWEED), RarityFilter.onAverageOnceEvery(20), PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get()));
		register(context, PATCH_HOGWEED, features.getOrThrow(PlantopiaVegetationFeatures.SINGLE_HOGWEED), NoiseBasedCountPlacement.of(-15, 80.0D, 0.8D), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome(), PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get()));

//		var fireweedSmallNoiseConfig = PlantopiaNoiseConfig.of(0.552D, 541, 432);
//		float fireweedSmallNoiseLevel = -0.88F;
//
//		register(
//			context,
//			PATCH_FIREWEED_MOUNTAINS,
//			features.getOrThrow(PlantopiaVegetationFeatures.PATCH_FIREWEED),
//			CountPlacement.of(22),
//			InSquarePlacement.spread(),
//			PlantopiaNoiseFilter.belowLevel(fireweedSmallNoiseConfig, fireweedSmallNoiseLevel, 0.1F),
//			PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
//			PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(90), VerticalAnchor.TOP),
//			BiomeFilter.biome(),
//			PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
//		);

		var fireweedBigNoiseConfig = PlantopiaNoiseConfig.of(0.614D, 501, 402);
		float fireweedBigNoiseLevel = -0.815F;

		var fireweedSmallNoiseConfig = PlantopiaNoiseConfig.of(0.102D, 274, 148);
		float fireweedSmallNoiseLevel = -0.1F;

		register(
			context,
			PATCH_FIREWEED_MOUNTAINS,
			features.getOrThrow(PlantopiaVegetationFeatures.PATCH_FIREWEED),
			PlantopiaNoiseCountPlacement.belowLevel(fireweedBigNoiseConfig, fireweedBigNoiseLevel, 26),
			InSquarePlacement.spread(),
			PlantopiaNoiseFilter.belowLevel(fireweedBigNoiseConfig, fireweedBigNoiseLevel, 0.1F),
			PlantopiaNoiseFilter.aboveLevel(fireweedSmallNoiseConfig, fireweedSmallNoiseLevel, 0.15F),
			PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
			PlantopiaHeightRangeFilter.uniform(VerticalAnchor.absolute(86), VerticalAnchor.TOP),
			BiomeFilter.biome(),
			PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.FIREWEED.get())
		);

		register(context, HOGWEED_INFESTED_GRASS_BLOCK, features.getOrThrow(PlantopiaVegetationFeatures.SINGLE_HOGWEED), BlockPredicateFilter.forPredicate(
			BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get())
			)
		));

		register(context, PATCH_TINY_CACTUS, features.getOrThrow(PlantopiaVegetationFeatures.PATCH_TINY_CACTUS), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

		register(context, PATCH_COBBLESTONE_SHARD, features.getOrThrow(PlantopiaVegetationFeatures.PATCH_COBBLESTONE_SHARD), CountPlacement.of(40), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
		register(context, PATCH_COBBLESTONE_SHARD_IN_WATER, features.getOrThrow(PlantopiaVegetationFeatures.PATCH_COBBLESTONE_SHARD_IN_WATER), CountPlacement.of(40), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
		register(context, PATCH_MOSSY_COBBLESTONE_SHARD, features.getOrThrow(PlantopiaVegetationFeatures.PATCH_MOSSY_COBBLESTONE_SHARD), CountPlacement.of(40), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
		register(context, PATCH_MOSSY_COBBLESTONE_SHARD_2, features.getOrThrow(PlantopiaVegetationFeatures.PATCH_MOSSY_COBBLESTONE_SHARD), CountPlacement.of(40), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
		register(context, PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER, features.getOrThrow(PlantopiaVegetationFeatures.PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER), CountPlacement.of(40), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
	}
}
