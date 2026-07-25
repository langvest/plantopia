package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.treeDeclaration;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public interface PlantopiaArborealPlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

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

    ResourceKey<PlacedFeature> TREES_LAVENDER_FIELDS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_LAVENDER_FIELDS),
        treeDeclaration(PlantopiaFeatures.TREES_LAVENDER_FIELDS, PlacementUtils.countExtra(0, 0.125F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.LAVENDER_FIELDS)
            )
    );

    ResourceKey<PlacedFeature> TREES_POPPY_FIELDS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_POPPY_FIELDS),
        treeDeclaration(PlantopiaFeatures.TREES_POPPY_FIELDS, PlacementUtils.countExtra(0, 0.125F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.POPPY_FIELDS)
            )
    );


    ResourceKey<PlacedFeature> TREES_ASPEN_GROVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_ASPEN_GROVE),
        treeDeclaration(PlantopiaFeatures.TREES_ASPEN_GROVE, PlacementUtils.countExtra(5, 0.1F, 2))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.ASPEN_GROVE)
            )
    );

    ResourceKey<PlacedFeature> TREES_ASPEN_CLEARING = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_ASPEN_CLEARING),
        treeDeclaration(PlantopiaFeatures.TREES_ASPEN_CLEARING, PlacementUtils.countExtra(1, 0.25F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.ASPEN_CLEARING)
            )
    );

    ResourceKey<PlacedFeature> TREES_SNOWY_ASPEN_GROVE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_SNOWY_ASPEN_GROVE),
        treeDeclaration(PlantopiaFeatures.TREES_SNOWY_ASPEN_GROVE, PlacementUtils.countExtra(5, 0.1F, 2))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SNOWY_ASPEN_GROVE)
            )
    );

    ResourceKey<PlacedFeature> TREES_SNOWY_ASPEN_CLEARING = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_SNOWY_ASPEN_CLEARING),
        treeDeclaration(PlantopiaFeatures.TREES_SNOWY_ASPEN_CLEARING, PlacementUtils.countExtra(1, 0.25F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SNOWY_ASPEN_CLEARING)
            )
    );

    ResourceKey<PlacedFeature> TREES_SEASONAL_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_SEASONAL_FOREST),
        treeDeclaration(PlantopiaFeatures.TREES_SEASONAL_FOREST, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.SEASONAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_BOREAL_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_BOREAL_FOREST),
        treeDeclaration(PlantopiaFeatures.TREES_BOREAL_FOREST, PlacementUtils.countExtra(12, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.BOREAL_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_MAPLE_WOODS = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_MAPLE_WOODS),
        treeDeclaration(PlantopiaFeatures.TREES_MAPLE_WOODS, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.MAPLE_WOODS)
            )
    );

    ResourceKey<PlacedFeature> TREES_AMBER_THICKET = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_AMBER_THICKET),
        treeDeclaration(PlantopiaFeatures.TREES_AMBER_THICKET, PlacementUtils.countExtra(14, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.AMBER_THICKET)
            )
    );

    ResourceKey<PlacedFeature> TREES_OAK_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_OAK_FOREST),
        treeDeclaration(PlantopiaFeatures.TREES_OAK_FOREST, PlacementUtils.countExtra(10, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.OAK_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_OLD_GROWTH_OAK_FOREST = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_OLD_GROWTH_OAK_FOREST),
        treeDeclaration(PlantopiaFeatures.TREES_OLD_GROWTH_OAK_FOREST, PlacementUtils.countExtra(8, 0.25F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.OLD_GROWTH_OAK_FOREST)
            )
    );

    ResourceKey<PlacedFeature> TREES_BLOOMING_GLADE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TREES_BLOOMING_GLADE),
        treeDeclaration(PlantopiaFeatures.TREES_BLOOMING_GLADE, PlacementUtils.countExtra(2, 0.1F, 1))
            .biomes(biomes -> biomes
                .add(PlantopiaBiomes.BLOOMING_GLADE)
            )
    );
}
