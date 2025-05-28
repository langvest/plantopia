package by.langvest.plantopia.adv;

import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationFrom;

public class PlantopiaAdvancement {
	private ResourceLocation id = null;
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
		if(this.id != null) return this;

		this.id = id;
		return this;
	}

	public @NotNull ResourceLocation getGroup() {
		var advancementMeta = PlantopiaMetaRegistries.ADVANCEMENTS.getValueOrThrow(this);

		return advancementMeta.getGroup();
	}

	public @NotNull ResourceLocation getId() {
		return Objects.requireNonNull(id);
	}

	public PlantopiaAdvancement apply(@NotNull Consumer<PlantopiaAdvancement> consumer) {
		consumer.accept(this);
		return this;
	}

	public void save(@NotNull Consumer<Advancement> consumer) {
		var id = getId();
		var group = getGroup();
		var location = locationFrom(id.getNamespace(), group.getPath(), id.getPath());
		instance = builder.build(location);
		consumer.accept(instance);
	}
}