package by.langvest.toolkit.registry;

import com.google.common.collect.Maps;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class SimpleRegistry<T> extends Registry<T> {
	protected final ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey;
	protected final HashMap<ResourceLocation, RegistryObject<T>> storage = Maps.newLinkedHashMap();

	public SimpleRegistry(ResourceKey<? extends net.minecraft.core.Registry<T>> registryKey) {
		this.registryKey = registryKey;
	}

	@Override
	public ResourceKey<? extends net.minecraft.core.Registry<T>> getRegistryKey() {
		return registryKey;
	}

	@Override
	public boolean hasKey(ResourceLocation key) {
		return storage.containsKey(key);
	}

	@Override
	public SupposedRegistryObject<T> supposeValue(ResourceLocation key) {
		return new SupposedRegistryObject<>(key, this);
	}

	@Override
	public Optional<RegistryObject<T>> getValue(ResourceLocation key) {
		return Optional.ofNullable(storage.get(key));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V extends T> RegistryObject<V> register(ResourceLocation key, Supplier<V> supplier) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(supplier);

		if(hasKey(key)) {
			throw new IllegalIcuArgumentException(String.format("Cannot add a new registration for the key '%s' as it already exists in the registry %s", key, this));
		}

		var registryObject = new SimpleRegistryObject<>(key, supplier);

		storage.put(key, (RegistryObject<T>)registryObject);

		return registryObject;
	}

	@Override
	public @NotNull Iterator<RegistryObject<T>> iterator() {
		return storage.values().iterator();
	}
}
