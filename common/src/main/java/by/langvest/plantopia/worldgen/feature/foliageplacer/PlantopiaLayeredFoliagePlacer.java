package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.util.PlantopiaPrinter;
import by.langvest.plantopia.worldgen.util.PlantopiaTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
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
        PlantopiaPrinter.printBox(context.ceiling(), context.random(), context.height(), layerProvider);
    }

    protected abstract PlantopiaPrinter.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper);

    protected LayerHelper getLayerHelper(PlaceContext context) {
        return new LayerHelper(context);
    }

    protected static class LayerHelper {
        protected final PlaceContext context;
        protected final PlantopiaPrinter.LayerContext layerContext;

        protected LayerHelper(PlaceContext context) {
            this.context = context;

            this.layerContext = new PlantopiaPrinter.LayerContext() {
                @Override
                public BlockState getState(BlockPos pos, RandomSource random) {
                    return context.getFoliageState(pos);
                }

                @Override
                public boolean setBlock(BlockPos pos, BlockState state) {
                    return context.tryPlaceLeaf(pos, state);
                }
            };
        }

        protected PlantopiaPrinter.@NotNull Layer layer(int range, PlantopiaTemplate template, PlantopiaPrinter.LayerModifier... modifiers) {
            return new PlantopiaPrinter.Layer(
                range,
                context.hasDoubleTrunk(),
                PlantopiaPrinter.templateFilter(template),
                PlantopiaPrinter.pipelinePlacer(layerContext, List.of(modifiers))
            );
        }

        protected PlantopiaPrinter.@NotNull Layer empty() {
            return new PlantopiaPrinter.Layer(
                -1,
                context.hasDoubleTrunk(),
                PlantopiaPrinter.declineFilter(),
                PlantopiaPrinter.declinePlacer()
            );
        }
    }
}
