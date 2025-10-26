package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.toolkit.registry.RegistryObject;
import by.langvest.toolkit.util.LocationLike;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;

public class PlantopiaFeatures {
	protected static final String SINGLE = "single";
	protected static final String PATCH = "patch";
	protected static final String IN_WATER = "in_water";
	protected static final String IN_SNOW = "in_snow";
	protected static final String ON_SAND = "on_sand";

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		getDeclarations().forEach((key, declaration) -> {
			var configuredFeature = declaration.getConfiguredFeature(context);

			context.register(key, configuredFeature);
		});
	}

	public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
		Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> result = Maps.newHashMap();

		result.putAll(PlantopiaVegetationFeatures.getDeclarations());

		return result;
	}

	protected static @NotNull ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, plantopia(name));
	}

	/* FEATURES ******************************************/

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> simpleBlock(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleBlockConfiguration> configFactory) {
		return context -> new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, configFactory.apply(context));
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> naturalBlock(Function<BootstapContext<ConfiguredFeature<?, ?>>, SimpleBlockConfiguration> configFactory) {
		return context -> new ConfiguredFeature<>(PlantopiaFeatureTypes.NATURAL_BLOCK.get(), configFactory.apply(context));
	}

	@Contract(pure = true)
	protected static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> randomPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, RandomPatchConfiguration> configFactory) {
		return context -> new ConfiguredFeature<>(Feature.RANDOM_PATCH, configFactory.apply(context));
	}

	/* CONFIGS ******************************************/

	@Contract(pure = true)
	protected static @NotNull SimpleBlockConfiguration simpleConfig(Block block) {
		return new SimpleBlockConfiguration(BlockStateProvider.simple(block));
	}

	@Contract(pure = true)
	protected static @NotNull SimpleBlockConfiguration weightedConfig(@NotNull Function<SimpleWeightedRandomList.Builder<BlockState>, SimpleWeightedRandomList.Builder<BlockState>> states) {
		return new SimpleBlockConfiguration(new WeightedStateProvider(
			states.apply(SimpleWeightedRandomList.builder())
		));
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
	protected static @NotNull String singleNameOf(LocationLike locationLike) {
		return singleNameOf(nameOf(locationLike));
	}

	@Contract("_ -> new")
	protected static @NotNull String patchNameOf(LocationLike locationLike) {
		return patchNameOf(nameOf(locationLike));
	}

	protected static int calculateExponential(int t, double k) {
		int maxWeight = 100;
		double dec = (maxWeight * Math.exp(k * (t - 1))) - maxWeight;

		return (int) Math.max(1, maxWeight - dec);
	}
}
