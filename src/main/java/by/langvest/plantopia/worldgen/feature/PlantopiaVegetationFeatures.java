package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCobblestoneShardBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatures.createKey;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatures.register;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class PlantopiaVegetationFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_HOGWEED = createKey("single_hogweed");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TINY_CACTUS = createKey("patch_tiny_cactus");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CLOVER = createKey("patch_clover");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREWEED = createKey("patch_fireweed");
	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_DIAMOND_BLOCK = createKey("single_diamond_block");

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_COBBLESTONE_SHARD = createKey("patch_cobblestone_shard");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_COBBLESTONE_SHARD_IN_WATER = createKey("patch_cobblestone_shard_in_water");

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOSSY_COBBLESTONE_SHARD = createKey("patch_mossy_cobblestone_shard");
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER = createKey("patch_mossy_cobblestone_shard_in_water");

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		register(context, SINGLE_HOGWEED, PlantopiaFeatureTypes.HOGWEED.get());

		register(
			context,
			PATCH_FIREWEED,
			Feature.RANDOM_PATCH,
			new RandomPatchConfiguration(64, 6, 3, PlacementUtils.filtered(
				Feature.SIMPLE_BLOCK,
				new SimpleBlockConfiguration(new WeightedStateProvider(
					SimpleWeightedRandomList.<BlockState>builder()
						.add(PlantopiaBlocks.FIREWEED.get().defaultBlockState(), 10)
						.add(Blocks.TALL_GRASS.defaultBlockState(), 1)
				)),
				BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS)
			))
		);

		register(
			context,
			SINGLE_DIAMOND_BLOCK,
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DIAMOND_BLOCK))
		);

		SimpleWeightedRandomList.Builder<BlockState> builder3 = SimpleWeightedRandomList.builder();

		builder3.add(PlantopiaBlocks.TINY_CACTUS.get().defaultBlockState(), 5);
		builder3.add(PlantopiaBlocks.FLOWERING_TINY_CACTUS.get().defaultBlockState(), 2);

		register(context, PATCH_TINY_CACTUS, Feature.RANDOM_PATCH, new RandomPatchConfiguration(2, 2, 2, PlacementUtils.onlyWhenEmpty(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(new WeightedStateProvider(builder3))
		)));

		SimpleWeightedRandomList.Builder<BlockState> builder1 = SimpleWeightedRandomList.builder();
		SimpleWeightedRandomList.Builder<BlockState> builder4 = SimpleWeightedRandomList.builder();
		SimpleWeightedRandomList.Builder<BlockState> builder2 = SimpleWeightedRandomList.builder();
		SimpleWeightedRandomList.Builder<BlockState> builder5 = SimpleWeightedRandomList.builder();

		for(int i = PlantopiaCobblestoneShardBlock.MIN_SHARDS; i <= PlantopiaCobblestoneShardBlock.MAX_SHARDS; i++) {
			int weight = calculateExponential(i, 0.215);

			builder1.add(PlantopiaBlocks.COBBLESTONE_SHARD.get().defaultBlockState().setValue(PlantopiaCobblestoneShardBlock.AMOUNT, i), weight);
			builder4.add(PlantopiaBlocks.COBBLESTONE_SHARD.get().defaultBlockState().setValue(PlantopiaCobblestoneShardBlock.AMOUNT, i).setValue(PlantopiaCobblestoneShardBlock.WATERLOGGED, true), weight);
			builder2.add(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get().defaultBlockState().setValue(PlantopiaCobblestoneShardBlock.AMOUNT, i), weight);
			builder5.add(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get().defaultBlockState().setValue(PlantopiaCobblestoneShardBlock.AMOUNT, i).setValue(PlantopiaCobblestoneShardBlock.WATERLOGGED, true), weight);
		}

		register(context, PATCH_COBBLESTONE_SHARD, Feature.RANDOM_PATCH, new RandomPatchConfiguration(4, 1, 1, PlacementUtils.filtered(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(new WeightedStateProvider(builder1)),
			BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.solid(BlockPos.ZERO.below())
			)
		)));

		register(context, PATCH_COBBLESTONE_SHARD_IN_WATER, Feature.RANDOM_PATCH, new RandomPatchConfiguration(4, 1, 1, PlacementUtils.filtered(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(new WeightedStateProvider(builder4)),
			BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.WATER),
				BlockPredicate.solid(BlockPos.ZERO.below())
			)
		)));

		register(context, PATCH_MOSSY_COBBLESTONE_SHARD, Feature.RANDOM_PATCH, new RandomPatchConfiguration(4, 1, 1, PlacementUtils.filtered(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(new WeightedStateProvider(builder2)),
			BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.AIR),
				BlockPredicate.solid(BlockPos.ZERO.below())
			)
		)));

		register(context, PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER, Feature.RANDOM_PATCH, new RandomPatchConfiguration(4, 1, 1, PlacementUtils.filtered(
			Feature.SIMPLE_BLOCK,
			new SimpleBlockConfiguration(new WeightedStateProvider(builder5)),
			BlockPredicate.allOf(
				BlockPredicate.matchesBlocks(Blocks.WATER),
				BlockPredicate.solid(BlockPos.ZERO.below())
			)
		)));
	}

	@Contract("_, _ -> new")
	private static @NotNull RandomPatchConfiguration grassPatch(BlockStateProvider blockStateProvider, int pTries) {
		return FeatureUtils.simpleRandomPatchConfiguration(pTries, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(blockStateProvider)));
	}

	public static int calculateExponential(int t, double k) {
		int maxWeight = 100;
		double dec = (maxWeight * Math.exp(k * (t - 1))) - maxWeight;

		return (int) Math.max(1, maxWeight - dec);
	}
}
