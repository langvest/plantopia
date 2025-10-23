package by.langvest.plantopia.adv;

import by.langvest.plantopia.adv.special.PlantopiaSimpleAdvancement;
import by.langvest.toolkit.util.LocationLike;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class PlantopiaAdvancement implements LocationLike {
    public abstract Advancement.Builder getBuilder();

    @Nullable
    public abstract Advancement getInstance();

    protected abstract void bindLocation(ResourceLocation location);

    public abstract PlantopiaAdvancement apply(@NotNull Consumer<PlantopiaSimpleAdvancement> consumer);

    public abstract void save(@NotNull Consumer<Advancement> consumer);
}
