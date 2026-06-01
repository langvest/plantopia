package by.langvest.plantopia.worldgen.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public final class PlantopiaBiomeUtils {
    /* KEY *************************************************************************************************/

    public static @NotNull ResourceKey<Biome> createKey(String name) {
        return ResourceKey.create(Registries.BIOME, plantopia(name));
    }

    /* MATH *************************************************************************************************/

    public static int calculateSkyColor(float temperature) {
        float temperatureFactor = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - temperatureFactor * 0.05F, 0.5F + temperatureFactor * 0.1F, 1.0F);
    }
}
