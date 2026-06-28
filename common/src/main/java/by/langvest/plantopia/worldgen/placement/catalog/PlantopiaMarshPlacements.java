package by.langvest.plantopia.worldgen.placement.catalog;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaMiscOverworldFeatures;
import by.langvest.plantopia.worldgen.feature.catalog.PlantopiaVegetationFeatures;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.special.*;
import by.langvest.plantopia.worldgen.placement.verticalanchor.PlantopiaVerticalAnchor;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils.*;

/**
 * @see net.minecraft.data.worldgen.placement.VegetationPlacements
 */
public final class PlantopiaMarshPlacements {
    public static final Catalog<ResourceKey<PlacedFeature>, PlantopiaPlacementDeclaration> DECLARATION = Catalog.newCatalog();

    public static @NotNull ResourceKey<PlacedFeature> declarePlacement(String name, PlantopiaPlacementDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    /* MARSH PLACEMENTS ******************************************/

    public static final ResourceKey<PlacedFeature> LAKE_WATER_MARSH = declarePlacement(
        compileNameFrom(PlantopiaMiscOverworldFeatures.LAKE_WATER_MARSH),
        PlantopiaPlacementDeclaration.builder()
            .step(GenerationStep.Decoration.LAKES)
            .feature(PlantopiaMiscOverworldFeatures.LAKE_WATER_MARSH)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(2.42F, 5.24F),
                CountPlacement.of(UniformInt.of(1, 3)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .addTag(PlantopiaBiomeTags.IS_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_WATERLILY_MARSH = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_WATERLILY, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(VegetationFeatures.PATCH_WATERLILY)
            .modifiers(context -> List.of(
                CountPlacement.of(UniformInt.of(0, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(Blocks.LILY_PAD)
            ))
            .biomes(biomes -> biomes
                .addTag(PlantopiaBiomeTags.IS_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_FLOWERING_LILY_PAD_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_FLOWERING_LILY_PAD_MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_FLOWERING_LILY_PAD_MARSH)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(4.64F),
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.WHITE_FLOWERING_LILY_PAD.get())
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_FLOWERING_LILY_PAD_DEAD_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_FLOWERING_LILY_PAD_DEAD_MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_FLOWERING_LILY_PAD_DEAD_MARSH)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(4.64F),
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome(),
                PlacementUtils.filteredByBlockSurvival(PlantopiaBlocks.RED_FLOWERING_LILY_PAD.get())
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.DEAD_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_REED_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_REED, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_REED)
            .modifiers(context -> List.of(
                CountPlacement.of(UniformInt.of(2, 4)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                WATER_PLANT_FIND_WATER,
                WATER_PLANT_RANGE_FILTER,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.DEAD_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_AZURE_BLUET_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_AZURE_BLUET, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_AZURE_BLUET)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(6),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_SUGAR_CANE_MARSH = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_SUGAR_CANE, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(VegetationFeatures.PATCH_SUGAR_CANE)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(5.24F),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_GRASS_MARSH = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_GRASS, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(VegetationFeatures.PATCH_GRASS)
            .modifiers(context -> List.of(
                CountPlacement.of(12),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .addTag(PlantopiaBiomeTags.IS_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_PUMPKIN_MARSH = declarePlacement(
        compileNameFrom(VegetationFeatures.PATCH_PUMPKIN, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(VegetationFeatures.PATCH_PUMPKIN)
            .modifiers(context -> List.of(
                RarityFilter.onAverageOnceEvery(80),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.DEAD_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_GIANT_GRASS_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_GIANT_GRASS, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_GIANT_GRASS)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(1.64F),
                CountPlacement.of(UniformInt.of(1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .addTag(PlantopiaBiomeTags.IS_MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_SWEET_FLAG_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_SWEET_FLAG)
            .modifiers(context -> {
                var bigNoiseConfig = PlantopiaNoiseConfig.of(0.048D, 534, 22);
                var smallNoiseConfig = PlantopiaNoiseConfig.of(0.022D, 43, 884);
                float bigNoiseLevel = -0.26F;
                float smallNoiseLevel = -0.1F;

                return List.of(
                    PlantopiaNoiseCountPlacement.below(bigNoiseConfig, bigNoiseLevel, 20),
                    InSquarePlacement.spread(),
                    PlantopiaNoiseFilter.below(bigNoiseConfig, bigNoiseLevel, 0.1F),
                    PlantopiaNoiseFilter.above(smallNoiseConfig, smallNoiseLevel, 0.15F),
                    PlacementUtils.HEIGHTMAP_TOP_SOLID,
                    WATER_PLANT_FIND_WATER,
                    WATER_PLANT_RANGE_FILTER,
                    BiomeFilter.biome()
                );
            })
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.MARSH)
            )
    );

    public static final ResourceKey<PlacedFeature> PATCH_BRANCHING_SHRUB_MARSH = declarePlacement(
        compileNameFrom(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB, MARSH),
        PlantopiaPlacementDeclaration.builder()
            .feature(PlantopiaVegetationFeatures.PATCH_BRANCHING_SHRUB)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(4.42F),
                CountPlacement.of(UniformInt.of(1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_TOP_SOLID,
                PlantopiaRangeFilter.above(PlantopiaVerticalAnchor.seaLevel()),
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(PlantopiaOverworldBiomes.DEAD_MARSH)
            )
    );
}
