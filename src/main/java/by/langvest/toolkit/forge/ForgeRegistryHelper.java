package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.Platform;
import by.langvest.toolkit.platform.RegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeRegistryHelper extends RegistryHelper {
	public ForgeRegistryHelper(Platform platform) {
		super(platform);
	}

	@Override
	protected void addKnownRegistries(Map<ResourceLocation, RegistryEntry> registries) {
		super.addKnownRegistries(registries);
		addForgeRegistries(registries);
	}

	protected void addForgeRegistries(Map<ResourceLocation, RegistryEntry> registries) {
		for(Field field : ForgeRegistries.class.getDeclaredFields()) {
			try {
				if(!Modifier.isStatic(field.getModifiers())) continue;

				field.setAccessible(true);
				var name = field.getName();
				var value = field.get(null);

				if(value instanceof IForgeRegistry<?> registry) {
					var location = registry.getRegistryName();
					var clazz = getErasedClassFromSingleDepthField(field);
					var adapter = new ForgeRegistryAdapter<>(registry);

					registries.put(location, new RegistryEntry(location, clazz, adapter));
				}

				if(value instanceof Supplier<?> registrySupplier) {
					var deferredField = ForgeRegistries.class.getDeclaredField("DEFERRED_" + name);

					deferredField.setAccessible(true);
					var deferredValue = deferredField.get(null);

					if(deferredValue instanceof DeferredRegister<?> deferredRegister) {
						var location = deferredRegister.getRegistryName();
						var clazz = getErasedClassFromSingleDepthField(deferredField);
						var adapter = new ForgeDeferredRegistryAdapter<>(() -> (IForgeRegistry<?>)registrySupplier.get());

						registries.put(location, new RegistryEntry(location, clazz, adapter));
					}
				}
			} catch(Exception e) {
				platform.getLogger().error("Error while obtaining known forge registry", e);
			}
		}
	}
}
