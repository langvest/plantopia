package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class PlantopiaResourceHelper {
	@Contract("_, _ -> new")
	public static @NotNull ResourceLocation locationFrom(String namespace, String name) {
		return ResourceLocation.fromNamespaceAndPath(namespace, name);
	}

	@Contract("_, _ -> new")
	public static @NotNull ResourceLocation locationFrom(String namespace, String... path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, String.join("/", path));
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraft(String name) {
		return locationFrom("minecraft", name);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraft(String... path) {
		return locationFrom("minecraft", path);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopia(String name) {
		return locationFrom(Plantopia.MOD_ID, name);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopia(String... path) {
		return locationFrom(Plantopia.MOD_ID, path);
	}

	public static @NotNull ResourceLocation locationOf(@NotNull Object object) {
		var resourceHelper = Plantopia.getPlatform().getResourceHelper();

		return resourceHelper.getLocationOrThrow(object);
	}

	public static @NotNull String idOf(@NotNull Object object) {
		return locationOf(object).toString();
	}

	public static @NotNull String nameOf(@NotNull Object object) {
		return locationOf(object).getPath();
	}

	public static @NotNull String namespaceOf(@NotNull Object object) {
		return locationOf(object).getNamespace();
	}
}
