package by.langvest.toolkit.registry;

import com.google.common.collect.Maps;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Registry<T> {
	protected final ResourceLocation location;
	protected final HashMap<ResourceLocation, RegistryObject<T>> storage = Maps.newLinkedHashMap();

	public Registry(ResourceLocation location) {
		this.location = location;
	}

	public ResourceLocation getLocation() {
		return location;
	}

	@SuppressWarnings("unchecked")
	public <V extends T> RegistryObject<V> register(ResourceLocation key, Supplier<V> supplier) {

		Objects.requireNonNull(key);
		Objects.requireNonNull(supplier);

		if(hasKey(key)) {
			throw new IllegalIcuArgumentException(String.format("Cannot add a new registration for the key '%s', as it already exists in the registry '%s'.", key, getLocation()));
		}

		var registryObject = new SimpleRegistryObject<>(key, supplier);

		storage.put(key, (RegistryObject<T>)registryObject);

		return registryObject;
	}

	public List<RegistryObject<T>> getAll() {
		return storage.values().stream().toList();
	}

	public List<RegistryObject<T>> findAll(Predicate<RegistryObject<T>> predicate) {
		return storage.values().stream().filter(predicate).toList();
	}

	public @Nullable RegistryObject<T> findValue(Predicate<RegistryObject<T>> predicate) {
		for(RegistryObject<T> meta : storage.values()) if(predicate.test(meta)) return meta;
		return null;
	}

	public boolean hasKey(ResourceLocation key) {
		return storage.containsKey(key);
	}

	public @Nullable SupposedRegistryObject<T> supposeValue(ResourceLocation key) {
		return new SupposedRegistryObject<>(key, this);
	}

	public @Nullable RegistryObject<T> getValue(ResourceLocation key) {
		return storage.get(key);
	}

	public @NotNull RegistryObject<T> getValueOrThrow(ResourceLocation key) {
		return Objects.requireNonNull(getValue(key));
	}

	public void forEach(Consumer<RegistryObject<T>> action) {
		storage.forEach((key, registryObject) -> action.accept(registryObject));
	}
}
