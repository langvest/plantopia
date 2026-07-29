package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaLayer;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaRelativeIntProportion;
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
public class PlantopiaLushFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaLushFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaRelativeIntProportion.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaLushFoliagePlacer::new));

    private final PlantopiaRelativeIntProportion height;

    public PlantopiaLushFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaRelativeIntProportion height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.LUSH_FOLIAGE_PLACER.get();
    }

    @Override
    protected PlantopiaLayer.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper) {
        int radius = context.radius();

        return row -> {
            if (row == 0 || row == 1) return helper.layer(0, square());
            if (row == 2) return helper.layer(1, square());
            int patternIndex = row - 3;
            boolean isCross = patternIndex % 2 == 0;
            if (isCross) return helper.layer(radius / 2, noCorner());
            return helper.layer(radius, noCorner());
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.sample(random, trunkHeight, h -> h % 2 == 0 ? h : h - 1);
    }
}
