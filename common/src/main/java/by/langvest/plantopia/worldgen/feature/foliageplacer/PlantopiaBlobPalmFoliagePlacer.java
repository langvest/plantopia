package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import by.langvest.plantopia.worldgen.util.PlantopiaLayer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.worldgen.util.PlantopiaLayerUtils.filteredByTemplate;
import static by.langvest.plantopia.worldgen.util.PlantopiaLayerUtils.placeHangingLeaves;
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
    protected PlantopiaLayer.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper) {
        int radius = context.radius();

        return row -> {
            if (row == 0) return helper.layer(1, anyOf(noCorner(), withChance(0.4F)));
            if (row == 1) return helper.layer(radius, allOf(noCorner(), anyOf(not(allOf(outline(), corner(2))), withChance(0.4F))));
            if (row == 2) return helper.layer(radius, noCorner(), filteredByTemplate(outline(), placeHangingLeaves(0.5333334F)));
            if (row == 3) return helper.layer(1, square());
            return helper.empty();
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 4;
    }
}
