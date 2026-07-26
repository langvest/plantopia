package by.langvest.plantopia.kit.tree.jacaranda;

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
public class PlantopiaJacarandaWorldgenKit extends PlantopiaKit {
    public final PlantopiaJacarandaFeatureKit feature;
    public final PlantopiaJacarandaPlacementKit placement;
    public final AbstractTreeGrower treeGrower;

    public PlantopiaJacarandaWorldgenKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling
    ) {
        this.feature = new PlantopiaJacarandaFeatureKit(
            baseName,
            log,
            leaves
        );

        this.placement = new PlantopiaJacarandaPlacementKit(
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
                return hasFlowers ? feature.treeBees005 : feature.tree;
            }
        };
    }
}
