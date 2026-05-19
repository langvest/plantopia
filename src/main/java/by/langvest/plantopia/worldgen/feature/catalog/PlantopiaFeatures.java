package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureTypes;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaLimitedRandomPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaRadialPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaVegetationPatchConfiguration;
import by.langvest.toolkit.util.Catalog;
import by.langvest.toolkit.util.LocationLike;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaFeatures {
	public static final String SINGLE = "single";
	public static final String PIT = "pit";
	public static final String LAKE = "lake";
	public static final String PATCH = "patch";
	public static final String STALACTITE = "stalactite";
	public static final String STALAGMITE = "stalagmite";
	public static final String BONEMEAL = "bonemeal";
	public static final String VEGETATION = "vegetation";
	public static final String MOUNTAIN = "mountain";
	public static final String JUNGLE = "jungle";
	public static final String MARSH = "marsh";
	public static final String ANCHOR = "anchor";
	public static final String CAVE = "cave";
	public static final String HUGE = "huge";
	public static final String SEASONAL = "seasonal";
	public static final String FANCY = "fancy";
	public static final String CLUSTER = "cluster";
	public static final String SURFACE = "surface";
	public static final String LARGE = "large";
	public static final String WIDE = "wide";
	public static final String IN_WATER = "in_water";
	public static final String IN_SNOW = "in_snow";
	public static final String ON_SAND = "on_sand";

	public static final BlockPredicate ON_SAND_PREDICATE = BlockPredicate.allOf(
		BlockPredicate.ONLY_IN_AIR_PREDICATE,
		BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND)
	);

	public static final Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
		PlantopiaVegetationFeatures.DECLARATION,
		PlantopiaMiscOverworldFeatures.DECLARATION,
		PlantopiaCaveFeatures.DECLARATION,
		PlantopiaTreeFeatures.DECLARATION
	));

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		DECLARATION.forEach((key, declaration) -> {
			var configuredFeature = declaration.getConfiguredFeature(context);

			context.register(key, configuredFeature);
		});
	}

	protected static @NotNull ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, plantopia(name));
	}

	/* FEATURES ******************************************/

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> simpleBlock(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleBlockConfiguration> configFactory) {
		return configuredFeature(Feature.SIMPLE_BLOCK, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> simpleRandomSelector(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleRandomFeatureConfiguration> configFactory) {
		return configuredFeature(Feature.SIMPLE_RANDOM_SELECTOR, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> randomSelector(Function<BootstapContext<ConfiguredFeature<?, ?>>, RandomFeatureConfiguration> configFactory) {
		return configuredFeature(Feature.RANDOM_SELECTOR, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> tree(Function<BootstapContext<ConfiguredFeature<?, ?>>, TreeConfiguration> configFactory) {
		return configuredFeature(Feature.TREE, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> naturalBlockColumn(Function<BootstapContext<ConfiguredFeature<?, ?>>, BlockColumnConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.NATURAL_BLOCK_COLUMN, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> naturalBlock(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleBlockConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.NATURAL_BLOCK.get(), configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> randomPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, RandomPatchConfiguration> configFactory) {
		return configuredFeature(Feature.RANDOM_PATCH, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> vegetationPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaVegetationPatchConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.VEGETATION_PATCH, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> limitedRandomPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaLimitedRandomPatchConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.LIMITED_RANDOM_PATCH.get(), configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> radialPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.RADIAL_PATCH.get(), configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> pit(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaPitConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.PIT.get(), configFactory);
	}

	@Contract(pure = true)
	public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> lake(Function<BootstapContext<ConfiguredFeature<?, ?>>, LakeFeature.Configuration> configFactory) {
		return configuredFeature(Feature.LAKE, configFactory);
	}

	@Contract(pure = true)
	public static @NotNull <FC extends FeatureConfiguration, F extends Feature<FC>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(F feature, Function<BootstapContext<ConfiguredFeature<?, ?>>, FC> configFactory) {
		return context -> new ConfiguredFeature<>(feature, configFactory.apply(context));
	}

	@Contract(pure = true)
	public static @NotNull <FC extends FeatureConfiguration, F extends Feature<FC>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(Supplier<F> feature, Function<BootstapContext<ConfiguredFeature<?, ?>>, FC> configFactory) {
		return context -> new ConfiguredFeature<>(feature.get(), configFactory.apply(context));
	}

	@Contract(pure = true)
	public static @NotNull <F extends Feature<NoneFeatureConfiguration>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(F feature) {
		return context -> new ConfiguredFeature<>(feature, new NoneFeatureConfiguration());
	}

	@Contract(pure = true)
	public static @NotNull <F extends Feature<NoneFeatureConfiguration>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(Supplier<F> feature) {
		return context -> new ConfiguredFeature<>(feature.get(), new NoneFeatureConfiguration());
	}

	/* CONFIGS ******************************************/

	@Contract(pure = true)
	public static @NotNull SimpleBlockConfiguration simpleConfig(Block block) {
		return new SimpleBlockConfiguration(simpleProvider(block));
	}

	@Contract(pure = true)
	public static @NotNull SimpleBlockConfiguration weightedConfig(@NotNull Function<SimpleWeightedRandomList.Builder<BlockState>, SimpleWeightedRandomList.Builder<BlockState>> states) {
		return new SimpleBlockConfiguration(weightedProvider(states));
	}

	/* PROVIDERS ******************************************/

	@Contract(pure = true)
	public static @NotNull BlockStateProvider simpleProvider(Block block) {
		return BlockStateProvider.simple(block);
	}

	@Contract(pure = true)
	public static @NotNull BlockStateProvider simpleProvider(BlockState state) {
		return BlockStateProvider.simple(state);
	}

	@Contract(pure = true)
	public static @NotNull WeightedStateProvider weightedProvider(@NotNull Function<SimpleWeightedRandomList.Builder<BlockState>, SimpleWeightedRandomList.Builder<BlockState>> states) {
		return new WeightedStateProvider(states.apply(SimpleWeightedRandomList.builder()));
	}

	@Contract(pure = true)
	public static @NotNull WeightedListInt weightedListInt(@NotNull Function<SimpleWeightedRandomList.Builder<IntProvider>, SimpleWeightedRandomList.Builder<IntProvider>> values) {
		return new WeightedListInt(values.apply(SimpleWeightedRandomList.builder()).build());
	}

	/* CONTEXT *************************************************/

	public static @NotNull HolderGetter<ConfiguredFeature<?, ?>> lookupFeatures(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
		return context.lookup(Registries.CONFIGURED_FEATURE);
	}

	public static @NotNull HolderGetter<PlacedFeature> lookupPlacements(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
		return context.lookup(Registries.PLACED_FEATURE);
	}

	public static @NotNull HolderGetter<Biome> lookupBiomes(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
		return context.lookup(Registries.BIOME);
	}

	/* HELPER METHODS ******************************************/

	@Contract("_ -> new")
	public static @NotNull String singleNameOf(String name) {
		return compileNameFrom(SINGLE, name);
	}

	@Contract("_ -> new")
	public static @NotNull String patchNameOf(String name) {
		return compileNameFrom(PATCH, name);
	}

	@Contract("_ -> new")
	public static @NotNull String patchNameOf(Block block) {
		return compileNameFrom(PATCH, nameOf(block));
	}

	@Contract("_ -> new")
	public static @NotNull String singleNameOf(LocationLike locationLike) {
		return singleNameOf(nameOf(locationLike));
	}

	@Contract("_ -> new")
	public static @NotNull String patchNameOf(LocationLike locationLike) {
		return patchNameOf(nameOf(locationLike));
	}

	public static int calculateExponentialWeight(int step, double decay) {
		int maxWeight = 100;
		double weightDecrease = (maxWeight * Math.exp(decay * (step - 1))) - maxWeight;

		return (int) Math.max(1, maxWeight - weightDecrease);
	}
}
