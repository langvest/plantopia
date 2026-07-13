package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaProportionConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaLushFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaLushFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaProportionConfig.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaLushFoliagePlacer::new));

    private final PlantopiaProportionConfig height;

    public PlantopiaLushFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaProportionConfig height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.LUSH_FOLIAGE_PLACER.get();
    }

    @Override
    protected LayerProvider getLayerProvider(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        return layerIndex -> {
            if (layerIndex == 0 || layerIndex == 1) return Layer.row(0, square());
            if (layerIndex == 2) return Layer.row(1, square());
            int patternIndex = layerIndex - 3;
            boolean isCross = patternIndex % 2 == 0;
            if (isCross) return Layer.row(foliageRadius / 2, noCorner());
            return Layer.row(foliageRadius, noCorner());
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.getClampedValue(random, trunkHeight, h -> h % 2 == 0 ? h : h - 1);
    }
}
