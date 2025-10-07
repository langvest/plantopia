package by.langvest.toolkit.meta;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class MetaObject<T> implements Supplier<T> {
	protected final ResourceLocation identifier;

	public MetaObject(ResourceLocation identifier) {
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