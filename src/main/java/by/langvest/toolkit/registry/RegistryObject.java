package by.langvest.toolkit.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class RegistryObject<T> implements Supplier<T> {
	protected final ResourceLocation identifier;

	public RegistryObject(ResourceLocation identifier) {
		this.identifier = identifier;
	}

	public ResourceLocation getIdentifier() {
		return identifier;
	}

	public String getName() {
		return identifier.getPath();
	}

	public String getNamespace() {
		return identifier.getNamespace();
	}

	@Override
	public String toString() {
		return String.format("%s{%s}", getClass().getSimpleName(), getIdentifier());
	}
}
