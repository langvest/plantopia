package by.langvest.plantopia.meta.core;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public abstract class PlantopiaMetaType<T extends PlantopiaMetaType<T, P>, P extends PlantopiaMetaProperties<T, P>> {
	protected final ResourceLocation id;
	private final String category;
	private final String name;
	protected final P properties;

	public PlantopiaMetaType(String category, String name, @NotNull P properties) {
		this.id = plantopiaLocationFrom(category, name);
		this.category = category;
		this.name = name;
		this.properties = properties;
	}

	public PlantopiaMetaType(@NotNull ResourceLocation id, @NotNull P properties) {
		var splitId = id.getPath().split("/");

		this.id = id;
		this.category = splitId[0];
		this.name = splitId[1];
		this.properties = properties;
	}

	public boolean instanceOf(T type) {
		var innerType = this;

		do {
			if(innerType.equals(type)) return true;
			innerType = innerType.properties.type;
		} while(innerType != null);

		return false;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public ResourceLocation getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("%s{%s}", getClass().getSimpleName(), getId());
	}
}