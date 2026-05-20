package by.langvest.toolkit.util;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Catalog<K, V> implements Streamable<V> {
    protected final HashMap<K, V> storage = Maps.newLinkedHashMap();
    protected final @Nullable Function<Catalog<K, V>, Catalog<K, V>> transitiveCatalogFactory;

    public Catalog(@Nullable Function<Catalog<K, V>, Catalog<K, V>> transitiveCatalogFactory) {
        this.transitiveCatalogFactory = transitiveCatalogFactory;
    }

    public Catalog() {
        this(null);
    }

    @Contract(" -> new")
    public static <K, V> @NotNull Catalog<K, V> newCatalog() {
        return new Catalog<>();
    }

    @Contract("_ -> new")
    public static <K, V> @NotNull Catalog<K, V> newCatalog(Function<Catalog<K, V>, Catalog<K, V>> transitiveCatalogFactory) {
        return new Catalog<>(transitiveCatalogFactory);
    }

    @SafeVarargs
    public static <K, V> @NotNull Catalog<K, V> merge(Catalog<K, V> @NotNull ... catalogs) {
        var mergedCatalog = new Catalog<K, V>();

        for (var catalog : catalogs) {
            for (var entry : catalog.entrySet()) {
                mergedCatalog.add(entry.getKey(), entry.getValue());
            }
        }

        return mergedCatalog;
    }

    protected Catalog<K, V> lookupCatalog() {
        if (transitiveCatalogFactory != null) {
            var transitiveCatalog = transitiveCatalogFactory.apply(this);

            if (transitiveCatalog != null && transitiveCatalog != this) {
                var mergedCatalog = new Catalog<K, V>();

                for (var entry : storage.entrySet()) {
                    mergedCatalog.add(entry.getKey(), entry.getValue());
                }

                for (var entry : transitiveCatalog.entrySet()) {
                    mergedCatalog.add(entry.getKey(), entry.getValue());
                }

                return mergedCatalog;
            }
        }

        return this;
    }

    protected HashMap<K, V> lookupStorage() {
        return lookupCatalog().storage;
    }

    public boolean hasKey(K key) {
        return lookupStorage().containsKey(key);
    }

    public Optional<V> getValue(K key) {
        return Optional.ofNullable(lookupStorage().get(key));
    }

    public @NotNull V getValueOrThrow(K key) {
        var value = getValue(key);
        if (value.isPresent()) return value.get();
        throw new NoSuchElementException(String.format("Cannot get value for the key '%s' as it does not exist in the catalog", key));
    }

    public Map.Entry<K, V> add(K key, V value) {
        if (storage.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Cannot add a new value for the key '%s' as it already exists in the catalog", key));
        }

        storage.put(key, value);
        return Map.entry(key, value);
    }

    public @NotNull Collection<V> values() {
        return lookupStorage().values();
    }

    @Override
    public @NotNull Iterator<V> iterator() {
        return lookupStorage().values().iterator();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return lookupStorage().entrySet();
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        lookupStorage().forEach(action);
    }
}
