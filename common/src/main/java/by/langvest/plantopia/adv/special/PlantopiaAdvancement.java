package by.langvest.plantopia.adv.special;

import by.langvest.toolkit.util.LocationLike;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class PlantopiaAdvancement implements LocationLike {
    public abstract Advancement.Builder getBuilder();

    @Nullable
    public abstract AdvancementHolder getInstance();

    public abstract PlantopiaAdvancement apply(@NotNull Consumer<PlantopiaSimpleAdvancement> consumer);

    public abstract void save(@NotNull Consumer<AdvancementHolder> saver);
}
