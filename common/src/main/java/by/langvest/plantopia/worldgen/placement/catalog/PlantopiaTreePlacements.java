package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.CHECKED;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.createKey;

public interface PlantopiaTreePlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

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

    ResourceKey<PlacedFeature> TINY_YELLOW_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TINY_YELLOW_ASPEN, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.TINY_YELLOW_ASPEN)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> RED_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.RED_ASPEN, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.RED_ASPEN)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> TINY_RED_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TINY_RED_ASPEN, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.TINY_RED_ASPEN)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> ACACIA_CYPRESS_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.ACACIA_CYPRESS, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.ACACIA_CYPRESS)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.ACACIA_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> BIRCH_CYPRESS_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.BIRCH_CYPRESS, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.BIRCH_CYPRESS)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> SPRUCE_CYPRESS_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SPRUCE_CYPRESS, CHECKED),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.SPRUCE_CYPRESS)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> TALL_OAK_BEES_0002 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TALL_OAK_BEES_0002),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.TALL_OAK_BEES_0002)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
            ))
    );

    ResourceKey<PlacedFeature> OAK_BUSH = declarePlacement(
        compileNameFrom(PlantopiaFeatures.OAK_BUSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaFeatures.OAK_BUSH)
            .modifiers(context -> List.of(
                PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
            ))
    );
}
