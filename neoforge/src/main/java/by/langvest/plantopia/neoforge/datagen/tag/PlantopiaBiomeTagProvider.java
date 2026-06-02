package by.langvest.plantopia.neoforge.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.worldgen.biome.catalog.PlantopiaOverworldBiomes;
import by.langvest.plantopia.worldgen.placement.catalog.PlantopiaPlacements;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaBiomeTagProvider extends BiomeTagsProvider implements PlantopiaTagProvider<Biome> {
    private static final Map<TagKey<Biome>, PlantopiaTagSet<Biome>> byTagKeys = Maps.newHashMap();

    public static final PlantopiaTagSet<Biome> IS_OVERWORLD = getOrCreateTagSet(BiomeTags.IS_OVERWORLD);
    public static final PlantopiaTagSet<Biome> ALLOWS_QUAGMIRE = getOrCreateTagSet(PlantopiaBiomeTags.ALLOWS_QUAGMIRE);
    public static final PlantopiaTagSet<Biome> ALLOWS_FRAZIL = getOrCreateTagSet(PlantopiaBiomeTags.ALLOWS_FRAZIL);
    public static final PlantopiaTagSet<Biome> IS_MARSH = getOrCreateTagSet(PlantopiaBiomeTags.IS_MARSH);
    public static final PlantopiaTagSet<Biome> IS_QUICKSAND_PRECIPITABLE = getOrCreateTagSet(PlantopiaBiomeTags.IS_QUICKSAND_PRECIPITABLE);

    public PlantopiaBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup, ExistingFileHelper existingFileHelper) {
        super(output, registryLookup, Plantopia.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider registryLookup) {
        generateAll();

        ALLOWS_QUAGMIRE.add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
        ALLOWS_FRAZIL.add(Biomes.FROZEN_OCEAN);
        IS_MARSH.add(PlantopiaOverworldBiomes.MARSH, PlantopiaOverworldBiomes.DEAD_MARSH);
        IS_QUICKSAND_PRECIPITABLE.add(Biomes.DESERT);

        saveAll();
    }

    private void generateAll() {
        PlantopiaPlacements.DECLARATION.forEach((placedFeatureKey, declaration) -> {
            var biomeTagSet = declaration.getBiomeTagSet();

            if (biomeTagSet.isEmpty()) return;

            var placedFeatureName = nameOf(placedFeatureKey);
            var biomeTagKey = PlantopiaBiomeTags.createBiomeHasFeatureTag(placedFeatureName);

            byTagKeys.put(biomeTagKey, biomeTagSet);
        });

        PlantopiaOverworldBiomes.DECLARATION.forEach((biomeKey, declaration) -> {
            IS_OVERWORLD.add(biomeKey);
        });
    }

    private void saveAll() {
        saveByTagKeys(byTagKeys);
    }

    public static @NotNull PlantopiaTagSet<Biome> getOrCreateTagSet(TagKey<Biome> key) {
        return byTagKeys.computeIfAbsent(key, k -> PlantopiaTagSet.newTagSet());
    }

    @Override
    public @NotNull TagAppender<Biome> getTagAppender(TagKey<Biome> key) {
        return tag(key);
    }

    @Override
    public ResourceKey<? extends Registry<Biome>> getRegistryKey() {
        return Registries.BIOME;
    }
}
