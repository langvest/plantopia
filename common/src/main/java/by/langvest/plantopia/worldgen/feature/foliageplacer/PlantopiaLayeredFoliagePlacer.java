package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
            placeRow(level, blockSetter, random, config, attachment, dy, layer.range(), layer.template());
        }
    }

    protected abstract LayerProvider getLayerProvider(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset);

    @FunctionalInterface
    public interface LayerProvider {
        Layer provide(int layerIndex);
    }

    public record Layer(
        int range,
        PlantopiaTemplate template
    ) {
        @Contract("_, _ -> new")
        public static @NotNull Layer of(int range, PlantopiaTemplate template) {
            return new Layer(range, template);
        }
    }
}
