package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaEntityTypeTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlantopiaEntityTypeTagProvider extends EntityTypeTagsProvider implements PlantopiaTagProvider<EntityType<?>> {
    private static final Map<TagKey<EntityType<?>>, PlantopiaTagSet<EntityType<?>>> byTagKeys = Maps.newHashMap();

	public static final PlantopiaTagSet<EntityType<?>> QUICKSAND_WALKABLE_MOBS = getOrCreateTagSet(PlantopiaEntityTypeTags.QUICKSAND_WALKABLE_MOBS);
	private final PlantopiaTagSet<EntityType<?>> QUICKSAND_IMMUNE_ENTITY_TYPES = getOrCreateTagSet(PlantopiaEntityTypeTags.QUICKSAND_IMMUNE_ENTITY_TYPES);

	public PlantopiaEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		QUICKSAND_WALKABLE_MOBS.add(EntityType.RABBIT, EntityType.ENDERMITE, EntityType.SILVERFISH);
		QUICKSAND_IMMUNE_ENTITY_TYPES.add(EntityType.HUSK, EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.WITHER_SKELETON, EntityType.IRON_GOLEM, EntityType.WITHER, EntityType.SILVERFISH, EntityType.ENDERMITE);

		saveAll();
	}

	private void saveAll() {
		saveByTagKeys(byTagKeys);
	}

    public static @NotNull PlantopiaTagSet<EntityType<?>> getOrCreateTagSet(TagKey<EntityType<?>> key) {
        return byTagKeys.computeIfAbsent(key, k -> PlantopiaTagSet.newTagSet());
    }

	@Override
	public @NotNull IntrinsicTagAppender<EntityType<?>> getTagAppender(TagKey<EntityType<?>> key) {
		return tag(key);
	}

	@Override
	public ResourceKey<? extends Registry<EntityType<?>>> getRegistryKey() {
		return Registries.ENTITY_TYPE;
	}
}
