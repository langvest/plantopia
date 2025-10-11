package by.langvest.toolkit.meta;

import org.jetbrains.annotations.NotNull;

public class MetaException extends RuntimeException {
	public MetaException(String message) {
		super(message);
	}

	public static class UnableToSet extends RuntimeException {
		public UnableToSet(String propertyName, @NotNull SimpleMetaObject.MetaType<?, ?> type) {
			super(String.format("Unable to set '%s' property to %s meta type properties.", propertyName, type));
		}
	}

	public static class Required extends RuntimeException {
		public Required(String propertyName, @NotNull SimpleMetaObject.MetaType<?, ?> type) {
			super(String.format("'%s' property is required for %s meta type properties.", propertyName, type));
		}
	}
}