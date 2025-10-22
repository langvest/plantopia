package by.langvest.toolkit.platform;

import by.langvest.toolkit.util.LocationLike;
import by.langvest.toolkit.util.Streamable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class RegistryAdapter<T> implements LocationLike, Streamable<T> {
	public abstract ResourceKey<? extends Registry<?>> getRegistryKey();

	public abstract Optional<ResourceKey<T>> getResourceKey(T value);

	public abstract Optional<T> getValue(ResourceLocation key);

	public @NotNull ResourceKey<T> getKeyOrThrow(T value) {
		var key = getResourceKey(value);
		if(key.isPresent()) return key.get();
		throw new NoSuchElementException(String.format("Cannot get resource key of the value '%s' as it does not exist in the registry %s", value, this));
	}

	public @NotNull T getValueOrThrow(ResourceLocation key) {
		var value = getValue(key);
		if(value.isPresent()) return value.get();
		throw new NoSuchElementException(String.format("Cannot get value for the key '%s' as it does not exist in the registry %s", key, this));
	}

	@Override
	public ResourceLocation location() {
		return getRegistryKey().location();
	}

	@Override
	public String toString() {
		return String.format("%s{%s}", getClass().getSimpleName(), getRegistryKey());
	}
}
