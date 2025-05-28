package by.langvest.plantopia.meta.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlantopiaMetaProperties<T extends PlantopiaMetaType<T, P>, P extends PlantopiaMetaProperties<T, P>> implements Cloneable {
	@Nullable
	protected T type = null;

	protected static <T extends PlantopiaMetaType<T, P>, P extends PlantopiaMetaProperties<T, P>> @NotNull P fromType(@NotNull T type) {
		var properties = type.properties.clone();
		properties.type = type;
		return properties;
	}

	@Override
	@SuppressWarnings("unchecked")
	public P clone() {
		try {
			return (P)super.clone();
		} catch(CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}