package by.langvest.plantopia.util;

import by.langvest.plantopia.Plantopia;
import by.langvest.toolkit.registry.RegistryObject;
import by.langvest.toolkit.util.TagSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public class PlantopiaTagSet<T> extends TagSet<T, PlantopiaTagSet<T>> {
    @Contract(" -> new")
    public static <T> @NotNull PlantopiaTagSet<T> newTagSet() {
        return new PlantopiaTagSet<>();
    }

    @SafeVarargs
    public final PlantopiaTagSet<T> add(T... values) {
        if (values != null) {
            for (T value : values) {
                var valueKey = Plantopia.getPlatform().getRegistryHelper().getResourceKeyOrThrow(value);

                if (!contains(valueKey)) {
                    this.values.add(valueKey);
                }
            }
        }

        return this;
    }

    @SafeVarargs
    public final PlantopiaTagSet<T> add(RegistryObject<T>... values) {
        if (values != null) {
            for (RegistryObject<T> value : values) {
                var valueKey = Plantopia.getPlatform().getRegistryHelper().getResourceKeyOrThrow(value.get());

                if (!contains(valueKey)) {
                    this.values.add(valueKey);
                }
            }
        }

        return this;
    }
}
