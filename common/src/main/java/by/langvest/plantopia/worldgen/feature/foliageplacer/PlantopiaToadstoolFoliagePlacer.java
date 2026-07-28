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

import static by.langvest.plantopia.worldgen.util.PlantopiaTemplate.cross;
import static by.langvest.plantopia.worldgen.util.PlantopiaTemplate.square;

@ParametersAreNonnullByDefault
public class PlantopiaToadstoolFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaToadstoolFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaIntProportion.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaToadstoolFoliagePlacer::new));

    private final PlantopiaIntProportion height;

    public PlantopiaToadstoolFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaIntProportion height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.TOADSTOOL_FOLIAGE_PLACER.get();
    }

    @Override
    protected PlantopiaPrinter.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper) {
        int radius = context.radius();

        return row -> {
            if (row == 0) return helper.layer(radius, cross());
            return helper.layer(radius, square());
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.sample(random, trunkHeight);
    }
}
