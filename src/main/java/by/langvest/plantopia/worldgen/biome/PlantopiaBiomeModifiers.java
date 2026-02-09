package by.langvest.plantopia.worldgen.biome;

import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacements;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBiomeModifiers {
    public static void bootstrap(@NotNull BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        PlantopiaPlacements.getDeclarations().forEach((placedFeatureKey, declaration) -> {
            var biomeTagSet = declaration.getBiomeTagSet();

            if(biomeTagSet.isEmpty()) return;

            var placedFeatureName = nameOf(placedFeatureKey);
            var biomeModifierKey = createKey(placedFeatureName);
            var biomeTagKey = PlantopiaBiomeTags.createBiomeHasFeatureTag(placedFeatureName);

            context.register(biomeModifierKey, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(biomeTagKey),
                HolderSet.direct(placedFeatures.getOrThrow(placedFeatureKey)),
                declaration.getGenerationStep()
            ));
        });
    }

    private static @NotNull ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, plantopia(name));
    }
}
