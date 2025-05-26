package by.langvest.plantopia.adv;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlantopiaAdvancementTab {
	private final ResourceLocation location;

	PlantopiaAdvancementTab(ResourceLocation location) {
		this.location = location;
	}

	public @NotNull ResourceLocation location() {
		return Objects.requireNonNull(this.location);
	}
}
