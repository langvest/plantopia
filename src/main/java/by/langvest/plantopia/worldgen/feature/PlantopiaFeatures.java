package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.worldgen.feature.config.PlantopiaLimitedRandomPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaRadialPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaVegetationPatchConfiguration;
import by.langvest.toolkit.util.LocationLike;
import com.google.common.collect.Maps;
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
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaFeatures {
	protected static final String SINGLE = "single";
	protected static final String PIT = "pit";
	protected static final String PATCH = "patch";
	protected static final String BONEMEAL = "bonemeal";
	protected static final String VEGETATION = "vegetation";
	protected static final String MOUNTAIN = "mountain";
	protected static final String JUNGLE = "jungle";
	protected static final String ANCHOR = "anchor";
	protected static final String CAVE = "cave";
	protected static final String HUGE = "huge";
	protected static final String WIDE = "wide";
	protected static final String IN_WATER = "in_water";
	protected static final String IN_SNOW = "in_snow";
	protected static final String ON_SAND = "on_sand";

	protected static final BlockPredicate ON_SAND_PREDICATE = BlockPredicate.allOf(
		BlockPredicate.ONLY_IN_AIR_PREDICATE,
		BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND)
	);

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		getDeclarations().forEach((key, declaration) -> {
			var configuredFeature = declaration.getConfiguredFeature(context);

			context.register(key, configuredFeature);
		});
	}

	public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
		Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> result = Maps.newHashMap();

		result.putAll(PlantopiaVegetationFeatures.getDeclarations());
		result.putAll(PlantopiaMiscOverworldFeatures.getDeclarations());
		result.putAll(PlantopiaCaveFeatures.getDeclarations());
		result.putAll(PlantopiaTreeFeatures.getDeclarations());

		return result;
	}

	protected static @NotNull ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, plantopia(name));
	}

	/* FEATURES ******************************************/

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> simpleBlock(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleBlockConfiguration> configFactory) {
		return configuredFeature(Feature.SIMPLE_BLOCK, configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> simpleRandomSelector(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleRandomFeatureConfiguration> configFactory) {
		return configuredFeature(Feature.SIMPLE_RANDOM_SELECTOR, configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> naturalBlockColumn(Function<BootstapContext<ConfiguredFeature<?, ?>>, BlockColumnConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.NATURAL_BLOCK_COLUMN, configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> naturalBlock(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleBlockConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.NATURAL_BLOCK.get(), configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> randomPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, RandomPatchConfiguration> configFactory) {
		return configuredFeature(Feature.RANDOM_PATCH, configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> vegetationPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaVegetationPatchConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.VEGETATION_PATCH, configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> limitedRandomPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaLimitedRandomPatchConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.LIMITED_RANDOM_PATCH.get(), configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> radialPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.RADIAL_PATCH.get(), configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> pit(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaPitConfiguration> configFactory) {
		return configuredFeature(PlantopiaFeatureTypes.PIT.get(), configFactory);
	}

	@Contract(pure = true)
	protected static @NotNull <FC extends FeatureConfiguration, F extends Feature<FC>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(F feature, Function<BootstapContext<ConfiguredFeature<?, ?>>, FC> configFactory) {
		return context -> new ConfiguredFeature<>(feature, configFactory.apply(context));
	}

	@Contract(pure = true)
	protected static @NotNull <FC extends FeatureConfiguration, F extends Feature<FC>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(Supplier<F> feature, Function<BootstapContext<ConfiguredFeature<?, ?>>, FC> configFactory) {
		return context -> new ConfiguredFeature<>(feature.get(), configFactory.apply(context));
	}

	@Contract(pure = true)
	protected static @NotNull <F extends Feature<NoneFeatureConfiguration>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(F feature) {
		return context -> new ConfiguredFeature<>(feature, new NoneFeatureConfiguration());
	}

	@Contract(pure = true)
	protected static @NotNull <F extends Feature<NoneFeatureConfiguration>> Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> configuredFeature(Supplier<F> feature) {
		return context -> new ConfiguredFeature<>(feature.get(), new NoneFeatureConfiguration());
	}

	/* CONFIGS ******************************************/

	@Contract(pure = true)
	protected static @NotNull SimpleBlockConfiguration simpleConfig(Block block) {
		return new SimpleBlockConfiguration(simpleProvider(block));
	}

	@Contract(pure = true)
	protected static @NotNull SimpleBlockConfiguration weightedConfig(@NotNull Function<SimpleWeightedRandomList.Builder<BlockState>, SimpleWeightedRandomList.Builder<BlockState>> states) {
		return new SimpleBlockConfiguration(weightedProvider(states));
	}

	/* PROVIDERS ******************************************/

	@Contract(pure = true)
	protected static @NotNull BlockStateProvider simpleProvider(Block block) {
		return BlockStateProvider.simple(block);
	}

	@Contract(pure = true)
	protected static @NotNull BlockStateProvider simpleProvider(BlockState state) {
		return BlockStateProvider.simple(state);
	}

	@Contract(pure = true)
	protected static @NotNull WeightedStateProvider weightedProvider(@NotNull Function<SimpleWeightedRandomList.Builder<BlockState>, SimpleWeightedRandomList.Builder<BlockState>> states) {
		return new WeightedStateProvider(states.apply(SimpleWeightedRandomList.builder()));
	}

	@Contract(pure = true)
	protected static @NotNull WeightedListInt weightedListInt(@NotNull Function<SimpleWeightedRandomList.Builder<IntProvider>, SimpleWeightedRandomList.Builder<IntProvider>> values) {
		return new WeightedListInt(values.apply(SimpleWeightedRandomList.builder()).build());
	}

	/* CONTEXT *************************************************/

	protected static @NotNull HolderGetter<ConfiguredFeature<?, ?>> lookupFeatures(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
		return context.lookup(Registries.CONFIGURED_FEATURE);
	}

	protected static @NotNull HolderGetter<PlacedFeature> lookupPlacements(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
		return context.lookup(Registries.PLACED_FEATURE);
	}

	protected static @NotNull HolderGetter<Biome> lookupBiomes(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
		return context.lookup(Registries.BIOME);
	}

	/* HELPER METHODS ******************************************/

	@Contract("_ -> new")
	protected static @NotNull String singleNameOf(String name) {
		return compileNameFrom(SINGLE, name);
	}

	@Contract("_ -> new")
	protected static @NotNull String patchNameOf(String name) {
		return compileNameFrom(PATCH, name);
	}

	@Contract("_ -> new")
	protected static @NotNull String patchNameOf(Block block) {
		return compileNameFrom(PATCH, nameOf(block));
	}

	@Contract("_ -> new")
	protected static @NotNull String singleNameOf(LocationLike locationLike) {
		return singleNameOf(nameOf(locationLike));
	}

	@Contract("_ -> new")
	protected static @NotNull String patchNameOf(LocationLike locationLike) {
		return patchNameOf(nameOf(locationLike));
	}

	protected static int calculateExponentialWeight(int step, double decay) {
		int maxWeight = 100;
		double weightDecrease = (maxWeight * Math.exp(decay * (step - 1))) - maxWeight;

		return (int) Math.max(1, maxWeight - weightDecrease);
	}
}
