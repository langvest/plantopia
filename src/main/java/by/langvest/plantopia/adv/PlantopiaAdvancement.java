package by.langvest.plantopia.adv;

import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

public class PlantopiaAdvancement {
	private ResourceLocation identifier = null;
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

	protected PlantopiaAdvancement bindId(ResourceLocation id) {
		if(this.identifier != null) return this;

		this.identifier = id;
		return this;
	}

	public @NotNull ResourceLocation getGroup() {
		var advancementMeta = PlantopiaMetaBuckets.ADVANCEMENT.getValueOrThrow(identifier);

		return advancementMeta.getGroup();
	}

	public @NotNull ResourceLocation getIdentifier() {
		return Objects.requireNonNull(identifier);
	}

	public PlantopiaAdvancement apply(@NotNull Consumer<PlantopiaAdvancement> consumer) {
		consumer.accept(this);
		return this;
	}

	public void save(@NotNull Consumer<Advancement> consumer) {
		var group = getGroup();
		var location = locationFrom(identifier.getNamespace(), group.getPath(), identifier.getPath());
		instance = builder.build(location);
		consumer.accept(instance);
	}
}