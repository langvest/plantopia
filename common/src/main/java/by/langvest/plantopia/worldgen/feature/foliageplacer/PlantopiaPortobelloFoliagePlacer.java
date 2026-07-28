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
public class PlantopiaPortobelloFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaPortobelloFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaIntProportion.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaPortobelloFoliagePlacer::new));

    private final PlantopiaIntProportion height;

    public PlantopiaPortobelloFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaIntProportion height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.PORTOBELLO_FOLIAGE_PLACER.get();
    }

    @Override
    protected LayerProvider getLayerProvider(PlaceContext context) {
        int radius = context.radius();

        return layerIndex -> {
            if (layerIndex == 0) return Layer.of(radius - 1, noCorner());
            return Layer.of(radius, octagonalOutline(), revealMushroomInsides());
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.sample(random, trunkHeight);
    }
}
