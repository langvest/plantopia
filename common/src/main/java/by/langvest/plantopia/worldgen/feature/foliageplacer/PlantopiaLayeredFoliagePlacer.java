package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaLayer;
import by.langvest.plantopia.worldgen.util.PlantopiaLayerUtils;
import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.util.valueproviders.IntProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public abstract class PlantopiaLayeredFoliagePlacer extends PlantopiaFoliagePlacer {
    public PlantopiaLayeredFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected void createFoliage(PlaceContext context) {
        var layerHelper = getLayerHelper(context);
        var layerProvider = getLayerProvider(context, layerHelper);
        PlantopiaLayer.printBox(context.ceiling(), context.random(), context.height(), layerProvider);
    }

    protected abstract PlantopiaLayer.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper);

    protected LayerHelper getLayerHelper(PlaceContext context) {
        return new LayerHelper(context);
    }

    protected record LayerHelper(PlaceContext context) {
        @Contract("_, _, _ -> new")
        protected @NotNull PlantopiaLayer layer(int range, PlantopiaTemplate template, PlantopiaLayerUtils.LayerModifier... modifiers) {
            return new PlantopiaLayer(
                range,
                context.hasDoubleTrunk(),
                PlantopiaLayerUtils.templateFilter(template),
                PlantopiaLayerUtils.pipelinePlacer(context::getFoliageState, context::tryPlaceLeaf, List.of(modifiers))
            );
        }

        @Contract(" -> new")
        protected @NotNull PlantopiaLayer empty() {
            return new PlantopiaLayer(
                -1,
                context.hasDoubleTrunk(),
                PlantopiaLayerUtils::never,
                PlantopiaLayerUtils::never
            );
        }
    }
}
