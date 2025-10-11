package by.langvest.plantopia.datagen.tag;

import by.langvest.plantopia.util.PlantopiaTagSet;
import by.langvest.plantopia.util.helper.PlantopiaResourceHelper;
import by.langvest.toolkit.meta.MetaAccessor;
import by.langvest.toolkit.meta.SimpleMetaObject;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

public interface PlantopiaTagProvider<T> {
    String META_TYPE_TAG_PREFIX = "type/";
    Map<Pair<ResourceKey<? extends Registry<?>>, SimpleMetaObject.MetaType<?, ?>>, PlantopiaTagSet<?>> CACHE = Maps.newHashMap();

    IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> getTagAppender(TagKey<T> key);

    default void saveByTagKeys(@NotNull Map<TagKey<T>, PlantopiaTagSet<T>> tagSets) {
        tagSets.forEach(this::save);
    }

    @SuppressWarnings("unchecked")
    default void saveByMetaTypes(@NotNull Map<? extends SimpleMetaObject.MetaType<?, ?>, PlantopiaTagSet<T>> tagSets) {
        for(var metaType : tagSets.keySet()) {
            recursivelyPrepareParentTagSetFor(metaType);
        }

        tagSets.forEach((metaType, tagSet) -> getCachedTagSetOf(metaType).addAll(tagSet));

        var registryKey = getRegistryKey();

        CACHE.entrySet()
            .stream()
            .filter(entry -> entry.getKey().getFirst().equals(registryKey))
            .forEach(entry -> {
                var metaType = entry.getKey().getSecond();
                var tagSet = (PlantopiaTagSet<T>) entry.getValue();
                var key = getTagKeyOf(metaType);

                save(key, tagSet);
            });
    }

    default void save(TagKey<T> key, @NotNull PlantopiaTagSet<T> tagSet) {
        if(tagSet.isEmpty()) return;

        var appender = getTagAppender(key);

        var tags = tagSet.getTags();
        var values = tagSet.getValues();
        var optionalTags = tagSet.getOptionalTags();
        var optionalValues = tagSet.getOptionalValues();

        tags.sort(PlantopiaResourceHelper::compareById);
        values.sort(PlantopiaResourceHelper::compareById);
        optionalTags.sort(PlantopiaResourceHelper::compareById);
        optionalValues.sort(PlantopiaResourceHelper::compareById);

        tags.forEach(appender::addTag);
        values.forEach(appender::add);
        optionalTags.forEach(appender::addOptionalTag);
        optionalValues.forEach(appender::addOptional);
    }

    default void recursivelyPrepareParentTagSetFor(SimpleMetaObject.@NotNull MetaType<?, ?> metaType) {
        var parentMetaType = getParentMetaTypeOf(metaType);

        if(parentMetaType == null) return;

        getCachedTagSetOf(parentMetaType).addTag(getTagKeyOf(metaType));
        recursivelyPrepareParentTagSetFor(parentMetaType);
    }

    ResourceKey<? extends Registry<T>> getRegistryKey();

    default TagKey<T> getTagKeyOf(SimpleMetaObject.@NotNull MetaType<?, ?> metaType) {
        return TagKey.create(getRegistryKey(), metaType.getIdentifier().withPrefix(META_TYPE_TAG_PREFIX));
    }

    @SuppressWarnings("unchecked")
    default PlantopiaTagSet<T> getCachedTagSetOf(SimpleMetaObject.@NotNull MetaType<?, ?> metaType) {
        return (PlantopiaTagSet<T>) CACHE.computeIfAbsent(Pair.of(getRegistryKey(), metaType), key -> PlantopiaTagSet.newTagSet());
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private static <T extends SimpleMetaObject.MetaType<T, P>, P extends SimpleMetaObject.MetaProperties<T, P>> T getParentMetaTypeOf(SimpleMetaObject.MetaType<?, ?> metaType) {
        P properties = MetaAccessor.getMetaPropertiesFrom((T) metaType);
        return MetaAccessor.getMetaTypeFrom(properties);
    }

    @Contract("_ -> new")
    default @NotNull ResourceLocation forge(String... path) {
        return locationFrom("forge", path);
    }
}
