package by.langvest.toolkit.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class BuiltInRegistryAdapter<T> extends RegistryAdapter<T> {
	protected Registry<T> builtInRegistry;

	public BuiltInRegistryAdapter(Registry<T> registry) {
		this.builtInRegistry = registry;
	}

	public Registry<T> getBuiltInRegistry() {
		return builtInRegistry;
	}

	@Override
	public ResourceLocation getLocation() {
		return getBuiltInRegistry().key().location();
	}

	@Override
	public ResourceLocation getKey(T value) {
		return getBuiltInRegistry().getKey(value);
	}
}
