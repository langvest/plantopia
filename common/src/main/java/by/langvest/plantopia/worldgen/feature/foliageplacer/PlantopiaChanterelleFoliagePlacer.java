package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaPrinter;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaIntProportion;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.worldgen.util.PlantopiaTemplate.*;

@ParametersAreNonnullByDefault
public class PlantopiaChanterelleFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaChanterelleFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaIntProportion.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaChanterelleFoliagePlacer::new));

    private final PlantopiaIntProportion height;

    public PlantopiaChanterelleFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaIntProportion height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.CHANTERELLE_FOLIAGE_PLACER.get();
    }

    @Override
    protected PlantopiaPrinter.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper) {
        int radius = context.radius();

        return row -> {
            if (row == 0) return helper.layer(radius, octagonalOutline());
            if (row == 1) return helper.layer(radius / 2, outline());
            return helper.layer(1, cross());
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.sample(random, trunkHeight);
    }
}
