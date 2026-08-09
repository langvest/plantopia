package by.langvest.plantopia.kit.tree.maple;

import by.langvest.plantopia.kit.special.PlantopiaKit;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaMapleWorldgenKit extends PlantopiaKit {
    public final PlantopiaMapleFeatureKit feature;
    public final PlantopiaMaplePlacementKit placement;

    public final AbstractTreeGrower treeGrower;

    public PlantopiaMapleWorldgenKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling,
        Supplier<Block> balk,
        Supplier<Block> stub,
        ResourceKey<PlacedFeature> leafLitterPlacement
    ) {
        this.feature = new PlantopiaMapleFeatureKit(
            baseName,
            log,
            leaves,
            balk,
            stub,
            leafLitterPlacement
        );

        this.placement = new PlantopiaMaplePlacementKit(
            baseName,
            feature,
            sapling
        );

        this.treeGrower = createTreeGrower();
    }

    protected AbstractTreeGrower createTreeGrower() {
        return new AbstractTreeGrower() {
            @Override
            protected @NotNull ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {
                if (random.nextInt(10) == 0) {
                    return hasFlowers ? feature.fancyTreeBees005 : feature.fancyTree;
                } else {
                    return hasFlowers ? feature.treeBees005 : feature.tree;
                }
            }
        };
    }
}
