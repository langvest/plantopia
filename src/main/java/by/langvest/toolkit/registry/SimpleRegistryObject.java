package by.langvest.toolkit.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class SimpleRegistryObject<T> extends RegistryObject<T> {
	protected final Supplier<T> supplier;
	protected T instance = null;

	public SimpleRegistryObject(ResourceLocation identifier, Supplier<T> supplier) {
		super(identifier);
		this.supplier = supplier;
	}

	@Override
	public T get() {
		if(instance != null) {
			return instance;
		}

		instance = supplier.get();

		return instance;
	}
}
