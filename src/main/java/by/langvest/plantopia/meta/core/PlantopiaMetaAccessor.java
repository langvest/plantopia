package by.langvest.plantopia.meta.core;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlantopiaMetaAccessor {
	@Contract(pure = true)
	public static <T extends PlantopiaMetaType<T, P>, P extends PlantopiaMetaProperties<T, P>> T getMetaTypeFrom(@NotNull P properties) {
		return properties.type;
	}

	@Contract(pure = true)
	public static <T extends PlantopiaMetaType<T, P>, P extends PlantopiaMetaProperties<T, P>> P getMetaPropertiesFrom(@NotNull T type) {
		return type.properties;
	}
}