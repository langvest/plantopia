package by.langvest.toolkit.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;

public class BuiltInRegistryAdapter<T> extends RegistryAdapter<T> {
	protected Registry<T> builtInRegistry;

	public BuiltInRegistryAdapter(Registry<T> registry) {
		this.builtInRegistry = registry;
	}

	public Registry<T> getBuiltInRegistry() {
		return builtInRegistry;
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistryKey() {
		return getBuiltInRegistry().key();
	}

	@Override
	public Optional<ResourceLocation> getKey(T value) {
		return Optional.ofNullable(getBuiltInRegistry().getKey(value));
	}

	@Override
	public Optional<T> getValue(ResourceLocation key) {
		return Optional.ofNullable(getBuiltInRegistry().get(key));
	}

	@Override
	public @NotNull Iterator<T> iterator() {
		return getBuiltInRegistry().iterator();
	}
}
