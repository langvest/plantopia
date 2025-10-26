package by.langvest.toolkit.util;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("UnusedReturnValue")
public class TagSet<T, Self extends TagSet<T, Self>> {
    protected final ArrayList<TagKey<T>> tags;
    protected final ArrayList<ResourceKey<T>> values;
    protected final ArrayList<ResourceLocation> optionalTags;
    protected final ArrayList<ResourceLocation> optionalValues;

    public TagSet() {
        tags = Lists.newArrayList();
        values = Lists.newArrayList();
        optionalTags = Lists.newArrayList();
        optionalValues = Lists.newArrayList();
    }

    @SuppressWarnings("unchecked")
    protected Self self() {
        return (Self) this;
    }

    public Self addAll(@NotNull TagSet<T, Self> tagSet) {
        tagSet.tags.forEach(this::addTag);
        tagSet.values.forEach(this::add);
        tagSet.optionalTags.forEach(this::addOptionalTag);
        tagSet.optionalValues.forEach(this::addOptional);
        return self();
    }

    public boolean isEmpty() {
        return tags.isEmpty()
            && values.isEmpty()
            && optionalTags.isEmpty()
            && optionalValues.isEmpty();
    }

    public Self apply(@NotNull Consumer<Self> consumer) {
        consumer.accept(self());
        return self();
    }

    public ArrayList<ResourceKey<T>> getValues() {
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

    public boolean contains(ResourceKey<T> value) {
        return values.contains(value);
    }

    public boolean containsOptional(ResourceLocation value) {
        return optionalValues.contains(value);
    }

    @SafeVarargs
    public final Self add(ResourceKey<T>... values) {
        if(values != null) {
            for(ResourceKey<T> value : values) {
                if(!contains(value)) {
                    this.values.add(value);
                }
            }
        }

        return self();
    }

    public final Self addOptional(ResourceLocation... optionalValues) {
        if(optionalValues != null) {
            for(ResourceLocation optionalValue : optionalValues) {
                if(!containsOptional(optionalValue)) {
                    this.optionalValues.add(optionalValue);
                }
            }
        }

        return self();
    }

    /* TARGS **************************************************/

    public boolean containsTag(TagKey<T> tag) {
        return tags.contains(tag);
    }

    public boolean containsOptionalTag(ResourceLocation optionalTag) {
        return optionalTags.contains(optionalTag);
    }

    @SafeVarargs
    public final Self addTag(TagKey<T>... tags) {
        if(tags != null) {
            for(TagKey<T> tag : tags) {
                if(!containsTag(tag)) {
                    this.tags.add(tag);
                }
            }
        }

        return self();
    }

    public final Self addOptionalTag(ResourceLocation... optionalTags) {
        if(optionalTags != null) {
            for(ResourceLocation optionalTag : optionalTags) {
                if(!containsOptionalTag(optionalTag)) {
                    this.optionalTags.add(optionalTag);
                }
            }
        }

        return self();
    }
}
