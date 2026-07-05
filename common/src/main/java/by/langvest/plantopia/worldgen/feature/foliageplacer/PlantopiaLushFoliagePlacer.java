package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaLushFoliagePlacer extends FoliagePlacer {
    public static final Codec<PlantopiaLushFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> foliagePlacerParts(instance).and(
        IntProvider.codec(0, 24).fieldOf("base_height").forGetter(it -> it.baseHeight)
    ).apply(instance, PlantopiaLushFoliagePlacer::new));

    private final IntProvider baseHeight;

    public PlantopiaLushFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider baseHeight) {
        super(radius, offset);
        this.baseHeight = baseHeight;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() {
        return PlantopiaFoliagePlacerTypes.LUSH_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        var origin = attachment.pos();

        for (int dy = offset; dy > offset - foliageHeight; dy--) {
            int layerIndex = offset - dy;

            // Слои 0 и 1: только центр
            if (layerIndex == 0 || layerIndex == 1) {
                placeLeavesRow(level, blockSetter, random, config, origin, 0, dy, attachment.doubleTrunk());
                continue;
            }

            // Слой 2: квадрат 3x3 (радиус 1)
            if (layerIndex == 2) {
                placeLeavesRow(level, blockSetter, random, config, origin, 1, dy, attachment.doubleTrunk());
                continue;
            }

            int patternIndex = layerIndex - 3;
            boolean isCross = patternIndex % 2 == 0;

            if (isCross) {
                placeLeavesRow(level, blockSetter, random, config, origin, radius.sample(random) / 2, dy, attachment.doubleTrunk());
            } else {
                placeLeavesRow(level, blockSetter, random, config, origin, radius.sample(random), dy, attachment.doubleTrunk());
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 4 + baseHeight.sample(random);
        // return ; // Мне нужно чтобы до земли всегда оставалось минимум 3 блока.
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return localX == range && localZ == range && range > 0 && localY < 0;
    }
}
