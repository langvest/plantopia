package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.util.helper.PlantopiaResourceHelper;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

public interface PlantopiaTagProvider<T> {
    IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> getTagAppender(TagKey<T> key);

    default void save(TagKey<T> key, @NotNull PlantopiaTagSet<T> tagSet) {
        if(tagSet.isEmpty()) return;

        var targetTag = getTagAppender(key);

        var tags = tagSet.getTags();
        var values = tagSet.getValues();

        tags.sort(PlantopiaResourceHelper::compareById);
        values.sort(PlantopiaResourceHelper::compareById);

        for(var tag : tags) targetTag.addTag(tag);
        for(var value : values) targetTag.add(value);
    }
}
