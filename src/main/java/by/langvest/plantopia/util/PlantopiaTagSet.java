package by.langvest.plantopia.util;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressWarnings("UnusedReturnValue")
public class PlantopiaTagSet<T> {
    private final ArrayList<TagKey<T>> tags;
    private final ArrayList<T> values;
    private final ArrayList<ResourceLocation> optionalTags;
    private final ArrayList<ResourceLocation> optionalValues;

    public PlantopiaTagSet() {
        tags = Lists.newArrayList();
        values = Lists.newArrayList();
        optionalTags = Lists.newArrayList();
        optionalValues = Lists.newArrayList();
    }

    @Contract(" -> new")
    public static <T> @NotNull PlantopiaTagSet<T> newTagSet() {
        return new PlantopiaTagSet<>();
    }

    public PlantopiaTagSet<T> addAll(@NotNull PlantopiaTagSet<T> tagSet) {
        tagSet.tags.forEach(this::addTag);
        tagSet.values.forEach(this::add);
        tagSet.optionalTags.forEach(this::addOptionalTag);
        tagSet.optionalValues.forEach(this::addOptional);
        return this;
    }

    public boolean isEmpty() {
        return tags.isEmpty()
            && values.isEmpty()
            && optionalTags.isEmpty()
            && optionalValues.isEmpty();
    }

    public ArrayList<T> getValues() {
        return values;
    }

    public ArrayList<TagKey<T>> getTags() {
        return tags;
    }

    public ArrayList<ResourceLocation> getOptionalValues() {
        return optionalValues;
    }

    public ArrayList<ResourceLocation> getOptionalTags() {
        return optionalTags;
    }

    /* VALUES **************************************************/

    public boolean contains(T value) {
        return values.contains(value);
    }

    public boolean containsOptional(ResourceLocation value) {
        return optionalValues.contains(value);
    }

    @SafeVarargs
    public final PlantopiaTagSet<T> add(T... values) {
        if(values != null) {
            for(T value : values) {
                if(!contains(value)) {
                    this.values.add(value);
                }
            }
        }

        return this;
    }

    public final PlantopiaTagSet<T> addOptional(ResourceLocation... optionalValues) {
        if(optionalValues != null) {
            for(ResourceLocation optionalValue : optionalValues) {
                if(!containsOptional(optionalValue)) {
                    this.optionalValues.add(optionalValue);
                }
            }
        }

        return this;
    }

    /* TARGS **************************************************/

    public boolean containsTag(TagKey<T> tag) {
        return tags.contains(tag);
    }

    public boolean containsOptionalTag(ResourceLocation optionalTag) {
        return optionalTags.contains(optionalTag);
    }

    @SafeVarargs
    public final PlantopiaTagSet<T> addTag(TagKey<T>... tags) {
        if(tags != null) {
            for(TagKey<T> tag : tags) {
                if(!containsTag(tag)) {
                    this.tags.add(tag);
                }
            }
        }

        return this;
    }

    public final PlantopiaTagSet<T> addOptionalTag(ResourceLocation... optionalTags) {
        if(optionalTags != null) {
            for(ResourceLocation optionalTag : optionalTags) {
                if(!containsOptionalTag(optionalTag)) {
                    this.optionalTags.add(optionalTag);
                }
            }
        }

        return this;
    }
}
