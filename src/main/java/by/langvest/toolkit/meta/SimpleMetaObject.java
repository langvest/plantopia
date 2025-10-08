package by.langvest.toolkit.meta;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Supplier;

public class SimpleMetaObject<T> extends MetaObject<T> {
	private final Supplier<T> supplier;

	public SimpleMetaObject(ResourceLocation identifier, Supplier<T> supplier) {
		super(identifier);
		this.supplier = supplier;
	}

	@Override
	public T get() {
		return supplier.get();
	}

	public abstract static class MetaType<Type extends MetaType<Type, Properties>, Properties extends MetaProperties<Type, Properties>> {
		protected final ResourceLocation location;
		protected final Properties properties;

		public MetaType(ResourceLocation location, @NotNull Properties properties) {
			this.location = location;
			this.properties = properties;
		}

		public boolean instanceOfExcept(MetaType<Type, Properties> type, @Nullable Set<MetaType<Type, Properties>> exceptions) {
			MetaType<Type, Properties> innerType = this;

			do {
				if(exceptions != null && exceptions.contains(innerType)) return false;
				if(type.equals(innerType)) return true;
				innerType = innerType.properties.type;
			} while(innerType != null);

			return false;
		}

		public boolean instanceOf(MetaType<Type, Properties> type) {
			return instanceOfExcept(type, null);
		}

		public ResourceLocation getLocation() {
			return location;
		}

		@Override
		public String toString() {
			return String.format("%s{%s}", getClass().getSimpleName(), getLocation());
		}
	}

	public abstract static class MetaProperties<Type extends MetaType<Type, Properties>, Properties extends MetaProperties<Type, Properties>> implements Cloneable {
		@Nullable
		protected Type type = null;

		protected static <T extends MetaType<T, P>, P extends MetaProperties<T, P>> @NotNull P fromType(@NotNull T type) {
			var properties = type.properties.clone();
			properties.type = type;
			return properties;
		}

		@Override
		@SuppressWarnings("unchecked")
		public Properties clone() {
			try {
				return (Properties)super.clone();
			} catch(CloneNotSupportedException e) {
				throw new AssertionError();
			}
		}

		@Override
		public String toString() {
			return String.format("%s{%s}", getClass().getSimpleName(), type == null ? "root" : type.getLocation());
		}
	}
}
