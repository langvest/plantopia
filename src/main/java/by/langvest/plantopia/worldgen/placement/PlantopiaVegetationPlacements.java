package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.PlantopiaVegetationFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacements.createKey;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacements.register;
import static net.minecraft.data.worldgen.placement.PlacementUtils.filteredByBlockSurvival;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public class PlantopiaVegetationPlacements {
	public static final ResourceKey<PlacedFeature> HOGWEED_BONEMEAL = createKey("hogweed_bonemeal");
	public static final ResourceKey<PlacedFeature> HOGWEED_INFESTED_GRASS_BLOCK = createKey("hogweed_infested_grass_block");

	public static void bootstrap(@NotNull BootstapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

		register(context, HOGWEED_BONEMEAL, features.getOrThrow(PlantopiaVegetationFeatures.SINGLE_HOGWEED), filteredByBlockSurvival(PlantopiaBlocks.HOGWEED.get()));

		register(context, HOGWEED_INFESTED_GRASS_BLOCK, features.getOrThrow(PlantopiaVegetationFeatures.SINGLE_HOGWEED), BlockPredicateFilter.forPredicate(
			BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below().north().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get()),
				BlockPredicate.matchesBlocks(BlockPos.ZERO.below().east(), PlantopiaBlocks.INFESTED_GRASS_BLOCK.get())
			)
		));
	}
}
