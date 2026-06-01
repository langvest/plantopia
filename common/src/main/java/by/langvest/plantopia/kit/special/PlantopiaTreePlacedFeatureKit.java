package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementDeclaration;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.compileNameFrom;

public class PlantopiaTreePlacedFeatureKit extends PlantopiaKit {
    public final ResourceKey<PlacedFeature> treeBees0002;
    public final ResourceKey<PlacedFeature> fancyTreeBees0002;

    public PlantopiaTreePlacedFeatureKit(
        String baseName,
        @NotNull PlantopiaTreeConfiguredFeatureKit configured,
        Supplier<Block> sapling,
        PlantopiaTreeKitConfiguration config
    ) {
        this.treeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(configured.treeBees0002),
            PlantopiaPlacementDeclaration.builder()
                .feature(configured.treeBees0002)
                .modifiers(context -> List.of(
                    PlacementUtils.filteredByBlockSurvival(sapling.get())
                ))
        );

        this.fancyTreeBees0002 = PlantopiaPlacements.declarePlacement(
            compileNameFrom(configured.fancyTreeBees0002),
            PlantopiaPlacementDeclaration.builder()
                .feature(configured.fancyTreeBees0002)
                .modifiers(context -> List.of(
                    PlacementUtils.filteredByBlockSurvival(sapling.get())
                ))
        );
    }
}
