package by.langvest.plantopia.worldgen.biome;

import com.google.common.collect.Maps;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBiomes {
    public static void bootstrap(BootstapContext<Biome> context) {
        getDeclarations().forEach((key, declaration) -> {
            var biome = declaration.getBiome(context);

            context.register(key, biome);
        });
    }

    public static @NotNull Map<ResourceKey<Biome>, PlantopiaBiomeDeclaration> getDeclarations() {
        Map<ResourceKey<Biome>, PlantopiaBiomeDeclaration> result = Maps.newHashMap();

        result.putAll(PlantopiaOverworldBiomes.getDeclarations());

        return result;
    }

    protected static @NotNull ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, plantopia(name));
    }

    /* HELPER METHODS *************************************************************************************************/

    public static int calculateSkyColor(float temperature) {
        float temperatureFactor = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - temperatureFactor * 0.05F, 0.5F + temperatureFactor * 0.1F, 1.0F);
    }
}
