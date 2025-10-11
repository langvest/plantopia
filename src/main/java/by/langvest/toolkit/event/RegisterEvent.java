package by.langvest.toolkit.event;

import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class RegisterEvent extends Event {
	public abstract <T> void register(ResourceKey<Registry<T>> registryKey, ResourceLocation identifier, Supplier<T> supplier);

	public <T> void registerAll(ResourceKey<Registry<T>> registryKey, by.langvest.toolkit.registry.@NotNull Registry<T> sourceRegistry) {
		registerAll(registryKey, sourceRegistry.getAll());
	}

	@SafeVarargs
	public final <T> void registerAll(ResourceKey<Registry<T>> registryKey, RegistryObject<T>... registryObjects) {
		registerAll(registryKey, Arrays.asList(registryObjects));
	}

	public <T> void registerAll(ResourceKey<Registry<T>> registryKey, @NotNull List<RegistryObject<T>> registryObjects) {
		for(var registryObject : registryObjects) {
			register(registryKey, registryObject.getIdentifier(), registryObject);
		}
	}
}
