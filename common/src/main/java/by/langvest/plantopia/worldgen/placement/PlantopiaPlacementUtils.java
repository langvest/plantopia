package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaRangeFilter;
import by.langvest.plantopia.worldgen.util.verticalanchor.PlantopiaVerticalAnchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.cascades;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedListInt;

public final class PlantopiaPlacementUtils {
    public static final PlantopiaRangeFilter WATER_PLANT_RANGE_FILTER = PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel(-1));
    public static final PlacementModifier TREE_THRESHOLD = SurfaceWaterDepthFilter.forMaxDepth(0);

    public static final EnvironmentScanPlacement WATER_PLANT_FIND_WATER = EnvironmentScanPlacement.scanningFor(
        Direction.DOWN,
        BlockPredicate.matchesFluids(BlockPos.ZERO, Fluids.WATER),
        4
    );

    /* KEY *************************************************/

    public static @NotNull ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, plantopia(name));
    }

    /* CONTEXT *************************************************/

    public static @NotNull HolderGetter<ConfiguredFeature<?, ?>> lookupFeatures(@NotNull BootstapContext<PlacedFeature> context) {
        return context.lookup(Registries.CONFIGURED_FEATURE);
    }

    public static @NotNull HolderGetter<PlacedFeature> lookupPlacements(@NotNull BootstapContext<PlacedFeature> context) {
        return context.lookup(Registries.PLACED_FEATURE);
    }

    public static @NotNull HolderGetter<Biome> lookupBiomes(@NotNull BootstapContext<PlacedFeature> context) {
        return context.lookup(Registries.BIOME);
    }

    @SafeVarargs
    public static @NotNull HolderSet<Biome> directBiomes(@NotNull BootstapContext<PlacedFeature> context, ResourceKey<Biome>... biomeKeys) {
        var biomes = lookupBiomes(context);
        return HolderSet.direct(Arrays.stream(biomeKeys).map(biomes::getOrThrow).toList());
    }

    /* BIOMES ******************************************/

    public static void addVanillaMountainBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
        tagSet
            .add(Biomes.PLAINS, Biomes.MEADOW)
            .add(Biomes.STONY_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS);
    }

    public static void addVanillaOldGrowthBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
        tagSet
            .add(Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST);
    }

    public static void addVanillaSwampBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
        tagSet
            .add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
    }

    public static void addCascadesBiomes(@NotNull PlantopiaTagSet<Biome> tagSet) {
        tagSet
            .addOptional(cascades("autumnal_forest"))
            .addOptional(cascades("rainforest"))
            .addOptional(cascades("seasonal_forest"))
            .addOptional(cascades("temperate_rainforest"));
    }

    /* HELPER METHODS ******************************************/

    public static PlantopiaPlacementDeclaration.Builder getTreeDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier modifier) {
        return PlantopiaPlacementDeclaration.builder()
            .feature(feature)
            .modifiers(context -> List.of(
                modifier,
                InSquarePlacement.spread(),
                TREE_THRESHOLD,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
            ));
    }

    public static PlantopiaPlacementDeclaration.Builder getCheckedLeafLitterDeclaration(ResourceKey<ConfiguredFeature<?, ?>> feature) {
        return PlantopiaPlacementDeclaration.builder()
            .feature(feature)
            .modifiers(context -> List.of(
                RandomOffsetPlacement.horizontal(weightedListInt(values -> values
                    .add(UniformInt.of(-3, 3), 2)
                    .add(UniformInt.of(-2, 2), 5)
                )),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE
            ));
    }
}
