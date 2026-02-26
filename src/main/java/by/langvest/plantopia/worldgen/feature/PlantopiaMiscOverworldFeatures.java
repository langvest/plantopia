package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaLimitedRandomPatchConfiguration;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import com.google.common.collect.Maps;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

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
                        BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE
                    )
                )
            ))
    );
}
