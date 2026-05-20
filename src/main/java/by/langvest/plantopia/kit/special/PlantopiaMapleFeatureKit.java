package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaMapleFeatureKit extends PlantopiaKit {
    public final PlantopiaMapleConfiguredFeatureKit configured;
    public final PlantopiaMaplePlacedFeatureKit placed;

    public final AbstractTreeGrower treeGrower;

    public PlantopiaMapleFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling,
        ResourceKey<PlacedFeature> leafLitterPlacement,
        PlantopiaTreeKitConfiguration config
    ) {
        this.configured = new PlantopiaMapleConfiguredFeatureKit(
            baseName,
            log,
            leaves,
            leafLitterPlacement,
            config
        );

        this.placed = new PlantopiaMaplePlacedFeatureKit(
            baseName,
            configured,
            sapling,
            config
        );

        this.treeGrower = createTreeGrower();
    }

    protected AbstractTreeGrower createTreeGrower() {
        return new AbstractTreeGrower() {
            @Override
            protected @NotNull ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {
                if (random.nextInt(10) == 0) {
                    return hasFlowers ? configured.fancyTreeBees005 : configured.fancyTree;
                } else {
                    return hasFlowers ? configured.treeBees005 : configured.tree;
                }
            }
        };
    }
}
