package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacements;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaBiomeTagProvider extends BiomeTagsProvider implements PlantopiaTagProvider<Biome> {
    private final Map<TagKey<Biome>, PlantopiaTagSet<Biome>> byTagKeys = Maps.newHashMap();

	private final PlantopiaTagSet<Biome> ALLOWS_QUAGMIRE = createTagSet(PlantopiaBiomeTags.ALLOWS_QUAGMIRE);
	private final PlantopiaTagSet<Biome> ALLOWS_FRAZIL = createTagSet(PlantopiaBiomeTags.ALLOWS_FRAZIL);

	public PlantopiaBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		generateAll();

		ALLOWS_QUAGMIRE.add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
		ALLOWS_FRAZIL.add(Biomes.FROZEN_OCEAN);

		saveAll();
	}

	private void generateAll() {
		PlantopiaPlacements.getDeclarations().forEach((placedFeatureKey, declaration) -> {
			var biomeTagSet = declaration.getBiomeTagSet();

			if(biomeTagSet.isEmpty()) return;

			var placedFeatureName = nameOf(placedFeatureKey);
			var biomeTagKey = PlantopiaBiomeTags.createBiomeHasFeatureTag(placedFeatureName);

			byTagKeys.put(biomeTagKey, biomeTagSet);
		});
	}

	private void saveAll() {
		saveByTagKeys(byTagKeys);
	}

    private @NotNull PlantopiaTagSet<Biome> createTagSet(TagKey<Biome> key) {
        PlantopiaTagSet<Biome> tagSet = PlantopiaTagSet.newTagSet();
        byTagKeys.put(key, tagSet);
        return tagSet;
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
