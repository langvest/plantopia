package by.langvest.toolkit.meta;

import by.langvest.toolkit.util.LocationLike;
import by.langvest.toolkit.collection.Streamable;
import com.google.common.collect.Maps;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MetaBucket<Meta extends MetaObject<?>> implements LocationLike, Streamable<Meta> {
    protected final ResourceLocation location;
    protected final HashMap<ResourceLocation, Meta> storage = Maps.newLinkedHashMap();

    public MetaBucket(ResourceLocation location) {
        this.location = location;
    }

    @Override
    public ResourceLocation location() {
        return location;
    }

    public boolean hasKey(ResourceLocation key) {
        return storage.containsKey(key);
    }

    public Optional<Meta> getValue(ResourceLocation key) {
        return Optional.ofNullable(storage.get(key));
    }

    public @NotNull Meta getValueOrThrow(ResourceLocation key) {
        var meta = getValue(key);
        if (meta.isPresent()) return meta.get();
        throw new NoSuchElementException(String.format("Cannot get meta for the key '%s' as it does not exist in the meta bucket %s", key, this));
    }

    public Meta associate(ResourceLocation key, Meta meta) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(meta);

        if (hasKey(key)) {
            throw new IllegalIcuArgumentException(String.format("Cannot add a new association for the key '%s' as it already exists in the meta bucket %s", key, this));
        }

        storage.put(key, meta);

        return meta;
    }

    @Override
    public String toString() {
        return String.format("%s{%s}", getClass().getSimpleName(), location());
    }

    @Override
    public @NotNull Iterator<Meta> iterator() {
        return storage.values().iterator();
    }
}
