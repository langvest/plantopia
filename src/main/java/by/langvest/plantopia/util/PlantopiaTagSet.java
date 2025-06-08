package by.langvest.plantopia.util;

import com.google.common.collect.Lists;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlantopiaTagSet<T> {
	private final ArrayList<TagKey<T>> tags;
	private final ArrayList<T> elements;

	public PlantopiaTagSet() {
		tags = Lists.newArrayList();
		elements = Lists.newArrayList();
	}

	@Contract(" -> new")
	public static <T> @NotNull PlantopiaTagSet<T> newTagSet() {
		return new PlantopiaTagSet<>();
	}

	public PlantopiaTagSet<T> add(T[] elements) {
		if(elements != null) for(T element : elements) add(element);
		return this;
	}

	public PlantopiaTagSet<T> add(T element) {
		if(this.elements.contains(element)) return this;
		this.elements.add(element);
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public PlantopiaTagSet<T> addTags(TagKey<T>[] tags) {
		if(tags != null) for(TagKey<T> tag : tags) addTag(tag);
		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public PlantopiaTagSet<T> addTag(TagKey<T> tag) {
		if(this.tags.contains(tag)) return this;
		this.tags.add(tag);
		return this;
	}

	public boolean isEmpty() {
		return tags.isEmpty() && elements.isEmpty();
	}

	public ArrayList<T> getElements() {
		return elements;
	}

	public ArrayList<TagKey<T>> getTags() {
		return tags;
	}
}