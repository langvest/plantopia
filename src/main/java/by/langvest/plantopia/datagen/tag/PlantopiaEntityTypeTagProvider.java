package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaEntityTypeTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import com.google.common.collect.Maps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaEntityTypeTagProvider extends EntityTypeTagsProvider implements PlantopiaTagProvider<EntityType<?>> {
    private final Map<TagKey<EntityType<?>>, PlantopiaTagSet<EntityType<?>>> tagSets = Maps.newHashMap();

	private final PlantopiaTagSet<EntityType<?>> QUICKSAND_WALKABLE_MOBS = createTagSet(PlantopiaEntityTypeTags.QUICKSAND_WALKABLE_MOBS);
	private final PlantopiaTagSet<EntityType<?>> QUICKSAND_IMMUNE_ENTITY_TYPES = createTagSet(PlantopiaEntityTypeTags.QUICKSAND_IMMUNE_ENTITY_TYPES);

	public PlantopiaEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		add(QUICKSAND_WALKABLE_MOBS, EntityType.RABBIT, EntityType.ENDERMITE, EntityType.SILVERFISH);
		add(QUICKSAND_IMMUNE_ENTITY_TYPES, EntityType.HUSK, EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.WITHER_SKELETON, EntityType.IRON_GOLEM, EntityType.WITHER, EntityType.SILVERFISH, EntityType.ENDERMITE);

		saveAll();
	}

	private void add(@NotNull PlantopiaTagSet<EntityType<?>> tagSet, EntityType<?>... entities) {
		tagSet.add(Arrays.stream(entities).toArray(EntityType[]::new));
	}

	@SafeVarargs
	@SuppressWarnings("unused")
	private void add(@NotNull PlantopiaTagSet<EntityType<?>> tagSet, TagKey<EntityType<?>>... tags) {
		tagSet.addTags(tags);
	}

	private void saveAll() {
		tagSets.forEach(this::save);
	}

    private @NotNull PlantopiaTagSet<EntityType<?>> createTagSet(TagKey<EntityType<?>> key) {
        PlantopiaTagSet<EntityType<?>> tagSet = PlantopiaTagSet.newTagSet();
        this.tagSets.put(key, tagSet);
        return tagSet;
    }

	@Override
	public @NotNull IntrinsicTagAppender<EntityType<?>> getTagAppender(TagKey<EntityType<?>> key) {
		return tag(key);
	}
}
