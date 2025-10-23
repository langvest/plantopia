package by.langvest.plantopia.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaNoiseConfig {
    public static final Codec<PlantopiaNoiseConfig> CODEC = RecordCodecBuilder.create((p_191761_) -> p_191761_.group(
        Codec.DOUBLE.fieldOf("scale").forGetter((p_191771_) -> p_191771_.scale),
        Codec.INT.fieldOf("offset_x").forGetter((p_191769_) -> p_191769_.offsetX),
        Codec.INT.fieldOf("offset_z").forGetter((p_191763_) -> p_191763_.offsetZ)
    ).apply(p_191761_, PlantopiaNoiseConfig::new));

    private final double scale;
    private final int offsetX;
    private final int offsetZ;

    private PlantopiaNoiseConfig(double scale, int offsetX, int offsetZ) {
        this.scale = scale;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    public static @NotNull PlantopiaNoiseConfig of(double scale, int offsetX, int offsetZ) {
        return new PlantopiaNoiseConfig(scale, offsetX, offsetZ);
    }

    public double getScale() {
        return scale;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    @SuppressWarnings("removal")
    public double getValue(@NotNull BlockPos pos) {
        double noiseX = ((double) pos.getX() / 200 / scale) + offsetX;
        double noiseZ = ((double) pos.getZ() / 200 / scale) + offsetZ;

        return Biome.BIOME_INFO_NOISE.getValue(noiseX, noiseZ, false);
    }
}
