package by.langvest.plantopia.worldgen.feature.catalog;

import by.langvest.plantopia.kit.PlantopiaKits;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaBiomes;
import by.langvest.plantopia.worldgen.feature.PlantopiaFeatureDeclaration;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import by.langvest.toolkit.collection.catalog.Catalog;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static by.langvest.plantopia.util.PlantopiaDictionary.TREES;
import static by.langvest.plantopia.util.PlantopiaDictionary.VEGETATION;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;
import static by.langvest.plantopia.worldgen.feature.PlantopiaFeatureUtils.*;

public interface PlantopiaArborealFeatures {
    Catalog<ResourceKey<ConfiguredFeature<?, ?>>, PlantopiaFeatureDeclaration> DECLARATION = Catalog.newCatalog();

    static @NotNull ResourceKey<ConfiguredFeature<?, ?>> declareFeature(String name, PlantopiaFeatureDeclaration.@NotNull Builder builder) {
        return DECLARATION.add(createKey(name), builder.build()).getKey();
    }

    ResourceKey<ConfiguredFeature<?, ?>> SEASONAL_DARK_FOREST_VEGETATION = declareFeature(
        compileNameFrom(PlantopiaBiomes.SEASONAL_DARK_FOREST, VEGETATION),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var features = lookupFeatures(context);
                var placements = lookupPlacements(context);

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM)), 0.025F),
                        new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)), 0.05F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.SEASONAL_DARK_OAK_LITTER_055), 0.6666667F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.BIRCH_CHECKED), 0.2F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_CHECKED), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.OAK_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_BOREAL_FOREST = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.BOREAL_FOREST),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var yellowMaple = PlantopiaKits.MAPLE.yellowFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.lushTreeBees0002litter055), 0.15F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.treeBees0002litter055), 0.13333334F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.PINE_CHECKED), 0.35F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.OAK_BEES_0002), 0.33333334F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.TALL_OAK_BEES_0002), 0.15F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_MAPLE_WOODS = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.MAPLE_WOODS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var redMaple = PlantopiaKits.MAPLE.redFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.PINE_CHECKED), 0.33333334F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.lushTreeBees0002litter055), 0.4F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.treeBees0002litter055), 0.25F)
                    ),
                    placements.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_AMBER_THICKET = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.AMBER_THICKET),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var orangeMaple = PlantopiaKits.MAPLE.orangeFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.SPRUCE_CHECKED), 0.2533334F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.PINE_CHECKED), 0.2333334F),
                        new WeightedPlacedFeature(placements.getOrThrow(orangeMaple.lushTreeBees0002litter055), 0.3333334F),
                        new WeightedPlacedFeature(placements.getOrThrow(orangeMaple.treeBees0002litter055), 0.25F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.BIRCH_BEES_002), 0.2333334F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.TALL_OAK_BEES_0002), 0.15F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.OAK_BEES_0002)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_SEASONAL_FOREST = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.SEASONAL_FOREST),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var yellowMaple = PlantopiaKits.MAPLE.yellowFeature.placed;
                var orangeMaple = PlantopiaKits.MAPLE.orangeFeature.placed;
                var redMaple = PlantopiaKits.MAPLE.redFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED), 0.03F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.OAK_BEES_0002), 0.05F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.01F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.treeBees0002litter055), 0.25F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.fancyTreeBees0002litter055), 0.1F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.treeBees0002litter055), 0.25F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.fancyTreeBees0002litter055), 0.1F),
                        new WeightedPlacedFeature(placements.getOrThrow(orangeMaple.fancyTreeBees0002litter055), 0.1F)
                    ),
                    placements.getOrThrow(orangeMaple.treeBees0002litter055)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_ASPEN_GROVE = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.ASPEN_GROVE),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var yellowMaple = PlantopiaKits.MAPLE.yellowFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.TINY_YELLOW_ASPEN_CHECKED), 0.15F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.fancyTreeBees0002litter055), 0.1F)
                    ),
                    placements.getOrThrow(PlantopiaPlacements.YELLOW_ASPEN_BEES_0002_LITTER_055)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_ASPEN_CLEARING = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.ASPEN_CLEARING),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var yellowMaple = PlantopiaKits.MAPLE.yellowFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.YELLOW_ASPEN_BEES_0002_LITTER_055), 0.35F),
                        new WeightedPlacedFeature(placements.getOrThrow(yellowMaple.fancyTreeBees0002litter055), 0.1F)
                    ),
                    placements.getOrThrow(PlantopiaPlacements.TINY_YELLOW_ASPEN_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_SNOWY_ASPEN_GROVE = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.SNOWY_ASPEN_GROVE),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var redMaple = PlantopiaKits.MAPLE.redFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.SPRUCE_CHECKED), 0.35F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.PINE_CHECKED), 0.2F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.TINY_RED_ASPEN_CHECKED), 0.15F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.fancyTreeBees0002), 0.1F)
                    ),
                    placements.getOrThrow(PlantopiaPlacements.RED_ASPEN_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_SNOWY_ASPEN_CLEARING = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.SNOWY_ASPEN_CLEARING),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var redMaple = PlantopiaKits.MAPLE.redFeature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.SPRUCE_CHECKED), 0.25F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.RED_ASPEN_CHECKED), 0.2F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.PINE_CHECKED), 0.15F),
                        new WeightedPlacedFeature(placements.getOrThrow(redMaple.fancyTreeBees0002), 0.1F)
                    ),
                    placements.getOrThrow(PlantopiaPlacements.TINY_RED_ASPEN_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_LAVENDER_FIELDS = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.LAVENDER_FIELDS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);
                var jacaranda = PlantopiaKits.JACARANDA.feature.placed;

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(jacaranda.treeBees0002), 0.7F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.OAK_BEES_0002)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_POPPY_FIELDS = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.POPPY_FIELDS),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.OAK_BEES_0002), 0.1333334F)
                    ),
                    placements.getOrThrow(PlantopiaPlacements.ACACIA_CYPRESS_CHECKED)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_OAK_FOREST = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.OAK_FOREST),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.OAK_BEES_0002)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_OLD_GROWTH_OAK_FOREST = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.OLD_GROWTH_OAK_FOREST),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.TALL_OAK_BEES_0002), 0.3F),
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.OAK_BEES_0002), 0.2F)
                    ),
                    placements.getOrThrow(TreePlacements.FANCY_OAK_BEES_0002)
                );
            }))
    );

    ResourceKey<ConfiguredFeature<?, ?>> TREES_BLOOMING_GLADE = declareFeature(
        compileNameFrom(TREES, PlantopiaBiomes.BLOOMING_GLADE),
        PlantopiaFeatureDeclaration.builder()
            .feature(randomSelector(context -> {
                var placements = lookupPlacements(context);

                return new RandomFeatureConfiguration(
                    List.of(
                        new WeightedPlacedFeature(placements.getOrThrow(TreePlacements.OAK_BEES_0002), 0.3F),
                        new WeightedPlacedFeature(placements.getOrThrow(PlantopiaPlacements.TALL_OAK_BEES_0002), 0.1F)
                    ),
                    placements.getOrThrow(TreePlacements.SPRUCE_CHECKED)
                );
            }))
    );
}
