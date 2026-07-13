package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.config.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.*;
import static by.langvest.plantopia.worldgen.value.PlantopiaProviderUtils.simpleProvider;
import static by.langvest.plantopia.worldgen.value.PlantopiaProviderUtils.weightedProvider;

public final class PlantopiaFeatureUtils {
    public static final float CHANCE_055 = 0.55F;
    public static final float CHANCE_005 = 0.05F;
    public static final float CHANCE_0002 = 0.002F;

    public static final BlockPredicate WATER_PlANT_PREDICATE = BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GRASS, Blocks.SEAGRASS);

    public static final Supplier<BlockPredicate> BRANCHING_SHRUB_VERTICAL_PREDICATE = () -> BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.GLOW_LICHEN, Blocks.SEAGRASS, Blocks.GRASS, Blocks.FERN);
    public static final Supplier<BlockPredicate> BRANCHING_SHRUB_HORIZONTAL_PREDICATE = () -> BlockPredicate.allOf(
        BRANCHING_SHRUB_VERTICAL_PREDICATE.get(),
        BlockPredicate.anyOf(
            BlockPredicate.matchesBlocks(BlockPos.ZERO.below(), PlantopiaBlocks.BRANCHING_SHRUB.get()),
            BlockPredicate.replaceable(BlockPos.ZERO.below())
        )
    );

    public static final BlockPredicate GRASS_PLANT_PREDICATE = BlockPredicate.allOf(
        BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS),
        BlockPredicate.solid(BlockPos.ZERO.below())
    );

    public static final BlockPredicate ON_SAND_PREDICATE = BlockPredicate.allOf(
        BlockPredicate.ONLY_IN_AIR_PREDICATE,
        BlockPredicate.matchesTag(BlockPos.ZERO.below(), BlockTags.SAND)
    );

    /* KEY ******************************************/

    public static @NotNull ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
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
    public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> composite(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaCompositeConfiguration> configFactory) {
        return configuredFeature(PlantopiaFeatureTypes.COMPOSITE, configFactory);
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
        return configuredFeature(PlantopiaFeatureTypes.NATURAL_BLOCK, configFactory);
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
        return configuredFeature(PlantopiaFeatureTypes.LIMITED_RANDOM_PATCH, configFactory);
    }

    @Contract(pure = true)
    public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> radialPatch(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaRadialPatchConfiguration> configFactory) {
        return configuredFeature(PlantopiaFeatureTypes.RADIAL_PATCH, configFactory);
    }

    @Contract(pure = true)
    public static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, ConfiguredFeature<?, ?>> pit(Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaPitConfiguration> configFactory) {
        return configuredFeature(PlantopiaFeatureTypes.PIT, configFactory);
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

    /* CONTEXT ************************************************/

    public static @NotNull HolderGetter<ConfiguredFeature<?, ?>> lookupFeatures(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
        return context.lookup(Registries.CONFIGURED_FEATURE);
    }

    public static @NotNull HolderGetter<PlacedFeature> lookupPlacements(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
        return context.lookup(Registries.PLACED_FEATURE);
    }

    public static @NotNull HolderGetter<Biome> lookupBiomes(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context) {
        return context.lookup(Registries.BIOME);
    }

    /* MATH **********************************************************/

    public static int calculateExponentialWeight(int step, double decay) {
        int maxWeight = 100;
        double weightDecrease = (maxWeight * Math.exp(decay * (step - 1))) - maxWeight;

        return (int) Math.max(1, maxWeight - weightDecrease);
    }
}
