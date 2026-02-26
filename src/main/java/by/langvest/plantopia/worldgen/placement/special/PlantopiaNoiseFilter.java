package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaThresholdActivationType;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlantopiaNoiseFilter extends PlacementFilter {
    public static final Codec<PlantopiaNoiseFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlantopiaNoiseConfig.CODEC.fieldOf("noise_config").forGetter(it -> it.noiseConfig),
        PlantopiaThresholdActivationType.CODEC.fieldOf("activation").forGetter(it -> it.activationType),
        Codec.FLOAT.fieldOf("noise_level").forGetter(it -> it.noiseLevel),
        FloatProvider.codec(-1.0F, 1.0F).fieldOf("erosion_level").forGetter(it -> it.erosionLevel)
    ).apply(instance, PlantopiaNoiseFilter::new));

    private final PlantopiaNoiseConfig noiseConfig;
    private final PlantopiaThresholdActivationType activationType;
    private final float noiseLevel;
    private final FloatProvider erosionLevel;

    private PlantopiaNoiseFilter(PlantopiaNoiseConfig noiseConfig, PlantopiaThresholdActivationType activationType, float noiseLevel, FloatProvider erosionLevel) {
        this.noiseConfig = noiseConfig;
        this.activationType = activationType;
        this.noiseLevel = noiseLevel;
        this.erosionLevel = erosionLevel;
    }

    public static @NotNull PlantopiaNoiseFilter aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel) {
        return aboveLevel(noiseConfig, noiseLevel, 0);
    }

    public static @NotNull PlantopiaNoiseFilter aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, float erosion) {
        return aboveLevel(noiseConfig, noiseLevel, getErosionLevel(noiseLevel, erosion));
    }

    public static @NotNull PlantopiaNoiseFilter aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, FloatProvider erosionLevel) {
        return new PlantopiaNoiseFilter(noiseConfig, PlantopiaThresholdActivationType.ABOVE, noiseLevel, erosionLevel);
    }

    public static @NotNull PlantopiaNoiseFilter belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel) {
        return belowLevel(noiseConfig, noiseLevel, 0);
    }

    public static @NotNull PlantopiaNoiseFilter belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, float erosion) {
        return belowLevel(noiseConfig, noiseLevel, getErosionLevel(noiseLevel, erosion));
    }

    public static @NotNull PlantopiaNoiseFilter belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, FloatProvider erosionLevel) {
        return new PlantopiaNoiseFilter(noiseConfig, PlantopiaThresholdActivationType.BELOW, noiseLevel, erosionLevel);
    }

    private static @NotNull FloatProvider getErosionLevel(float noiseLevel, float erosion) {
        erosion = Math.abs(erosion);

        if(erosion == 0.0F) return ConstantFloat.of(noiseLevel);

        float minErosionLevel = Math.max(-1.0F, noiseLevel - erosion);
        float maxErosionLevel = Math.min(1.0F, noiseLevel + erosion);

        return UniformFloat.of(
            new BigDecimal(minErosionLevel).setScale(4, RoundingMode.HALF_UP).floatValue(),
            new BigDecimal(maxErosionLevel).setScale(4, RoundingMode.HALF_UP).floatValue()
        );
    }

    @Override
    public boolean shouldPlace(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        double noiseValue = noiseConfig.getValue(pos);
        float minErosionLevel = erosionLevel.getMinValue();
        float maxErosionLevel = erosionLevel.getMaxValue();

        if(activationType == PlantopiaThresholdActivationType.BELOW) {
            if(noiseValue < noiseLevel) {
                if(noiseLevel == minErosionLevel) return true;
                return !(noiseValue >= minErosionLevel) || !random.nextBoolean();
            } else {
                if(noiseLevel == maxErosionLevel) return false;
                return noiseValue < maxErosionLevel && random.nextBoolean();
            }
        }

        if(activationType == PlantopiaThresholdActivationType.ABOVE) {
            if(noiseValue >= noiseLevel) {
                if(noiseLevel == maxErosionLevel) return true;
                return !(noiseValue < maxErosionLevel) || !random.nextBoolean();
            } else {
                if(noiseLevel == minErosionLevel) return false;
                return noiseValue >= minErosionLevel && random.nextBoolean();
            }
        }

        return false;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.NOISE_FILTER.get();
    }
}
