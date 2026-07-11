package by.langvest.plantopia.worldgen.feature.foliageplacer;

import by.langvest.plantopia.worldgen.feature.PlantopiaFoliagePlacerTypes;
import by.langvest.plantopia.worldgen.feature.PlantopiaProportionConfig;
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
public class PlantopiaCypressFoliagePlacer extends FoliagePlacer {
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
        var origin = attachment.pos();

        for (int dy = offset; dy > offset - foliageHeight; dy--) {
            int layerIndex = offset - dy;

            if (layerIndex == 0 || layerIndex == 1) {
                placeLeavesRow(level, blockSetter, random, config, origin, 0, dy, attachment.doubleTrunk());
                continue;
            }

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
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return height.getClampedValue(random, trunkHeight);
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        if (range > 0) {
            if (localY < 0) {
                if (range == 1) {
                    return false;
                }

                if (localX == range && localZ == range) {
                    return true;
                }

                return (localX == range || localZ == range) && random.nextInt(6) == 0;
            }

            return localX == range && localZ == range;
        }

        return false;
    }
}
