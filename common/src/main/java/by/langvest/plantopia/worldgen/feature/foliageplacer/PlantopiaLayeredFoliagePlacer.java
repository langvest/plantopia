package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class PlantopiaLayeredFoliagePlacer extends PlantopiaFoliagePlacer {
    public PlantopiaLayeredFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        var layerProvider = getLayerProvider(level, blockSetter, random, config, maxFreeTreeHeight, attachment, foliageHeight, foliageRadius, offset);

        for (int dy = offset; dy > offset - foliageHeight; dy--) {
            int layerIndex = offset - dy;
            var layer = layerProvider.provide(layerIndex);
            placeRow(level, blockSetter, random, attachment, config, dy, layer.range(), layer.template(), layer.modifier());
        }
    }

    protected abstract LayerProvider getLayerProvider(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset);

    @FunctionalInterface
    protected interface LayerProvider {
        Layer provide(int layerIndex);
    }

    protected record Layer(
        int range,
        PlantopiaTemplate template,
        @Nullable BlockStateModifier modifier
    ) {
        @Contract("_, _ -> new")
        protected static @NotNull Layer of(int range, PlantopiaTemplate template) {
            return new Layer(range, template, null);
        }

        @Contract("_, _, _ -> new")
        protected static @NotNull Layer of(int range, PlantopiaTemplate template, BlockStateModifier modifier) {
            return new Layer(range, template, modifier);
        }
    }
}
