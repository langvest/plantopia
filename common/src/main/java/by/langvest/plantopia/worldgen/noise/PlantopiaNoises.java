package by.langvest.plantopia.worldgen.noise;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public final class PlantopiaNoises {
    public static final ResourceKey<NormalNoise.NoiseParameters> WEIGHTED = createKey("weighted");
    public static final ResourceKey<NormalNoise.NoiseParameters> MARSH = createKey("marsh");

    protected static @NotNull ResourceKey<NormalNoise.NoiseParameters> createKey(String name) {
        return ResourceKey.create(Registries.NOISE, plantopia(name));
    }
}
