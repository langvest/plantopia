package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.worldgen.feature.config.PlantopiaPitConfiguration;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

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
        "quicksand_pit",
        PlantopiaFeatureDeclaration.builder()
            .feature(pit(context -> new PlantopiaPitConfiguration(
                UniformInt.of(5, 9),
                UniformInt.of(3, 5),
                UniformInt.of(3, 5),
                UniformFloat.of(0.2F, 0.25F),
                simpleProvider(PlantopiaBlocks.QUICKSAND.get()),
                Optional.of(BlockPredicate.matchesBlocks(Blocks.SAND)),
                Optional.of(Heightmap.Types.OCEAN_FLOOR_WG)
            )))
    );
}
