package by.langvest.toolkit.platform;

import by.langvest.toolkit.util.LocationRepresentable;
import net.minecraft.resources.ResourceLocation;

public abstract class RegistryAdapter<T> implements LocationRepresentable {
	public abstract ResourceLocation getKey(T value);
}
