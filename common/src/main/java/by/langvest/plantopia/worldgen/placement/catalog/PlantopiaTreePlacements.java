package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.PlantopiaDictionary.CHECKED;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.checkedTreeDeclaration;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.createKey;

public interface PlantopiaTreePlacements {
    Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<PlacedFeature> SEASONAL_DARK_OAK_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SEASONAL_DARK_OAK, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.SEASONAL_DARK_OAK, Blocks.DARK_OAK_SAPLING)
    );

    ResourceKey<PlacedFeature> SEASONAL_DARK_OAK_LITTER_055 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SEASONAL_DARK_OAK_LITTER_055),
        checkedTreeDeclaration(PlantopiaFeatures.SEASONAL_DARK_OAK_LITTER_055, Blocks.DARK_OAK_SAPLING)
    );

    ResourceKey<PlacedFeature> YELLOW_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.YELLOW_ASPEN, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.YELLOW_ASPEN, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> YELLOW_ASPEN_BEES_0002 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002),
        checkedTreeDeclaration(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> YELLOW_ASPEN_BEES_0002_LITTER_055 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002_LITTER_055),
        checkedTreeDeclaration(PlantopiaFeatures.YELLOW_ASPEN_BEES_0002_LITTER_055, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> TINY_YELLOW_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TINY_YELLOW_ASPEN, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.TINY_YELLOW_ASPEN, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> RED_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.RED_ASPEN, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.RED_ASPEN, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> TINY_RED_ASPEN_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TINY_RED_ASPEN, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.TINY_RED_ASPEN, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> ACACIA_CYPRESS_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.ACACIA_CYPRESS, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.ACACIA_CYPRESS, Blocks.ACACIA_SAPLING)
    );

    ResourceKey<PlacedFeature> BIRCH_CYPRESS_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.BIRCH_CYPRESS, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.BIRCH_CYPRESS, Blocks.BIRCH_SAPLING)
    );

    ResourceKey<PlacedFeature> SPRUCE_CYPRESS_CHECKED = declarePlacement(
        compileNameFrom(PlantopiaFeatures.SPRUCE_CYPRESS, CHECKED),
        checkedTreeDeclaration(PlantopiaFeatures.SPRUCE_CYPRESS, Blocks.SPRUCE_SAPLING)
    );

    ResourceKey<PlacedFeature> TALL_OAK_BEES_0002 = declarePlacement(
        compileNameFrom(PlantopiaFeatures.TALL_OAK_BEES_0002),
        checkedTreeDeclaration(PlantopiaFeatures.TALL_OAK_BEES_0002, Blocks.OAK_SAPLING)
    );

    ResourceKey<PlacedFeature> OAK_BUSH = declarePlacement(
        compileNameFrom(PlantopiaFeatures.OAK_BUSH),
        checkedTreeDeclaration(PlantopiaFeatures.OAK_BUSH, Blocks.OAK_SAPLING)
    );

    ResourceKey<PlacedFeature> DEADWOOD_SPIRE = declarePlacement(
        compileNameFrom(PlantopiaFeatures.DEADWOOD_SPIRE),
        checkedTreeDeclaration(PlantopiaFeatures.DEADWOOD_SPIRE, PlantopiaKits.DEADWOOD.sapling)
    );
}
