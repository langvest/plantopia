package by.langvest.plantopia.adv.special;

import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

public class PlantopiaSimpleAdvancement extends PlantopiaAdvancement {
    private final ResourceLocation location;
    private AdvancementHolder instance;
    private final Advancement.Builder builder;

    public PlantopiaSimpleAdvancement(ResourceLocation location) {
        this.builder = Advancement.Builder.advancement();
        this.location = location;
    }

    @Override
    public Advancement.Builder getBuilder() {
        return builder;
    }

    @Override
    @Nullable
    public AdvancementHolder getInstance() {
        return instance;
    }

    public @NotNull ResourceLocation getGroup() {
        var advancementMeta = PlantopiaMetaBuckets.ADVANCEMENT.getValueOrThrow(location);

        return advancementMeta.getGroup();
    }

    @Override
    public ResourceLocation location() {
        return Objects.requireNonNull(location);
    }

    @Override
    public PlantopiaSimpleAdvancement apply(@NotNull Consumer<PlantopiaSimpleAdvancement> consumer) {
        consumer.accept(this);
        return this;
    }

    @Override
    public void save(@NotNull Consumer<AdvancementHolder> saver) {
        var group = getGroup();
        var ownLocation = location();
        var saveLocation = locationFrom(ownLocation.getNamespace(), group.getPath(), ownLocation.getPath());
        instance = builder.build(saveLocation);
        saver.accept(instance);
    }
}