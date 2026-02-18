package by.langvest.toolkit.registry;

import by.langvest.toolkit.util.LocationLike;
import by.langvest.toolkit.util.Streamable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class Registry<T> implements LocationLike, Streamable<RegistryObject<T>> {
	public abstract ResourceKey<? extends net.minecraft.core.Registry<T>> getRegistryKey();

	public abstract boolean hasKey(ResourceLocation key);

	public abstract Optional<RegistryObject<T>> getValue(ResourceLocation key);

	public abstract Optional<ResourceLocation> getKey(T value);

	public @NotNull RegistryObject<T> getValueOrThrow(ResourceLocation key) {
		var value = getValue(key);
		if(value.isPresent()) return value.get();
		throw new NoSuchElementException(String.format("Cannot get value for the key '%s' as it does not exist in the registry %s", key, this));
	}

	public abstract SupposedRegistryObject<T> supposeValue(ResourceLocation key);

	public abstract <V extends T> RegistryObject<V> register(ResourceLocation key, Supplier<V> supplier);

	@Override
	public ResourceLocation location() {
		return getRegistryKey().location();
	}

	@Override
	public String toString() {
		return String.format("%s{%s}", getClass().getSimpleName(), getRegistryKey());
	}

    public Codec<T> byNameCodec() {
        return ResourceLocation.CODEC.flatXmap(
                location -> this.getValue(location)
                        .map(RegistryObject::get)
                        .map(DataResult::success)
                        .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + this.getRegistryKey() + ": " + location)),
                value -> this.getKey(value)
                        .map(DataResult::success)
                        .orElseGet(() -> DataResult.error(() -> "Unknown registry element in " + this.getRegistryKey() + ":" + value))
        );
    }
}
