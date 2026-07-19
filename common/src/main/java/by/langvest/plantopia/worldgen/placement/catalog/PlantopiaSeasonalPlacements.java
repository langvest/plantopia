package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;
import static by.langvest.plantopia.worldgen.util.PlantopiaProviderUtils.weightedListInt;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public interface PlantopiaSeasonalPlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    /* MARSH PLACEMENTS ******************************************/

    ResourceKey<PlacedFeature> SEASONAL_DARK_OAK_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SEASONAL_DARK_OAK, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SEASONAL_DARK_OAK)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> SEASONAL_DARK_OAK_LITTER_055 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SEASONAL_DARK_OAK_LITTER_055),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SEASONAL_DARK_OAK_LITTER_055)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> SEASONAL_DARK_FOREST_VEGETATION = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SEASONAL_DARK_FOREST_VEGETATION),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SEASONAL_DARK_FOREST_VEGETATION)
            .modifiers(context -> List.of(
                CountPlacement.of(16),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SEASONAL_DARK_FOREST)
            )
    );

    ResourceKey<PlacedFeature> YELLOW_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.YELLOW_ASPEN, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.YELLOW_ASPEN)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> YELLOW_ASPEN_BEES_0002 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> YELLOW_ASPEN_BEES_0002_LITTER_055 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002_LITTER_055),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002_LITTER_055)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> TINY_YELLOW_ASPEN_ASPEN_CLEARING = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TINY_YELLOW_ASPEN, PlantopiaBiomes.ASPEN_CLEARING),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.TINY_YELLOW_ASPEN)
            .modifiers(context -> List.of(
                CountPlacement.of(UniformInt.of(0, 1)),
                InSquarePlacement.spread(),
                TREE_THRESHOLD,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.ASPEN_CLEARING)
            )
    );

    ResourceKey<PlacedFeature> RED_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.RED_ASPEN, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.RED_ASPEN)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> TINY_RED_ASPEN = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TINY_RED_ASPEN),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.TINY_RED_ASPEN)
            .modifiers(context -> List.of(
                CountPlacement.of(UniformInt.of(0, 1)),
                InSquarePlacement.spread(),
                TREE_THRESHOLD,
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SNOWY_ASPEN_CLEARING)
            )
    );

    ResourceKey<PlacedFeature> TREES_ASPEN_GROVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_ASPEN_GROVE),
        getTreeDeclaration(PlantopiaFeatures.TREES_ASPEN_GROVE, PlacementUtils.countExtra(5, 0.1F, 2))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.ASPEN_GROVE)
            )
    );

    ResourceKey<PlacedFeature> TREES_ASPEN_CLEARING = declarePlacement(
        compileNameFrom(TREES, PlantopiaBiomes.ASPEN_CLEARING),
        getTreeDeclaration(
            PlantopiaFeatures.TREES_ASPEN_GROVE,
            CountPlacement.of(weightedListInt(values -> values
                .add(ConstantInt.of(0), 5)
                .add(ConstantInt.of(1), 3)
                .add(ConstantInt.of(2), 1)
            ))
        )
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.ASPEN_CLEARING)
            )
    );

    ResourceKey<PlacedFeature> TREES_SNOWY_ASPEN_GROVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_SNOWY_ASPEN_GROVE),
        getTreeDeclaration(PlantopiaFeatures.TREES_SNOWY_ASPEN_GROVE, PlacementUtils.countExtra(4, 0.1F, 2))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SNOWY_ASPEN_GROVE)
            )
    );

    ResourceKey<PlacedFeature> TREES_SNOWY_ASPEN_CLEARING = declarePlacement(
        compileNameFrom(TREES, PlantopiaBiomes.SNOWY_ASPEN_CLEARING),
        getTreeDeclaration(
            PlantopiaFeatures.TREES_SNOWY_ASPEN_GROVE,
            CountPlacement.of(weightedListInt(values -> values
                .add(ConstantInt.of(0), 5)
                .add(ConstantInt.of(1), 3)
                .add(ConstantInt.of(2), 1)
            ))
        )
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SNOWY_ASPEN_CLEARING)
            )
    );

    ResourceKey<PlacedFeature> TREES_SEASONAL_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_SEASONAL_FOREST),
        getTreeDeclaration(PlantopiaFeatures.TREES_SEASONAL_FOREST, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SEASONAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_BOREAL_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_BOREAL_FOREST),
        getTreeDeclaration(PlantopiaFeatures.TREES_BOREAL_FOREST, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.BOREAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_MAPLE_WOODS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_MAPLE_WOODS),
        getTreeDeclaration(PlantopiaFeatures.TREES_MAPLE_WOODS, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.MAPLE_WOODS)
            )
    );

    ResourceKey<PlacedFeature> PATCH_YELLOW_LEAF_LITTER_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_YELLOW_LEAF_LITTER, CHECKED),
        getCheckedLeafLitterDeclaration(PlantopiaFeatures.PATCH_YELLOW_LEAF_LITTER)
    );

    ResourceKey<PlacedFeature> PATCH_ORANGE_LEAF_LITTER_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_ORANGE_LEAF_LITTER, CHECKED),
        getCheckedLeafLitterDeclaration(PlantopiaFeatures.PATCH_ORANGE_LEAF_LITTER)
    );

    ResourceKey<PlacedFeature> PATCH_RED_LEAF_LITTER_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.PATCH_RED_LEAF_LITTER, CHECKED),
        getCheckedLeafLitterDeclaration(PlantopiaFeatures.PATCH_RED_LEAF_LITTER)
    );
}
