package by.langvest.toolkit.event;

import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class RegistryEvent extends Event {
	protected final Registrar<?> registrar;

	public RegistryEvent(Registrar<?> registrar) {
		this.registrar = registrar;
	}

	@SuppressWarnings("unchecked")
	public <T> void register(ResourceKey<Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier) {
		((Registrar<T>)registrar).register(registryKey, identifier, supplier);
	}

	public <T> void registerAll(ResourceKey<Registry<T>> registryKey, by.langvest.toolkit.registry.@NotNull Registry<T> sourceRegistry) {
		registerAll(registryKey, sourceRegistry.getAll());
	}

	@SafeVarargs
	public final <T> void registerAll(ResourceKey<Registry<T>> registryKey, RegistryObject<T>... registryObjects) {
		registerAll(registryKey, Arrays.asList(registryObjects));
	}

	public <T> void registerAll(ResourceKey<Registry<T>> registryKey, @NotNull List<RegistryObject<T>> registryObjects) {
		for(RegistryObject<T> registryObject : registryObjects) {
			register(registryKey, registryObject.getIdentifier(), registryObject);
		}
	}

	@FunctionalInterface
	public interface Registrar<T> {
		void register(ResourceKey<Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier);
	}
}
