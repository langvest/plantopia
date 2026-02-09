package by.langvest.plantopia.adv.special;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

public class PlantopiaSimpleAdvancement extends PlantopiaAdvancement {
	private ResourceLocation location;
	private Advancement instance;
	private final Advancement.Builder builder;

	public PlantopiaSimpleAdvancement() {
		this.builder = Advancement.Builder.advancement();
	}

	@Override
	public Advancement.Builder getBuilder() {
		return builder;
	}

	@Override
	@Nullable
	public Advancement getInstance() {
		return instance;
	}

	@Override
	protected void bindLocation(ResourceLocation location) {
		if(this.location != null) return;

		this.location = location;
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
	public void save(@NotNull Consumer<Advancement> consumer) {
		var group = getGroup();
		var ownLocation = location();
		var saveLocation = locationFrom(ownLocation.getNamespace(), group.getPath(), ownLocation.getPath());
		instance = builder.build(saveLocation);
		consumer.accept(instance);
	}
}