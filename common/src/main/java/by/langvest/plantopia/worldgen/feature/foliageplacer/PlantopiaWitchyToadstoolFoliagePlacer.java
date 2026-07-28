package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
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
public class PlantopiaWitchyToadstoolFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaWitchyToadstoolFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaIntProportion.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaWitchyToadstoolFoliagePlacer::new));

    private final PlantopiaIntProportion height;

    public PlantopiaWitchyToadstoolFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaIntProportion height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.WITCHY_TOADSTOOL_FOLIAGE_PLACER.get();
    }

    @Override
    protected LayerProvider getLayerProvider(PlaceContext context) {
        int radius = context.radius();

        return layerIndex -> {
            if (layerIndex <= 1) return Layer.of(0, square());
            if (layerIndex <= 3) return Layer.of(radius, cross());
            return Layer.of(radius, square());
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.sample(random, trunkHeight);
    }
}
