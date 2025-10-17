package by.langvest.toolkit.meta;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MetaAccessor {
	@Contract(pure = true)
	public static <T extends SimpleMetaObject.MetaType<T, P>, P extends SimpleMetaObject.MetaProperties<T, P>> T getMetaTypeFrom(@NotNull P properties) {
		return properties.type;
	}

	@Contract(pure = true)
	public static <T extends SimpleMetaObject.MetaType<T, P>, P extends SimpleMetaObject.MetaProperties<T, P>> P getMetaPropertiesFrom(@NotNull T type) {
		return type.properties;
	}
}