package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCobblestoneShardBlock;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.features.VegetationFeatures
 */
public class PlantopiaVegetationFeatures extends PlantopiaFeatures {
	protected static final BlockPredicate WATER_PlANT_PREDICATE = BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GRASS, Blocks.SEAGRASS);
	protected static final BlockPredicate ON_SAND_PREDICATE = BlockPredicate.allOf(
		BlockPredicate.ONLY_IN_AIR_PREDICATE,
		BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND)
	);

	private static final Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> declarations = Maps.newHashMap();

	public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
		return declarations;
	}

	private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareConfiguredFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
		var key = createKey(name);
		declarations.put(key, builder.build());
		return key;
	}

	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_HOGWEED = declareConfiguredFeature(
		singleNameOf(PlantopiaBlocks.HOGWEED),
		PlantopiaFeatureDeclaration.builder()
			.feature(naturalBlock(context ->
				simpleConfig(PlantopiaBlocks.HOGWEED.get())
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_DIAMOND_BLOCK = declareConfiguredFeature(
		singleNameOf("diamond_block"),
		PlantopiaFeatureDeclaration.builder()
			.feature(simpleBlock(context ->
				simpleConfig(Blocks.DIAMOND_BLOCK)
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_TINY_CACTUS_ON_SAND = declareConfiguredFeature(
		compileNameFrom(PATCH, PlantopiaBlocks.TINY_CACTUS, ON_SAND),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(context ->
				new RandomPatchConfiguration(8, 6, 2, PlacementUtils.filtered(
					PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
					weightedConfig(states -> states
						.add(PlantopiaBlocks.TINY_CACTUS.get().defaultBlockState(), 5)
						.add(PlantopiaBlocks.FLOWERING_TINY_CACTUS.get().defaultBlockState(), 2)
					),
					ON_SAND_PREDICATE
				))
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREWEED = declareConfiguredFeature(
		patchNameOf(PlantopiaBlocks.FIREWEED),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(context ->
				new RandomPatchConfiguration(64, 6, 3, PlacementUtils.filtered(
					PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
					weightedConfig(states -> states
						.add(PlantopiaBlocks.FIREWEED.get().defaultBlockState(), 10)
						.add(Blocks.TALL_GRASS.defaultBlockState(), 1)
					),
					BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS)
				))
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_REEDS = declareConfiguredFeature(
		patchNameOf(PlantopiaBlocks.REEDS),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(context ->
				new RandomPatchConfiguration(22, 3, 1, PlacementUtils.filtered(
					PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
					simpleConfig(PlantopiaBlocks.REEDS.get()),
					WATER_PlANT_PREDICATE
				))
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_CATTAIL = declareConfiguredFeature(
		patchNameOf(PlantopiaBlocks.CATTAIL),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(context ->
				new RandomPatchConfiguration(94, 6, 1, PlacementUtils.filtered(
					PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
					simpleConfig(PlantopiaBlocks.CATTAIL.get()),
					BlockPredicate.allOf(
						WATER_PlANT_PREDICATE,
						BlockPredicate.not(
							BlockPredicate.matchesTag(BlockTags.ICE)
						)
					)
				))
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DUNE_GRASS = declareConfiguredFeature(
		patchNameOf(PlantopiaBlocks.DUNE_GRASS),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(context ->
				new RandomPatchConfiguration(64, 3, 2, PlacementUtils.filtered(
					PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
					weightedConfig(states -> states
						.add(PlantopiaBlocks.DUNE_GRASS.get().defaultBlockState(), 5)
						.add(PlantopiaBlocks.TALL_DUNE_GRASS.get().defaultBlockState(), 2)
					),
					ON_SAND_PREDICATE
				))
			))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_COBBLESTONE_SHARD = declareConfiguredFeature(
		patchNameOf(PlantopiaBlocks.COBBLESTONE_SHARD),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(getCobblestoneShardConfig(PlantopiaBlocks.COBBLESTONE_SHARD, () -> Blocks.AIR)))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOSSY_COBBLESTONE_SHARD = declareConfiguredFeature(
		patchNameOf(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(getCobblestoneShardConfig(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD, () -> Blocks.AIR)))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_COBBLESTONE_SHARD_IN_WATER = declareConfiguredFeature(
		compileNameFrom(PATCH, PlantopiaBlocks.COBBLESTONE_SHARD, IN_WATER),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(getCobblestoneShardConfig(PlantopiaBlocks.COBBLESTONE_SHARD, () -> Blocks.WATER)))
	);

	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER = declareConfiguredFeature(
		compileNameFrom(PATCH, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD, IN_WATER),
		PlantopiaFeatureDeclaration.builder()
			.feature(randomPatch(getCobblestoneShardConfig(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD, () -> Blocks.WATER)))
	);

	/* HELPER METHODS ******************************************/

	@Contract(pure = true)
	private static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, RandomPatchConfiguration> getCobblestoneShardConfig(Supplier<Block> cobblestoneShardBlock, Supplier<Block> environmentalBlock) {
		return context -> {
			boolean isWaterlogged = environmentalBlock.get() == Blocks.WATER;

			return new RandomPatchConfiguration(4, 1, 1, PlacementUtils.filtered(
				PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
				weightedConfig(states -> {
					for(int i = PlantopiaCobblestoneShardBlock.MIN_SHARDS; i <= PlantopiaCobblestoneShardBlock.MAX_SHARDS; i++) {
						int weight = calculateExponential(i, 0.215);

						var state = cobblestoneShardBlock.get().defaultBlockState()
							.setValue(PlantopiaCobblestoneShardBlock.AMOUNT, i)
							.setValue(PlantopiaCobblestoneShardBlock.WATERLOGGED, isWaterlogged);

						states.add(state, weight);
					}

					return states;
				}),
				BlockPredicate.matchesBlocks(environmentalBlock.get())
			));
		};
	}
}
