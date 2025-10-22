package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaBiomeTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
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
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaBiomeTagProvider extends BiomeTagsProvider implements PlantopiaTagProvider<Biome> {
    private final Map<TagKey<Biome>, PlantopiaTagSet<Biome>> byTagKeys = Maps.newHashMap();

	private final PlantopiaTagSet<Biome> HAS_COBBLESTONE_SHARD = createTagSet(PlantopiaBiomeTags.HAS_COBBLESTONE_SHARD);
	private final PlantopiaTagSet<Biome> HAS_COBBLESTONE_SHARD_IN_WATER = createTagSet(PlantopiaBiomeTags.HAS_COBBLESTONE_SHARD_IN_WATER);
	private final PlantopiaTagSet<Biome> HAS_MOSSY_COBBLESTONE_SHARD = createTagSet(PlantopiaBiomeTags.HAS_MOSSY_COBBLESTONE_SHARD);
	private final PlantopiaTagSet<Biome> HAS_MOSSY_COBBLESTONE_SHARD_2 = createTagSet(PlantopiaBiomeTags.HAS_MOSSY_COBBLESTONE_SHARD_2);
	private final PlantopiaTagSet<Biome> HAS_MOSSY_COBBLESTONE_SHARD_IN_WATER = createTagSet(PlantopiaBiomeTags.HAS_MOSSY_COBBLESTONE_SHARD_IN_WATER);
	private final PlantopiaTagSet<Biome> HAS_TINY_CACTUS = createTagSet(PlantopiaBiomeTags.HAS_TINY_CACTUS);
	private final PlantopiaTagSet<Biome> HAS_FIREWEED = createTagSet(PlantopiaBiomeTags.HAS_FIREWEED);

	public PlantopiaBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		HAS_COBBLESTONE_SHARD.add(Biomes.PLAINS).addTag(BiomeTags.IS_FOREST, BiomeTags.IS_HILL, BiomeTags.IS_JUNGLE, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_SAVANNA);
		HAS_MOSSY_COBBLESTONE_SHARD.add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP).addTag(BiomeTags.IS_JUNGLE);
		HAS_MOSSY_COBBLESTONE_SHARD_2.add(Biomes.OLD_GROWTH_PINE_TAIGA).addTag(BiomeTags.IS_DEEP_OCEAN);
		HAS_COBBLESTONE_SHARD_IN_WATER.addTag(BiomeTags.IS_RIVER, BiomeTags.IS_OCEAN);
		HAS_MOSSY_COBBLESTONE_SHARD_IN_WATER.add(Biomes.RIVER).addTag(BiomeTags.IS_OCEAN);
		HAS_TINY_CACTUS.add(Biomes.DESERT).addTag(BiomeTags.IS_BADLANDS);
		HAS_FIREWEED.add(Biomes.PLAINS, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST).addTag(BiomeTags.IS_MOUNTAIN, BiomeTags.IS_HILL);

		saveAll();
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
