package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.util.valueproviders.IntProvider;
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
    protected void createFoliage(PlaceContext context) {
        int offset = context.offset();
        int height = context.height();
        var layerProvider = getLayerProvider(context);

        for (int dy = offset; dy > offset - height; dy--) {
            int layerIndex = offset - dy;
            var layer = layerProvider.provide(layerIndex);
            placeRow(context, dy, layer.range(), layer.template(), layer.modifier());
        }
    }

    protected abstract LayerProvider getLayerProvider(PlaceContext context);

    @FunctionalInterface
    protected interface LayerProvider {
        Layer provide(int layerIndex);
    }

    protected record Layer(
        int range,
        @Nullable PlantopiaTemplate template,
        @Nullable LeafModifier modifier
    ) {
        @Contract(" -> new")
        protected static @NotNull Layer empty() {
            return new Layer(-1, null, null);
        }

        @Contract("_, _ -> new")
        protected static @NotNull Layer of(int range, PlantopiaTemplate template) {
            return new Layer(range, template, null);
        }

        @Contract("_, _, _ -> new")
        protected static @NotNull Layer of(int range, PlantopiaTemplate template, LeafModifier modifier) {
            return new Layer(range, template, modifier);
        }
    }
}
