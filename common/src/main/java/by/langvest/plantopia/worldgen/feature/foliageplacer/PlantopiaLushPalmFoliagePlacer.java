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

import static by.langvest.plantopia.worldgen.util.PlantopiaTemplate.*;

@ParametersAreNonnullByDefault
public class PlantopiaLushPalmFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaLushPalmFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).apply(instance, PlantopiaLushPalmFoliagePlacer::new));

    public PlantopiaLushPalmFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.LUSH_PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected PlantopiaLayer.LayerProvider getLayerProvider(PlaceContext context, LayerHelper helper) {
        int radius = context.radius();

        return row -> {
            if (row == 0) return helper.layer(1, corner());
            if (row == 1) return helper.layer(radius, anyOf(center(), corner(2)));
            if (row == 2) return helper.layer(radius, anyOf(center(1), corner()));
            if (row == 3) return helper.layer(radius, cross());
            if (row == 4) return helper.layer(radius, allOf(cross(), outline()));
            return helper.empty();
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 5;
    }
}
