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
public class PlantopiaCypressFoliagePlacer extends PlantopiaFoliagePlacer {
    public static final Codec<PlantopiaCypressFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        PlantopiaProportionConfig.CODEC.fieldOf("height").forGetter(it -> it.height)
    ).apply(instance, PlantopiaCypressFoliagePlacer::new));

    private final PlantopiaProportionConfig height;

    public PlantopiaCypressFoliagePlacer(IntProvider radius, IntProvider offset, PlantopiaProportionConfig height) {
        super(radius, offset);
        this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.CYPRESS_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        boolean isTiny = foliageRadius == 1;

        for (int dy = offset; dy > offset - foliageHeight; dy--) {
            int layerIndex = offset - dy;

            if (layerIndex == 0 || layerIndex == 1) {
                placeRow(level, blockSetter, random, config, attachment, 0, dy, square());
                continue;
            }

            if (layerIndex == 2 || layerIndex == foliageHeight - 1) {
                placeRow(level, blockSetter, random, config, attachment, 1, dy, cross());
                continue;
            }

            if (layerIndex == 3) {
                placeRow(level, blockSetter, random, config, attachment, 1, dy, anyOf(noCorner(), withChance(isTiny ? 0.3F : 0.4F)));
                continue;
            }

            if (layerIndex == 4 || isTiny) {
                placeRow(level, blockSetter, random, config, attachment, 1, dy, square());
                continue;
            }

            if (layerIndex == 5 || layerIndex == foliageHeight - 2) {
                placeRow(level, blockSetter, random, config, attachment, foliageRadius, dy, anyOf(cross(), square(0.5F)));
                continue;
            }

            placeRow(level, blockSetter, random, config, attachment, foliageRadius, dy, allOf(noCorner(), anyOf(noOutline(), withChance(0.75F))));
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.getClampedValue(random, trunkHeight);
    }
}
