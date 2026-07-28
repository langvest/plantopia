package by.langvest.plantopia.kit.tree.palm;

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
public class PlantopiaPalmWorldgenKit extends PlantopiaKit {
    public final PlantopiaPalmFeatureKit feature;
    public final PlantopiaPalmPlacementKit placement;
    public final AbstractTreeGrower treeGrower;

    public PlantopiaPalmWorldgenKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling
    ) {
        this.feature = new PlantopiaPalmFeatureKit(
            baseName,
            log,
            leaves
        );

        this.placement = new PlantopiaPalmPlacementKit(
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
                return feature.tree;
            }
        };
    }
}
