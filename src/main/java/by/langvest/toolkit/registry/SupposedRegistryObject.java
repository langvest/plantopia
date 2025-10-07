package by.langvest.toolkit.registry;

import net.minecraft.resources.ResourceLocation;

public class SupposedRegistryObject<T> extends RegistryObject<T> {
	protected final Registry<T> registry;

	public SupposedRegistryObject(ResourceLocation identifier, Registry<T> registry) {
		super(identifier);
		this.registry = registry;
	}

	@Override
	public T get() {
		var registryObject = registry.getValue(identifier);

		if(registryObject == null) {
			throw new NullPointerException(String.format("Supposed registry object with the identifier '%s' is not found in the registry '%s'!", identifier, registry.getLocation()));
		}

		return registryObject.get();
	}
}
