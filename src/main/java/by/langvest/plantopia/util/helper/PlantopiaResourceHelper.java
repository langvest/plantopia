package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PlantopiaResourceHelper {
	@Contract("_, _ -> new")
	public static @NotNull ResourceLocation locationFrom(String namespace, String... path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, String.join("/", path));
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraft(String... path) {
		return locationFrom("minecraft", path);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopia(String... path) {
		return locationFrom(Plantopia.MOD_ID, path);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation cascades(String... path) {
		return locationFrom("hybrid_beta", path);
	}

	public static @NotNull ResourceLocation locationOf(@NotNull Object object) {
		var resourceHelper = Plantopia.getPlatform().getResourceHelper();
		return resourceHelper.getLocationOrThrow(object);
	}

	public static @NotNull String idOf(@NotNull Object object) {
		var location = locationOf(object);
		return location.getNamespace() + ":" + location.getPath();
	}

	public static @NotNull String compileNameFrom(Object... nameParts) {
		return Arrays.stream(nameParts)
			.map(object -> {
				if (object instanceof String) return (String) object;
				if (object instanceof Number) return object.toString().replace(".", "");
				return nameOf(object);
			})
			.collect(Collectors.joining("_"));
	}

	public static @NotNull String nameOf(@NotNull Object object) {
		return locationOf(object).getPath();
	}

	public static @NotNull String namespaceOf(@NotNull Object object) {
		return locationOf(object).getNamespace();
	}

	public static Optional<PlantopiaBlockMeta> metaOf(Block block) {
		return PlantopiaMetaBuckets.BLOCK.getValue(locationOf(block));
	}

	public static Optional<PlantopiaItemMeta> metaOf(Item item) {
		return PlantopiaMetaBuckets.ITEM.getValue(locationOf(item));
	}

	public static int compareById(@NotNull Object o1, @NotNull Object o2) {
		return idOf(o1).compareTo(idOf(o2));
	}
}
