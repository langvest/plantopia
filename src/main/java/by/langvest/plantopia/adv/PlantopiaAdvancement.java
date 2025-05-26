package by.langvest.plantopia.adv;

import by.langvest.plantopia.meta.store.PlantopiaMetaStore;
import by.langvest.plantopia.util.PlantopiaContentHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class PlantopiaAdvancement {
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
		this.location = location;
		return this;
	}

	@Nullable
	public PlantopiaAdvancementTab getGroup() {
		var advancementMeta = PlantopiaMetaStore.getAdvancement(this);

		if(advancementMeta == null) return null;

		return advancementMeta.getGroup();
	}

	public ResourceLocation location() {
		return Objects.requireNonNull(this.location);
	}

	public PlantopiaAdvancement apply(@NotNull Consumer<PlantopiaAdvancement> consumer) {
		consumer.accept(this);
		return this;
	}

	public void save(@NotNull Consumer<Advancement> consumer) {
		var group = getGroup();
		ResourceLocation location = PlantopiaContentHelper.location(location().getNamespace(), group.location().getPath(), location().getPath());
		instance = builder.build(location);
		consumer.accept(instance);
	}
}