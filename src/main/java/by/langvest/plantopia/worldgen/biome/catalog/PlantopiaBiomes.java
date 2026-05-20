package by.langvest.plantopia.worldgen.biome.catalog;

import by.langvest.plantopia.worldgen.biome.PlantopiaBiomeDeclaration;
import by.langvest.toolkit.util.Catalog;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public final class PlantopiaBiomes {
    public static final Catalog<ResourceKey<Biome>, PlantopiaBiomeDeclaration> DECLARATION = Catalog.newCatalog(catalog -> Catalog.merge(
        PlantopiaOverworldBiomes.DECLARATION
    ));

    public static void bootstrap(BootstapContext<Biome> context) {
        DECLARATION.forEach((key, declaration) -> {
            var biome = declaration.getBiome(context);

            context.register(key, biome);
        });
    }
}
