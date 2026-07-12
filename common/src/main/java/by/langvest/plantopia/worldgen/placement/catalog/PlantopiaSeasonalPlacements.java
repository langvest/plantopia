package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

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

    ResourceKey<PlacedFeature> ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.ASPEN, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.ASPEN)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> ASPEN_BEES_0002 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.ASPEN_BEES_0002),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.ASPEN_BEES_0002)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> ASPEN_BEES_0002_LITTER_055 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.ASPEN_BEES_0002_LITTER_055),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.ASPEN_BEES_0002_LITTER_055)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> TREES_ASPEN_GROVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_ASPEN_GROVE),
        getTreeDeclaration(PlantopiaFeatures.TREES_ASPEN_GROVE, PlacementUtils.countExtra(5, 0.1F, 2))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.ASPEN_GROVE)
            )
    );

    ResourceKey<PlacedFeature> TREES_SEASONAL_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_SEASONAL_FOREST),
        getTreeDeclaration(PlantopiaFeatures.TREES_SEASONAL_FOREST, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SEASONAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_BOREAL_WOODS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_BOREAL_WOODS),
        getTreeDeclaration(PlantopiaFeatures.TREES_BOREAL_WOODS, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.BOREAL_WOODS)
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
