package by.langvest.toolkit.forge;

import by.langvest.toolkit.platform.RegistryAdapter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class ForgeDeferredRegistryAdapter<T> extends RegistryAdapter<T> {
    protected Supplier<IForgeRegistry<T>> registrySupplier;

    public ForgeDeferredRegistryAdapter(Supplier<IForgeRegistry<T>> registrySupplier) {
        this.registrySupplier = registrySupplier;
    }

    public IForgeRegistry<T> getForgeRegistry() {
        return Objects.requireNonNull(registrySupplier.get());
    }

    @Override
    public ResourceKey<? extends Registry<?>> getRegistryKey() {
        return getForgeRegistry().getRegistryKey();
    }

    @Override
    public Optional<ResourceKey<T>> getKey(T value) {
        return getForgeRegistry().getResourceKey(value);
    }

    @Override
    public Optional<T> getValue(ResourceLocation key) {
        return Optional.ofNullable(getForgeRegistry().getValue(key));
    }

    @Override
    public void register(ResourceLocation identifier, @NotNull Supplier<T> supplier) {
        getForgeRegistry().register(identifier, supplier.get());
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return getForgeRegistry().iterator();
    }
}
