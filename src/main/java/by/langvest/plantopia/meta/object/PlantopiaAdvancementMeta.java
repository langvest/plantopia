package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.meta.core.*;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaAdvancementMeta extends PlantopiaMetaObject<PlantopiaAdvancement> {
	private final MetaType type;
	private final FrameType frameType;
	private final ItemStack icon;
	private final ResourceLocation group;
	private final ResourceLocation background;
	private final PlantopiaAdvancement parent;
	private final boolean showToast;
	private final boolean announceToChat;
	private final boolean isHidden;

	public PlantopiaAdvancementMeta(PlantopiaAdvancement target, @NotNull MetaProperties properties) {
		super(target);
		type = PlantopiaMetaAccessor.getMetaTypeFrom(properties);
		frameType = properties.frameType;
		showToast = properties.showToast;
		announceToChat = properties.announceToChat;
		isHidden = properties.isHidden;
		icon = properties.icon;
		parent = properties.parent;
		background = properties.background;

		if(icon == null) throw new PlantopiaMetaException.Required("icon", type);
		if(type != MetaType.ROOT && parent == null) throw new PlantopiaMetaException.Required("parent", type);

		group = type == MetaType.ROOT ? properties.group : parent.getGroup();

		if(group == null) throw new PlantopiaMetaException.Required("group", type);
		if(type == MetaType.ROOT && background == null) throw new PlantopiaMetaException.Required("background", type);
	}

	public String getName() {
		return nameOf(target);
	}

	public PlantopiaAdvancement getAdvancement() {
		return target;
	}

	public MetaType getType() {
		return type;
	}

	@Nullable
	public PlantopiaAdvancement getParent() {
		return parent;
	}

	public FrameType getFrameType() {
		return frameType;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public String getTitleKey() {
		return PlantopiaTemplateHelper.getAdvancementTitleKey(group.getPath(), getName());
	}

	public String getDescriptionKey() {
		return PlantopiaTemplateHelper.getAdvancementDescriptionKey(group.getPath(), getName());
	}

	public MutableComponent getTitle() {
		return Component.translatable(getTitleKey());
	}

	public MutableComponent getDescription() {
		return Component.translatable(getDescriptionKey());
	}

	public ResourceLocation getGroup() {
		return group;
	}

	@Nullable
	public ResourceLocation getBackground() {
		return background;
	}

	public boolean shouldShowToast() {
		return showToast;
	}

	public boolean shouldAnnounceToChat() {
		return announceToChat;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public static final class MetaType extends PlantopiaMetaType<MetaType, MetaProperties> {
		public static final MetaType ROOT = MetaProperties.of().doNotShowToast().doNotAnnounceToChat().makeType("root");
		public static final MetaType CHILD = MetaProperties.of().makeType("child");
		public static final MetaType TASK = MetaProperties.copy(CHILD).taskFrame().makeType("task");
		public static final MetaType GOAL = MetaProperties.copy(CHILD).goalFrame().makeType("goal");
		public static final MetaType CHALLENGE = MetaProperties.copy(CHILD).challengeFrame().makeType("challenge");

		private MetaType(String name, MetaProperties properties) {
			super("advancement", name, properties);
		}
	}

	public static final class MetaProperties extends PlantopiaMetaProperties<MetaType, MetaProperties> {
		private FrameType frameType = FrameType.TASK;
		private ItemStack icon = null;
		private ResourceLocation background = null;
		private ResourceLocation group = null;
		private PlantopiaAdvancement parent = null;
		private boolean showToast = true;
		private boolean announceToChat = true;
		private boolean isHidden = false;

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

		public MetaProperties group(String groupName) {
			if(type != null && !type.equals(MetaType.ROOT)) throw new PlantopiaMetaException.UnableToSet("group", type);
			return group(plantopiaLocationFrom(groupName));
		}

		public MetaProperties group(ResourceLocation groupLocation) {
			if(type != null && !type.equals(MetaType.ROOT)) throw new PlantopiaMetaException.UnableToSet("group", type);
			this.group = groupLocation;
			return this;
		}

		public MetaProperties parent(PlantopiaAdvancement parent) {
			if(type != null && type.equals(MetaType.ROOT)) throw new PlantopiaMetaException.UnableToSet("parent", type);
			this.parent = parent;
			return this;
		}

		public <T extends ItemLike> MetaProperties icon(@NotNull Supplier<T> icon) {
			return icon(icon.get());
		}

		public MetaProperties icon(@NotNull ItemLike icon) {
			return icon(new ItemStack(icon.asItem()));
		}

		public MetaProperties icon(ItemStack icon) {
			this.icon = icon;
			return this;
		}

		public MetaProperties taskFrame() {
			this.frameType = FrameType.TASK;
			return this;
		}

		public MetaProperties goalFrame() {
			this.frameType = FrameType.GOAL;
			return this;
		}

		public MetaProperties challengeFrame() {
			this.frameType = FrameType.CHALLENGE;
			return this;
		}

		public MetaProperties background(String textureName) {
			String backgroundPath = PlantopiaTemplateHelper.getAdvancementBackgroundPath(textureName);

			return background(plantopiaLocationFrom(backgroundPath));
		}

		public MetaProperties background(ResourceLocation backgroundLocation) {
			if(type != null && !type.equals(MetaType.ROOT)) throw new PlantopiaMetaException.UnableToSet("background", type);
			this.background = backgroundLocation;
			return this;
		}

		public MetaProperties showToast() {
			this.showToast = true;
			return this;
		}

		public MetaProperties doNotShowToast() {
			this.showToast = false;
			return this;
		}

		public MetaProperties announceToChat() {
			this.announceToChat = true;
			return this;
		}

		public MetaProperties doNotAnnounceToChat() {
			this.announceToChat = false;
			return this;
		}

		public MetaProperties hidden() {
			this.isHidden = true;
			return this;
		}

		public MetaProperties notHidden() {
			this.isHidden = false;
			return this;
		}
	}
}