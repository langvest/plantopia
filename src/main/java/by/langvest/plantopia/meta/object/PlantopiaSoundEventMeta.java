package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.meta.MetaAccessor;
import by.langvest.toolkit.meta.SimpleMetaObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaSoundEventMeta extends SimpleMetaObject<SoundEvent> {
	private final MetaType type;

	public PlantopiaSoundEventMeta(ResourceLocation identifier, @NotNull MetaProperties properties) {
		super(identifier, PlantopiaRegistries.SOUND_EVENT.supposeValue(identifier));
		type = MetaAccessor.getMetaTypeFrom(properties);
	}

	public MetaType getType() {
		return type;
	}

	public static class MetaType extends SimpleMetaObject.MetaType<MetaType, MetaProperties> {
		public static final MetaType BLOCK_BRAKE = MetaProperties.create().makeType("block_break");
		public static final MetaType BLOCK_FOOTSTEPS = MetaProperties.create().makeType("block_footsteps");
		public static final MetaType BLOCK_HIT = MetaProperties.create().makeType("block_hit");
		public static final MetaType BLOCK_PLACE = MetaProperties.create().makeType("block_place");
		public static final MetaType BLOCK_FALL = MetaProperties.create().makeType("block_fall");

		private MetaType(String name, MetaProperties properties) {
			super(plantopiaLocationFrom("sound_event", name), properties);
		}
	}

	public static class MetaProperties extends SimpleMetaObject.MetaProperties<MetaType, MetaProperties> {
		private MetaProperties() {}

		private static @NotNull MetaProperties create() {
			return new MetaProperties();
		}

		public static @NotNull MetaProperties of(@NotNull MetaType type) {
			return MetaProperties.fromType(type);
		}

		private @NotNull MetaType makeType(String name) {
			return new MetaType(name, this);
		}
	}
}