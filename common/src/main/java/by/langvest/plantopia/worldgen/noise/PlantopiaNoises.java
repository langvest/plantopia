package by.langvest.plantopia.worldgen.noise;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public final class PlantopiaNoises {
    public static final ResourceKey<NormalNoise.NoiseParameters> WEIGHTED = createKey("weighted");
    public static final ResourceKey<NormalNoise.NoiseParameters> MARSH = createKey("marsh");
    public static final ResourceKey<NormalNoise.NoiseParameters> PODZOL = createKey("podzol");
    public static final ResourceKey<NormalNoise.NoiseParameters> ROTTED_DIRT = createKey("rotted_dirt");
    public static final ResourceKey<NormalNoise.NoiseParameters> GRAVEL = createKey("gravel");

    public static @NotNull ResourceKey<NormalNoise.NoiseParameters> createKey(String name) {
        return ResourceKey.create(Registries.NOISE, plantopia(name));
    }
}
