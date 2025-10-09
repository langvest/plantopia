package by.langvest.toolkit.platform;

import by.langvest.toolkit.util.LocationLike;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class RegistryAdapter<T> implements LocationLike {
	public abstract ResourceKey<? extends Registry<?>> getRegistryKey();

	public abstract Optional<ResourceLocation> getKey(T value);

	public abstract Optional<T> getValue(ResourceLocation key);

	public ResourceLocation getKeyOrThrow(T value) {
		var key = getKey(value);

		if(key.isEmpty()) {
			throw new NoSuchElementException(String.format("Cannot get key of the value '%s' as it does not exist in the registry '%s'", value, location()));
		}

		return key.get();
	}

	public T getValueOrThrow(ResourceLocation key) {
		var value = getValue(key);

		if(value.isEmpty()) {
			throw new NoSuchElementException(String.format("Cannot get value for the key '%s' as it does not exist in the registry '%s'", key, location()));
		}

		return value.get();
	}

	@Override
	public ResourceLocation location() {
		return getRegistryKey().location();
	}
}
