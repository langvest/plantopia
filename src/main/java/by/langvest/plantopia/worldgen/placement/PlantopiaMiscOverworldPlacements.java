package by.langvest.plantopia.worldgen.placement;

import by.langvest.plantopia.worldgen.feature.PlantopiaMiscOverworldFeatures;
import by.langvest.plantopia.worldgen.placement.special.PlantopiaRarityFilter;
import com.google.common.collect.Maps;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ClampedInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

/**
 * @see net.minecraft.data.worldgen.placement.MiscOverworldPlacements
 */
public class PlantopiaMiscOverworldPlacements extends PlantopiaPlacements {
    private static final Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<PlacedFeature>, PlantopiaPlacedFeatureDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<PlacedFeature> declarePlacedFeature(String name, PlantopiaPlacedFeatureDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<PlacedFeature> QUICKSAND_PIT = declarePlacedFeature(
        compileNameFrom(PlantopiaMiscOverworldFeatures.QUICKSAND_PIT),
        PlantopiaPlacedFeatureDeclaration.builder()
            .generationStep(GenerationStep.Decoration.LOCAL_MODIFICATIONS)
            .feature(PlantopiaMiscOverworldFeatures.QUICKSAND_PIT)
            .modifiers(context -> List.of(
                PlantopiaRarityFilter.onAverageOnceEvery(20.0F, 22.0F),
                CountPlacement.of(ClampedInt.of(UniformInt.of(-1, 2), 1, 2)),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome()
            ))
            .biomes(biomes -> biomes
                .add(Biomes.DESERT)
            )
    );
}
