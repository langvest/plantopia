package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.tag.PlantopiaEntityTypeTags;
import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.util.helper.PlantopiaResourceHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

public final class PlantopiaEntityTypeTagProvider extends EntityTypeTagsProvider {
	private final PlantopiaTagSet<EntityType<?>> QUICKSAND_WALKABLE_MOBS = PlantopiaTagSet.newTagSet();
	private final PlantopiaTagSet<EntityType<?>> QUICKSAND_IMMUNE_ENTITY_TYPES = PlantopiaTagSet.newTagSet();

	public PlantopiaEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Plantopia.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		add(QUICKSAND_WALKABLE_MOBS, EntityType.HUSK, EntityType.RABBIT);
		add(QUICKSAND_IMMUNE_ENTITY_TYPES, EntityType.HUSK, EntityType.IRON_GOLEM);

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
		save(PlantopiaEntityTypeTags.QUICKSAND_WALKABLE_MOBS, QUICKSAND_WALKABLE_MOBS);
		save(PlantopiaEntityTypeTags.QUICKSAND_IMMUNE_ENTITY_TYPES, QUICKSAND_IMMUNE_ENTITY_TYPES);
	}

	private void save(TagKey<EntityType<?>> key, @NotNull PlantopiaTagSet<EntityType<?>> tagSet) {
		if(tagSet.isEmpty()) return;

		var targetTag = tag(key);

		var tags = tagSet.getTags();
		var entities = tagSet.getElements();

		tags.sort(Comparator.comparing(PlantopiaResourceHelper::idOf));
		entities.sort(Comparator.comparing(PlantopiaResourceHelper::idOf));

		for(var tag : tags) targetTag.addTag(tag);
		for(var entity : entities) targetTag.add(entity);
	}
}