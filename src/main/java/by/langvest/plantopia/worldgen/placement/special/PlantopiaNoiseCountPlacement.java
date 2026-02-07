package by.langvest.plantopia.worldgen.placement.special;

import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseActivationType;
import by.langvest.plantopia.worldgen.placement.PlantopiaNoiseConfig;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementModifierTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaNoiseCountPlacement extends RepeatingPlacement {
    public static final Codec<PlantopiaNoiseCountPlacement> CODEC = RecordCodecBuilder.create((p_191761_) -> p_191761_.group(
        PlantopiaNoiseConfig.CODEC.fieldOf("noise_config").forGetter((p_1917617_) -> p_1917617_.noiseConfig),
        PlantopiaNoiseActivationType.CODEC.fieldOf("activation").forGetter((p_1917617_) -> p_1917617_.activationType),
        Codec.FLOAT.fieldOf("noise_level").forGetter((p_191771_) -> p_191771_.noiseLevel),
        IntProvider.codec(0, 256).fieldOf("active_count").forGetter((p_191769_) -> p_191769_.activeCount),
        IntProvider.codec(0, 256).fieldOf("passive_count").forGetter((p_191763_) -> p_191763_.passiveCount)
    ).apply(p_191761_, PlantopiaNoiseCountPlacement::new));

    private final PlantopiaNoiseConfig noiseConfig;
    private final PlantopiaNoiseActivationType activationType;
    private final float noiseLevel;
    private final IntProvider activeCount;
    private final IntProvider passiveCount;

    private PlantopiaNoiseCountPlacement(PlantopiaNoiseConfig noiseConfig, PlantopiaNoiseActivationType activationType, float noiseLevel, IntProvider activeCount, IntProvider passiveCount) {
        this.noiseConfig = noiseConfig;
        this.activationType = activationType;
        this.noiseLevel = noiseLevel;
        this.activeCount = activeCount;
        this.passiveCount = passiveCount;
    }

    public static @NotNull PlantopiaNoiseCountPlacement belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, int count) {
        return belowLevel(noiseConfig, noiseLevel, count, 0);
    }

    public static @NotNull PlantopiaNoiseCountPlacement belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, IntProvider count) {
        return belowLevel(noiseConfig, noiseLevel, count, ConstantInt.of(0));
    }

    public static @NotNull PlantopiaNoiseCountPlacement belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, int activeCount, int passiveCount) {
        return belowLevel(noiseConfig, noiseLevel, ConstantInt.of(activeCount), ConstantInt.of(passiveCount));
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull PlantopiaNoiseCountPlacement belowLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, IntProvider activeCount, IntProvider passiveCount) {
        return new PlantopiaNoiseCountPlacement(noiseConfig, PlantopiaNoiseActivationType.BELOW, noiseLevel, activeCount, passiveCount);
    }

    public static @NotNull PlantopiaNoiseCountPlacement aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, int count) {
        return aboveLevel(noiseConfig, noiseLevel, count, 0);
    }

    public static @NotNull PlantopiaNoiseCountPlacement aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, IntProvider count) {
        return aboveLevel(noiseConfig, noiseLevel, count, ConstantInt.of(0));
    }

    public static @NotNull PlantopiaNoiseCountPlacement aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, int activeCount, int passiveCount) {
        return aboveLevel(noiseConfig, noiseLevel, ConstantInt.of(activeCount), ConstantInt.of(passiveCount));
    }

    @Contract("_, _, _, _ -> new")
    public static @NotNull PlantopiaNoiseCountPlacement aboveLevel(PlantopiaNoiseConfig noiseConfig, float noiseLevel, IntProvider activeCount, IntProvider passiveCount) {
        return new PlantopiaNoiseCountPlacement(noiseConfig, PlantopiaNoiseActivationType.ABOVE, noiseLevel, activeCount, passiveCount);
    }

    @Override
    protected int count(@NotNull RandomSource random, @NotNull BlockPos pos) {
        return shouldActivateChunk(pos) ? activeCount.sample(random) : passiveCount.sample(random);
    }

    private boolean shouldActivateChunk(BlockPos chunkPos) {
        return shouldActivatePos(chunkPos)
            || shouldActivatePos(chunkPos.offset(15, 0, 0))
            || shouldActivatePos(chunkPos.offset(0, 0, 15))
            || shouldActivatePos(chunkPos.offset(15, 0, 15));
    }

    private boolean shouldActivatePos(BlockPos pos) {
        double noiseValue = noiseConfig.getValue(pos);

        if(activationType == PlantopiaNoiseActivationType.BELOW && noiseValue < noiseLevel) return true;
        if(activationType == PlantopiaNoiseActivationType.ABOVE && noiseValue >= noiseLevel) return true;

        return false;
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return PlantopiaPlacementModifierTypes.NOISE_COUNT.get();
    }
}
