package by.langvest.toolkit.meta;

import com.google.common.collect.Maps;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MetaBucket<Meta extends MetaObject<?>> {
	protected final ResourceLocation location;
	protected final HashMap<ResourceLocation, Meta> storage = Maps.newLinkedHashMap();

	public MetaBucket(ResourceLocation location) {
		this.location = location;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	public Stream<Meta> stream() {
		return storage.values().stream();
	}

	public List<Meta> getAll() {
		return stream().toList();
	}

	public List<Meta> findAll(Predicate<Meta> predicate) {
		return stream().filter(predicate).toList();
	}

	@Nullable
	public Meta findValue(Predicate<Meta> predicate) {
		for(Meta item : storage.values()) if(predicate.test(item)) return item;
		return null;
	}

	public boolean hasKey(ResourceLocation key) {
		return storage.containsKey(key);
	}

	@Nullable
	public Meta getValue(ResourceLocation key) {
		return storage.get(key);
	}

	@NotNull
	public Meta getValueOrThrow(ResourceLocation key) {
		return Objects.requireNonNull(getValue(key));
	}

	public Meta associate(ResourceLocation key, Meta meta) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(meta);

		if(hasKey(key)) {
			throw new IllegalIcuArgumentException(String.format("Cannot add a new association for the key '%s', as it already exists in the meta bucket '%s'.", key, getLocation()));
		}

		storage.put(key, meta);

		return meta;
	}

	public void forEach(Consumer<Meta> action) {
		storage.forEach((key, meta) -> action.accept(meta));
	}
}