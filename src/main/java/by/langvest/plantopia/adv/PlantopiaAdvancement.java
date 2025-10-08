package by.langvest.plantopia.adv;

import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.toolkit.util.LocationRepresentable;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

public class PlantopiaAdvancement implements LocationRepresentable {
	private ResourceLocation location = null;
	private Advancement instance = null;
	private final Advancement.Builder builder;

	public PlantopiaAdvancement() {
		this.builder = Advancement.Builder.advancement();
	}

	public Advancement.Builder getBuilder() {
		return builder;
	}

	@Nullable
	public Advancement getInstance() {
		return instance;
	}

	protected PlantopiaAdvancement bindLocation(ResourceLocation location) {
		if(this.location != null) return this;

		this.location = location;
		return this;
	}

	public @NotNull ResourceLocation getGroup() {
		var advancementMeta = PlantopiaMetaBuckets.ADVANCEMENT.getValueOrThrow(location);

		return advancementMeta.getGroup();
	}

	@Override
	public ResourceLocation getLocation() {
		return Objects.requireNonNull(location);
	}

	public PlantopiaAdvancement apply(@NotNull Consumer<PlantopiaAdvancement> consumer) {
		consumer.accept(this);
		return this;
	}

	public void save(@NotNull Consumer<Advancement> consumer) {
		var group = getGroup();
		var locationToSave = locationFrom(location.getNamespace(), group.getPath(), location.getPath());
		instance = builder.build(locationToSave);
		consumer.accept(instance);
	}
}