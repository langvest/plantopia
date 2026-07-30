package by.langvest.plantopia.kit.tree.fir;

import by.langvest.plantopia.kit.special.PlantopiaKit;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaFirWorldgenKit extends PlantopiaKit {
    public final PlantopiaFirFeatureKit feature;
    public final PlantopiaFirPlacementKit placement;
    public final AbstractTreeGrower treeGrower;

    public PlantopiaFirWorldgenKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling
    ) {
        this.feature = new PlantopiaFirFeatureKit(
            baseName,
            log,
            leaves
        );

        this.placement = new PlantopiaFirPlacementKit(
            baseName,
            feature,
            sapling
        );

        this.treeGrower = createTreeGrower();
    }

    protected AbstractTreeGrower createTreeGrower() {
        return new AbstractMegaTreeGrower() {
            @Override
            protected @NotNull ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource random, boolean hasFlowers) {
                return feature.tree;
            }

            @Override
            protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
                return feature.megaTree;
            }
        };
    }
}
