package by.langvest.toolkit.platform;

import by.langvest.toolkit.util.LocationLike;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ResourceHelper extends PlatformHelper {
	public ResourceHelper(Platform platform) {
		super(platform);
	}

	public Optional<ResourceLocation> getLocation(Object object) {
		if(object instanceof ResourceLocation location) return Optional.of(location);
		if(object instanceof LocationLike locationLike) return Optional.of(locationLike.location());
		if(object instanceof ResourceKey<?> key) return Optional.of(key.location());
		if(object instanceof TagKey<?> key) return Optional.of(key.location());
		return getPlatform().getRegistryHelper().getRegistryName(object);
	}

	@NotNull
	public ResourceLocation getLocationOrThrow(Object object) {
		var location = getLocation(object);

		if(location.isEmpty()) {
			throw new IllegalArgumentException("No location found for object " + object);
		}

		return location.get();
	}
}
