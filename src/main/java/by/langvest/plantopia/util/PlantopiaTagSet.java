package by.langvest.plantopia.util;

import com.google.common.collect.Lists;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlantopiaTagSet<T> {
	private final ArrayList<TagKey<T>> tags;
	private final ArrayList<T> values;

	public PlantopiaTagSet() {
		tags = Lists.newArrayList();
		values = Lists.newArrayList();
	}

	@Contract(" -> new")
	public static <T> @NotNull PlantopiaTagSet<T> newTagSet() {
		return new PlantopiaTagSet<>();
	}

	public PlantopiaTagSet<T> add(T[] values) {
		if(values != null) for(T value : values) add(value);
		return this;
	}

	public PlantopiaTagSet<T> add(T value) {
		if(values.contains(value)) return this;
		values.add(value);
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public PlantopiaTagSet<T> addTags(TagKey<T>[] tags) {
		if(tags != null) for(TagKey<T> tag : tags) addTag(tag);
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public PlantopiaTagSet<T> addTag(TagKey<T> tag) {
		if(tags.contains(tag)) return this;
		tags.add(tag);
		return this;
	}

	public boolean isEmpty() {
		return tags.isEmpty() && values.isEmpty();
	}

	public ArrayList<T> getValues() {
		return values;
	}

	public ArrayList<TagKey<T>> getTags() {
		return tags;
	}
}