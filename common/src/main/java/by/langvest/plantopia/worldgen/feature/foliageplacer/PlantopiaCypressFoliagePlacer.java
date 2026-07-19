package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import by.langvest.plantopia.worldgen.util.intproportion.PlantopiaRelativeIntProportion;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.worldgen.util.PlantopiaTemplate.*;

@ParametersAreNonnullByDefault
public class PlantopiaCypressFoliagePlacer extends PlantopiaLayeredFoliagePlacer {
    public static final Codec<PlantopiaCypressFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance)
        .and(PlantopiaRelativeIntProportion.CODEC.fieldOf("height").forGetter(it -> it.height))
        .and(IntProvider.CODEC.fieldOf("tip_step").forGetter(it -> it.tipStep))
        .apply(instance, PlantopiaCypressFoliagePlacer::new)
    );

    private final PlantopiaRelativeIntProportion height;
    private final IntProvider tipStep;

    public PlantopiaCypressFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaRelativeIntProportion height, IntProvider tipStep) {
        super(radius, offset);
        this.height = height;
        this.tipStep = tipStep;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.CYPRESS_FOLIAGE_PLACER.get();
    }

    @Override
    protected LayerProvider getLayerProvider(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        boolean isThin = foliageRadius == 1;
        int tipStep = this.tipStep.sample(random);
        int tipHeight = tipStep * 2;

        return layerIndex -> {
            if (layerIndex < tipStep) return Layer.of(0, square());
            if (layerIndex < tipHeight) return Layer.of(1, layerIndex == tipHeight - 1 ? anyOf(noCorner(), withChance(isThin ? 0.25333334F : 0.4F)) : cross());
            if (layerIndex == foliageHeight - 1) return Layer.of(1, cross());
            if (layerIndex == tipHeight || isThin) return Layer.of(1, square());
            if (layerIndex == tipHeight + 1 || layerIndex == foliageHeight - 2) return Layer.of(foliageRadius, anyOf(cross(), square(0.5F)));
            return Layer.of(foliageRadius, allOf(noCorner(), anyOf(noOutline(), withChance(0.75F))));
        };
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.sample(random, trunkHeight);
    }
}
