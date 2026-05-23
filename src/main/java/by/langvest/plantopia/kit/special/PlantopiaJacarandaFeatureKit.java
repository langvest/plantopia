package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaJacarandaFeatureKit extends PlantopiaKit {
    public final PlantopiaJacarandaConfiguredFeatureKit configured;
    public final PlantopiaJacarandaPlacedFeatureKit placed;

    public final AbstractTreeGrower treeGrower;

    public PlantopiaJacarandaFeatureKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> leaves,
        Supplier<Block> sapling,
        PlantopiaTreeKitConfiguration config
    ) {
        this.configured = new PlantopiaJacarandaConfiguredFeatureKit(
            baseName,
            log,
            leaves,
            config
        );

        this.placed = new PlantopiaJacarandaPlacedFeatureKit(
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
                return hasFlowers ? configured.treeBees005 : configured.tree;
            }
        };
    }
}
