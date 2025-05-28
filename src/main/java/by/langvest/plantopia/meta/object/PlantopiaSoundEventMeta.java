package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.meta.core.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaSoundEventMeta extends PlantopiaMetaObject<RegistryObject<? extends SoundEvent>> {
	private final MetaType type;

	public PlantopiaSoundEventMeta(RegistryObject<? extends SoundEvent> target, @NotNull MetaProperties properties) {
		super(target);
		type = PlantopiaMetaAccessor.getMetaTypeFrom(properties);
	}

	public String getName() {
		return nameOf(target);
	}

	public SoundEvent getSoundEvent() {
		return target.get();
	}

	public MetaType getType() {
		return type;
	}

	public static final class MetaType extends PlantopiaMetaType<MetaType, MetaProperties> {
		public static final MetaType BLOCK_BRAKE = MetaProperties.of().makeType("block_break");
		public static final MetaType BLOCK_FOOTSTEPS = MetaProperties.of().makeType("block_footsteps");
		public static final MetaType BLOCK_HIT = MetaProperties.of().makeType("block_hit");
		public static final MetaType BLOCK_PLACE = MetaProperties.of().makeType("block_place");
		public static final MetaType BLOCK_FALL = MetaProperties.of().makeType("block_fall");

		private MetaType(String name, MetaProperties properties) {
			super("sound_event", name, properties);
		}
	}

	public static final class MetaProperties extends PlantopiaMetaProperties<MetaType, MetaProperties> {
		private MetaProperties() {}

		private static @NotNull MetaProperties of() {
			return new MetaProperties();
		}

		public static @NotNull MetaProperties copy(@NotNull MetaType type) {
			return MetaProperties.fromType(type);
		}

		private @NotNull MetaType makeType(String name) {
			return new MetaType(name, this);
		}
	}
}