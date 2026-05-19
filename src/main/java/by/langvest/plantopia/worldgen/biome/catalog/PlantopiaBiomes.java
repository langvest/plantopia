package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBiomes {
    public static final String DEAD = "dead";
    public static final String SEASONAL = "seasonal";

    public static final Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaOverworldBiomes.DECLARATION
    ));

    public static void bootstrap(BootstapContext<Biome> context) {
        DECLARATION.forEach((key, declaration) -> {
            var biome = declaration.getBiome(context);

            context.register(key, biome);
        });
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
