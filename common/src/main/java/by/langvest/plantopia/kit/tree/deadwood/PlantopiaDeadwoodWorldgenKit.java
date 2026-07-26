package by.langvest.plantopia.kit.tree.deadwood;

import by.langvest.plantopia.kit.special.PlantopiaKit;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaDeadwoodWorldgenKit extends PlantopiaKit {
    public final PlantopiaDeadwoodFeatureKit feature;
    public final PlantopiaDeadwoodPlacementKit placement;
    public final AbstractTreeGrower treeGrower;

    public PlantopiaDeadwoodWorldgenKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling
    ) {
        this.feature = new PlantopiaDeadwoodFeatureKit(
            baseName,
            log,
            leaves
        );

        this.placement = new PlantopiaDeadwoodPlacementKit(
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
                    return feature.fancyTree;
                } else {
                    return feature.tree;
                }
            }
        };
    }
}
