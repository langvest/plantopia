package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCobblestoneShardBlock;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaLimitedRandomPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import com.google.common.collect.Maps;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.features.MiscOverworldFeatures
 */
public class PlantopiaMiscOverworldFeatures extends PlantopiaFeatures {
    private static final Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareConfiguredFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> QUICKSAND_PIT = declareConfiguredFeature(
        compileNameFrom(PlantopiaBlocks.QUICKSAND, PIT),
        PlantopiaFeatureDeclaration.builder()
            .feature(pit(context ->
                new PlantopiaPitConfiguration(
                    UniformInt.of(5, 9),
                    UniformInt.of(3, 5),
                    UniformInt.of(3, 5),
                    UniformFloat.of(0.2F, 0.25F),
                    simpleProvider(PlantopiaBlocks.QUICKSAND.get()),
                    Optional.of(BlockPredicate.matchesBlocks(Blocks.SAND)),
                    Optional.of(Heightmap.Types.OCEAN_FLOOR_WG)
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_SEA_SHELL = declareConfiguredFeature(
        patchNameOf("sea_shell"),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(context ->
                new PlantopiaLimitedRandomPatchConfiguration(
                    UniformInt.of(8, 12),
                    UniformInt.of(3, 4),
                    ClampedInt.of(UniformInt.of(1, 4), 2, 4),
                    ClampedInt.of(UniformInt.of(1, 4), 2, 4),
                    PlacementUtils.filtered(
                        PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                        weightedConfig(states -> states
                            .add(PlantopiaBlocks.ROUND_SEA_SHELL.get().defaultBlockState(), 3)
                            .add(PlantopiaBlocks.TWISTY_SEA_SHELL.get().defaultBlockState(), 4)
                            .add(PlantopiaBlocks.TUBE_SEA_SHELL.get().defaultBlockState(), 4)
                        ),
                        BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.WATER, Blocks.SEAGRASS)
                    )
                )
            ))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_COBBLESTONE_SHARD = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.COBBLESTONE_SHARD),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(getCobblestoneShardConfig(PlantopiaBlocks.COBBLESTONE_SHARD)))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_MOSSY_COBBLESTONE_SHARD = declareConfiguredFeature(
        patchNameOf(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD),
        PlantopiaFeatureDeclaration.builder()
            .feature(limitedRandomPatch(getCobblestoneShardConfig(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD)))
    );

    /* HELPER METHODS *************************************************************************/

    @Contract(pure = true)
    private static @NotNull Function<BootstapContext<ConfiguredFeature<?, ?>>, PlantopiaLimitedRandomPatchConfiguration> getCobblestoneShardConfig(Supplier<Block> cobblestoneShardBlock) {
        return context ->
            new PlantopiaLimitedRandomPatchConfiguration(
                UniformInt.of(3, 4), // tries
                UniformInt.of(2, 3), // limit
                ConstantInt.of(1), // xzSpread
                ConstantInt.of(1), // ySpread
                PlacementUtils.filtered(
                    PlantopiaFeatureTypes.NATURAL_BLOCK.get(),
                    weightedConfig(states -> {
                        for (int amount = PlantopiaCobblestoneShardBlock.MIN_SHARDS; amount <= PlantopiaCobblestoneShardBlock.MAX_SHARDS; amount++) {
                            int weight = calculateExponentialWeight(amount, 0.2165);

                            var state = cobblestoneShardBlock.get().defaultBlockState()
                                .setValue(PlantopiaCobblestoneShardBlock.AMOUNT, amount);

                            states.add(state, weight);
                        }

                        return states;
                    }),
                    BlockPredicate.matchesBlocks(Blocks.AIR, Blocks.GRASS, Blocks.WATER, Blocks.SEAGRASS)
                )
            );
    }
}
