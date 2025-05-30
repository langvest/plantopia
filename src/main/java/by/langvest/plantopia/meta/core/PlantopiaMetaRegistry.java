package by.langvest.plantopia.meta.core;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaMetaRegistry<Target, Meta extends PlantopiaMetaObject<?>> {
	protected final ResourceLocation id;
	protected final HashMap<ResourceLocation, Meta> storage = Maps.newLinkedHashMap();
	protected final LocationExtractor<Target> locationExtractor;

	public PlantopiaMetaRegistry(ResourceLocation id, LocationExtractor<Target> locationExtractor) {
		this.id = id;
		this.locationExtractor = locationExtractor;
	}

	public ResourceLocation getId() {
		return id;
	}

	public List<Meta> getAll() {
		return storage.values().stream().toList();
	}

	public List<Meta> findAll(Predicate<Meta> predicate) {
		return storage.values().stream().filter(predicate).toList();
	}

	@Nullable
	public Meta findValue(Predicate<Meta> predicate) {
		for(Meta meta : storage.values()) if(predicate.test(meta)) return meta;
		return null;
	}

	@Nullable
	public ResourceLocation getKey(Target target) {
		ResourceLocation key = locationExtractor.extract(target);
		if(hasKey(key)) return key;
		return null;
	}

	public boolean hasKey(ResourceLocation key) {
		return storage.containsKey(key);
	}

	@Nullable
	public Meta getValue(Target target) {
		ResourceLocation key = locationExtractor.extract(target);
		return getValue(key);
	}

	@Nullable
	public Meta getValue(ResourceLocation key) {
		return storage.get(key);
	}

	@NotNull
	public Meta getValueOrThrow(Target target) {
		return Objects.requireNonNull(getValue(target));
	}

	@NotNull
	public Meta getValueOrThrow(ResourceLocation key) {
		return Objects.requireNonNull(getValue(key));
	}

	public Meta associate(Target target, Meta meta) {
		ResourceLocation key = locationExtractor.extract(target);
		return associate(key, meta);
	}

	public Meta associate(String name, Meta meta) {
		return associate(plantopiaLocationFrom(name), meta);
	}

	public Meta associate(ResourceLocation key, Meta meta) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(meta);

		if(hasKey(key)) throw new IllegalArgumentException(String.format("Cannot add a new association for the '%s' key, as it already exists in meta registry '%s'.", key, getId()));

		storage.put(key, meta);


		return meta;
	}

	public void forEach(Consumer<Meta> action) {
		Objects.requireNonNull(action);

		for(Meta meta : getAll()) action.accept(meta);
	}

	public interface LocationExtractor<Target> {
		ResourceLocation extract(Target target);
	}
}