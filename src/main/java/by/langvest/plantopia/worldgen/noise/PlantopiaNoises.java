package by.langvest.plantopia.worldgen.noise;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaNoises {
    private static final Map<ResourceKey<NormalNoise.NoiseParameters>, PlantopiaNoiseDeclaration> declarations = Maps.newHashMap();

    public static @NotNull Map<ResourceKey<NormalNoise.NoiseParameters>, PlantopiaNoiseDeclaration> getDeclarations() {
        return declarations;
    }

    private static @NotNull ResourceKey<NormalNoise.NoiseParameters> declareNoise(String name, PlantopiaNoiseDeclaration.@NotNull Builder builder) {
        var key = createKey(name);
        declarations.put(key, builder.build());
        return key;
    }

    public static final ResourceKey<NormalNoise.NoiseParameters> WEIGHTED = declareNoise(
        "weighted",
        PlantopiaNoiseDeclaration.builder()
            .noise(context -> new NormalNoise.NoiseParameters(0, 1.0))
    );

    public static final ResourceKey<NormalNoise.NoiseParameters> MARSH = declareNoise(
        "marsh",
        PlantopiaNoiseDeclaration.builder()
            .noise(context -> new NormalNoise.NoiseParameters(-1, 1.0))
    );

    /* BOOTSTRAP ******************************************************************************************************/

    public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> context) {
        getDeclarations().forEach((key, declaration) -> {
            var noise = declaration.getNoise(context);

            context.register(key, noise);
        });
    }

    protected static @NotNull ResourceKey<NormalNoise.NoiseParameters> createKey(String name) {
        return ResourceKey.create(Registries.NOISE, plantopia(name));
    }
}
