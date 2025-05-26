package by.langvest.plantopia.util.helper;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.adv.PlantopiaAdvancement;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class PlantopiaResourceHelper {
	/* LOCATION FROM ***********************************************************************/

	@Contract("_, _ -> new")
	public static @NotNull ResourceLocation locationFrom(String namespace, String name) {
		return ResourceLocation.fromNamespaceAndPath(namespace, name);
	}

	@Contract("_, _ -> new")
	public static @NotNull ResourceLocation locationFrom(String namespace, String... path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, String.join("/", path));
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraftLocationFrom(String name) {
		return ResourceLocation.fromNamespaceAndPath("minecraft", name);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation minecraftLocationFrom(String... path) {
		return ResourceLocation.fromNamespaceAndPath("minecraft", String.join("/", path));
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopiaLocationFrom(String name) {
		return ResourceLocation.fromNamespaceAndPath(Plantopia.MOD_ID, name);
	}

	@Contract("_ -> new")
	public static @NotNull ResourceLocation plantopiaLocationFrom(String... path) {
		return ResourceLocation.fromNamespaceAndPath(Plantopia.MOD_ID, String.join("/", path));
	}

	/* LOCATION OF ***********************************************************************/

	public static @NotNull ResourceLocation locationOf(@NotNull RegistryObject<?> registryObject) {
		return registryObject.getId();
	}

	public static @NotNull ResourceLocation locationOf(@NotNull TagKey<?> tag) {
		return tag.location();
	}

	public static @NotNull ResourceLocation locationOf(@NotNull ResourceKey<?> key) {
		return key.location();
	}

	public static @NotNull ResourceLocation locationOf(@NotNull Block block) {
		return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block));
	}

	public static @NotNull ResourceLocation locationOf(@NotNull ItemLike itemLike) {
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemLike.asItem()));
	}

	public static @NotNull ResourceLocation locationOf(@NotNull PlantopiaAdvancement advancement) {
		return advancement.getId();
	}

	/* ID OF ***********************************************************************/

	public static @NotNull String idOf(@NotNull RegistryObject<?> registryObject) {
		return locationOf(registryObject).toString();
	}

	public static @NotNull String idOf(@NotNull TagKey<?> tag) {
		return locationOf(tag).toString();
	}

	public static @NotNull String idOf(@NotNull ResourceKey<?> key) {
		return locationOf(key).toString();
	}

	public static @NotNull String idOf(@NotNull Block block) {
		return locationOf(block).toString();
	}

	public static @NotNull String idOf(@NotNull ItemLike itemLike) {
		return locationOf(itemLike).toString();
	}

	public static @NotNull String idOf(@NotNull PlantopiaAdvancement advancement) {
		return locationOf(advancement).toString();
	}

	/* NAME OF ***********************************************************************/

	public static @NotNull String nameOf(@NotNull RegistryObject<?> registryObject) {
		return locationOf(registryObject).getPath();
	}

	public static @NotNull String nameOf(@NotNull TagKey<?> tag) {
		return locationOf(tag).getPath();
	}

	public static @NotNull String nameOf(@NotNull ResourceKey<?> key) {
		return locationOf(key).getPath();
	}

	public static @NotNull String nameOf(@NotNull Block block) {
		return locationOf(block).getPath();
	}

	public static @NotNull String nameOf(@NotNull ItemLike itemLike) {
		return locationOf(itemLike).getPath();
	}

	public static @NotNull String nameOf(@NotNull PlantopiaAdvancement advancement) {
		return locationOf(advancement).getPath();
	}
}
