package by.langvest.plantopia.worldgen.biome;

import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.worldgen.placement.PlantopiaVegetationPlacements;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBiomeModifiers {
    public static final ResourceKey<BiomeModifier> PATCH_HOGWEED = createKey("patch_hogweed");
    public static final ResourceKey<BiomeModifier> PATCH_TINY_CACTUS = createKey("patch_tiny_cactus");
    public static final ResourceKey<BiomeModifier> PATCH_FLOWERING_TINY_CACTUS = createKey("patch_flowering_tiny_cactus");
    public static final ResourceKey<BiomeModifier> PATCH_COBBLESTONE_SHARD = createKey("patch_cobblestone_shard");
    public static final ResourceKey<BiomeModifier> PATCH_COBBLESTONE_SHARD_IN_WATER = createKey("patch_cobblestone_shard_in_water");
    public static final ResourceKey<BiomeModifier> PATCH_MOSSY_COBBLESTONE_SHARD = createKey("patch_mossy_cobblestone_shard");
    public static final ResourceKey<BiomeModifier> PATCH_MOSSY_COBBLESTONE_SHARD_2 = createKey("patch_mossy_cobblestone_shard_2");
    public static final ResourceKey<BiomeModifier> PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER = createKey("patch_mossy_cobblestone_shard_in_water");
    public static final ResourceKey<BiomeModifier> PATCH_FIREWEED = createKey("patch_fireweed");

    public static void bootstrap(@NotNull BootstapContext<BiomeModifier> context) {
        var placedFeature = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(PATCH_HOGWEED, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_HOGWEED)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_FIREWEED, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
//            biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_FIREWEED),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_FIREWEED_MOUNTAINS)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_TINY_CACTUS, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_TINY_CACTUS),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_TINY_CACTUS)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_COBBLESTONE_SHARD, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_COBBLESTONE_SHARD),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_COBBLESTONE_SHARD)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_COBBLESTONE_SHARD_IN_WATER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_COBBLESTONE_SHARD_IN_WATER),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_COBBLESTONE_SHARD_IN_WATER)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_MOSSY_COBBLESTONE_SHARD, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_MOSSY_COBBLESTONE_SHARD),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_MOSSY_COBBLESTONE_SHARD)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_MOSSY_COBBLESTONE_SHARD_2, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_MOSSY_COBBLESTONE_SHARD_2),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_MOSSY_COBBLESTONE_SHARD_2)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(PlantopiaBiomeTags.HAS_MOSSY_COBBLESTONE_SHARD_IN_WATER),
            HolderSet.direct(placedFeature.getOrThrow(PlantopiaVegetationPlacements.PATCH_MOSSY_COBBLESTONE_SHARD_IN_WATER)),
            GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }

    private static @NotNull ResourceKey<BiomeModifier> createKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, plantopia(name));
    }
}
