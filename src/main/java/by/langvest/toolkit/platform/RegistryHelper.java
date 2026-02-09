package by.langvest.toolkit.platform;

import com.google.common.collect.Maps;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Optional;

public abstract class RegistryHelper extends PlatformHelper {
	protected static Map<ResourceLocation, RegistryEntry> knownRegistries;
	
	public RegistryHelper(Platform platform) {
		super(platform);
	}

	public static FlowerPotBlock getEmptyFlowerPotBlock() {
		return (FlowerPotBlock) Blocks.FLOWER_POT;
	}

	public static FireBlock getFireBlock() {
		return (FireBlock) Blocks.FIRE;
	}

	public abstract void registerBrewable(Potion inputPotion, @NotNull ItemLike ingredient, Potion outputPotion);

	public void registerPottable(Block plantBlock, @NotNull FlowerPotBlock pottedBlock) {
		getEmptyFlowerPotBlock().addPlant(getResourceKeyOrThrow(plantBlock).location(), () -> pottedBlock);
	}

	public void registerFlammable(Block block, int encouragement, int flammability) {
		getFireBlock().setFlammable(block, encouragement, flammability);
	}

	public void registerCompostable(@NotNull ItemLike itemLike, float compostability) {
		ComposterBlock.COMPOSTABLES.put(itemLike.asItem(), compostability);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<RegistryAdapter<T>> getKnownRegistry(@NotNull ResourceKey<? extends Registry<T>> registryKey) {
		var registries = getKnownRegistries();
		var entry = registries.get(registryKey.location());

		if(entry == null) return Optional.empty();
		return Optional.of((RegistryAdapter<T>) entry.registry());
	}

	public <T> RegistryAdapter<T> getKnownRegistryOrThrow(@NotNull ResourceKey<? extends Registry<T>> registryKey) {
		var registry = getKnownRegistry(registryKey);
		if(registry.isPresent()) return registry.get();
		throw new IllegalStateException("No any known registry found for key " + registryKey);
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
		for(Field field : BuiltInRegistries.class.getDeclaredFields()) {
			try {
				if(!Modifier.isStatic(field.getModifiers())) continue;

				field.setAccessible(true);
				var value = field.get(null);

				if(value instanceof Registry<?> registry) {
					var location = registry.key().location();
					var clazz = getErasedClassFromSingleDepthField(field);
					var adapter = new BuiltInRegistryAdapter<>(registry);

					registries.put(location, new RegistryEntry(location, clazz, adapter));
				}
			} catch(Exception e) {
				platform.getLogger().error("Error while obtaining known vanilla registry", e);
			}
		}
	}

	protected static @Nullable Class<?> getErasedClassFromSingleDepthField(@NotNull Field field) {
		Type genericType = field.getGenericType();

		if(genericType instanceof ParameterizedType registryType) {
			Type[] registryTypeArgs = registryType.getActualTypeArguments();

			if(registryTypeArgs.length == 1) {
				return getErasedClassFromType(registryTypeArgs[0]);
			}
		}

		return null;
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
	public <T> Optional<ResourceKey<T>> getResourceKey(T object) {
		var matches = getKnownRegistries()
			.values()
			.stream()
			.filter(entry -> entry.type() != null && entry.type().isInstance(object))
			.toList();

		if(matches.size() != 1) return Optional.empty();

		var registry = (RegistryAdapter<T>) matches.get(0).registry();

		return registry.getResourceKey(object);
	}

	public <T> ResourceKey<T> getResourceKeyOrThrow(T object) {
		var registryName = getResourceKey(object);
		if(registryName.isPresent()) return registryName.get();
		throw new IllegalArgumentException(String.format("Object %s is not registered in any known registry!", object));
	}

	public record RegistryEntry(ResourceLocation location, @Nullable Class<?> type, RegistryAdapter<?> registry) {}
}
