package by.langvest.toolkit.meta;

import by.langvest.toolkit.util.LocationLike;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class MetaObject<T> implements Supplier<T>, LocationLike {
    protected final ResourceLocation identifier;

    public MetaObject(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    public ResourceLocation getIdentifier() {
        return identifier;
    }

    @Override
    public ResourceLocation location() {
        return getIdentifier();
    }

    public String getName() {
        return identifier.getPath();
    }

    public String getNamespace() {
        return identifier.getNamespace();
    }

    @Override
    public String toString() {
        return String.format("%s{%s}", getClass().getSimpleName(), getIdentifier());
    }
}
