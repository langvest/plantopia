package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
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
public class PlantopiaBlobPalmFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaBlobPalmFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).apply(instance, PlantopiaBlobPalmFoliagePlacer::new));

    public PlantopiaBlobPalmFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.BLOB_PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected LayerProvider getLayerProvider(PlaceContext context) {
        int radius = context.radius();

        return layerIndex -> {
            if (layerIndex == 0) return Layer.of(1, anyOf(noCorner(), withChance(0.4F)));
            if (layerIndex == 1) return Layer.of(radius, allOf(noCorner(), anyOf(not(allOf(outline(), corner(2))), withChance(0.4F))));
            if (layerIndex == 2) return Layer.of(radius, noCorner(), filteredByTemplate(outline(), placeHangingLeaves(0.5333334F)));
            if (layerIndex == 3) return Layer.of(1, square());
            return Layer.empty();
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 4;
    }
}
