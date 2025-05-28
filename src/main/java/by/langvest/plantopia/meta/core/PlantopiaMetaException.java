package by.langvest.plantopia.meta.core;

import org.jetbrains.annotations.NotNull;

public class PlantopiaMetaException extends RuntimeException {
	public PlantopiaMetaException(String message) {
		super(message);
	}

	public static class UnableToSet extends RuntimeException {
		public UnableToSet(String propertyName, @NotNull PlantopiaMetaType<?, ?> type) {
			super(String.format("Unable to set '%s' property to '%s' meta type properties.", propertyName, type.getId()));
		}
	}

	public static class Required extends RuntimeException {
		public Required(String propertyName, @NotNull PlantopiaMetaType<?, ?> type) {
			super(String.format("'%s' property is required for '%s' meta type properties.", propertyName, type.getId()));
		}
	}
}