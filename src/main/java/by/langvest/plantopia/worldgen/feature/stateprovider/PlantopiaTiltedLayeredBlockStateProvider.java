package by.langvest.plantopia.worldgen.feature.stateprovider;

import by.langvest.plantopia.worldgen.feature.PlantopiaBlockStateProviderTypes;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlantopiaTiltedLayeredBlockStateProvider extends BlockStateProvider {
    public static final Codec<PlantopiaTiltedLayeredBlockStateProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Layer.CODEC.listOf().fieldOf("layers").forGetter(it -> it.layers),
        Codec.FLOAT.fieldOf("angle").orElse(0.0F).forGetter(it -> it.angleDeg),
        Codec.FLOAT.fieldOf("rotation").orElse(0.0F).forGetter(it -> it.rotationDeg)
    ).apply(instance, PlantopiaTiltedLayeredBlockStateProvider::new));

    private final List<Layer> layers;
    private final float angleDeg;
    private final float rotationDeg;

    private final double nx;
    private final double ny;
    private final double nz;
    private final int totalHeight;

    public PlantopiaTiltedLayeredBlockStateProvider(@NotNull List<Layer> layers, float angleDeg, float rotationDeg) {
        if (layers.isEmpty()) {
            throw new IllegalArgumentException("PlantopiaTiltedLayeredBlockStateProvider requires at least one layer");
        }

        this.layers = List.copyOf(layers);
        this.angleDeg = angleDeg;
        this.rotationDeg = rotationDeg;

        int sum = 0;
        for (Layer layer : layers) {
            if (layer.height() <= 0) {
                throw new IllegalArgumentException("Layer height must be > 0");
            }
            sum += layer.height();
        }
        this.totalHeight = sum;

        double angleRad = Math.toRadians(angleDeg);
        double rotRad = Math.toRadians(rotationDeg);

        this.nx = Math.sin(angleRad) * Math.cos(rotRad);
        this.ny = Math.cos(angleRad);
        this.nz = Math.sin(angleRad) * Math.sin(rotRad);
    }

    @Contract(" -> new")
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @Override
    protected @NotNull BlockStateProviderType<?> type() {
        return PlantopiaBlockStateProviderTypes.TILTED_LAYERED_PROVIDER.get();
    }

    @Override
    public @NotNull BlockState getState(@NotNull RandomSource random, @NotNull BlockPos pos) {
        double h = pos.getX() * nx + pos.getY() * ny + pos.getZ() * nz;

        int hInt = Mth.floor(h);
        int local = Math.floorMod(hInt, totalHeight);

        int acc = 0;
        for (Layer layer : layers) {
            acc += layer.height();
            if (local < acc) {
                return layer.provider().getState(random, pos);
            }
        }

        // Fallback case.
        return layers.get(layers.size() - 1).provider().getState(random, pos);
    }

    public record Layer(int height, BlockStateProvider provider) {
        public static final Codec<Layer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(Layer::height),
            BlockStateProvider.CODEC.fieldOf("provider").forGetter(Layer::provider)
        ).apply(instance, Layer::new));
    }

    public static class Builder {
        private final List<Layer> layers = Lists.newArrayList();
        private float angleDeg = 0F;
        private float rotationDeg = 0F;

        public Builder() {}

        public Builder addLayer(int height, BlockStateProvider provider) {
            if (height <= 0) {
                throw new IllegalArgumentException("Layer height must be > 0");
            }
            if (provider == null) {
                throw new IllegalArgumentException("Layer provider cannot be null");
            }
            layers.add(new Layer(height, provider));
            return this;
        }

        public Builder angle(float angleDeg) {
            this.angleDeg = angleDeg;
            return this;
        }

        public Builder rotation(float rotationDeg) {
            this.rotationDeg = rotationDeg;
            return this;
        }

        public PlantopiaTiltedLayeredBlockStateProvider build() {
            if (layers.isEmpty()) {
                throw new IllegalStateException("Cannot build provider: no layers added");
            }
            return new PlantopiaTiltedLayeredBlockStateProvider(layers, angleDeg, rotationDeg);
        }
    }
}
