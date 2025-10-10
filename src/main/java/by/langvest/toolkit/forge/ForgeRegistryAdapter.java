package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.RegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;

public class ForgeRegistryAdapter<T> extends RegistryAdapter<T> {
	protected IForgeRegistry<T> forgeRegistry;

	public ForgeRegistryAdapter(IForgeRegistry<T> registry) {
		this.forgeRegistry = registry;
	}

	public IForgeRegistry<T> getForgeRegistry() {
		return forgeRegistry;
	}

	@Override
	public ResourceKey<? extends Registry<?>> getRegistryKey() {
		return getForgeRegistry().getRegistryKey();
	}

	@Override
	public Optional<ResourceLocation> getKey(T value) {
		return Optional.ofNullable(getForgeRegistry().getKey(value));
	}

	@Override
	public Optional<T> getValue(ResourceLocation key) {
		return Optional.ofNullable(getForgeRegistry().getValue(key));
	}

	@Override
	public @NotNull Iterator<T> iterator() {
		return getForgeRegistry().iterator();
	}
}
