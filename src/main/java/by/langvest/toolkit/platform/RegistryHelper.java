package by.langvest.toolkit.platform;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class RegistryHelper extends PlatformHelper {
	protected static Map<ResourceLocation, RegistryEntry> knownRegistries;
	
	public RegistryHelper(Platform platform) {
		super(platform);
	}

	public Map<ResourceLocation, RegistryEntry> getKnownRegistries() {
		if(knownRegistries != null) return knownRegistries;

		knownRegistries = Maps.newHashMap();

		addKnownRegistries(knownRegistries);

		return knownRegistries;
	}

	protected void addKnownRegistries(Map<ResourceLocation, RegistryEntry> registries) {
		addBuiltInRegistries(registries);
	}

	protected void addBuiltInRegistries(Map<ResourceLocation, RegistryEntry> registries) {
		BiConsumer<Field, Registry<?>> func = (field, registry) -> {
			Type genericType = field.getGenericType();

			if(genericType instanceof ParameterizedType registryType) {
				Type[] registryTypeArgs = registryType.getActualTypeArguments();

				if(registryTypeArgs.length == 1) {
					var location = registry.key().location();
					var clazz = getErasedClassFromType(registryTypeArgs[0]);
					var adapter = new BuiltInRegistryAdapter<>(registry);

					registries.put(location, new RegistryEntry(location, clazz, adapter));
				}
			}
		};

		for(Field field : BuiltInRegistries.class.getDeclaredFields()) {
			try {
				if(!Modifier.isStatic(field.getModifiers())) continue;

				field.setAccessible(true);
				var value = field.get(null);

				if(value instanceof Registry<?> registry) {
					func.accept(field, registry);
				}
			} catch(Exception e) {
				platform.getLogger().error("Error while obtaining known vanilla registry", e);
			}
		}
	}

	protected static @Nullable Class<?> getErasedClassFromType(Type type) {
		while(true) {
			if(type instanceof Class<?> clazz) return clazz;

			if(type instanceof ParameterizedType pt) {
				type = pt.getRawType();
				continue;
			}

			if(type instanceof WildcardType wt) {
				Type[] bounds = wt.getUpperBounds();
				if(bounds.length == 0) return Object.class;
				type = bounds[0];
				continue;
			}

			if (type instanceof TypeVariable<?> tv) {
				Type[] bounds = tv.getBounds();
				if(bounds.length == 0) return Object.class;
				type = bounds[0];
				continue;
			}

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Optional<ResourceLocation> getRegistryName(Object object) {
		var matches = getKnownRegistries()
			.values()
			.stream()
			.filter(entry -> entry.type().isInstance(object))
			.toList();

		if(matches.size() != 1) return Optional.empty();

		RegistryAdapter<Object> registry = (RegistryAdapter<Object>) matches.get(0).registry();

		var key = registry.getKey(object);

		return Optional.ofNullable(key);
	}

	public record RegistryEntry(ResourceLocation location, Class<?> type, RegistryAdapter<?> registry) {}
}
