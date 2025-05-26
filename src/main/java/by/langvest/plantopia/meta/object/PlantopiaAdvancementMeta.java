package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.adv.PlantopiaAdvancementTab;
import by.langvest.plantopia.meta.core.*;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.PlantopiaContentHelper.nameOf;
import static by.langvest.plantopia.util.PlantopiaContentHelper.plantopia;
import static by.langvest.plantopia.util.PlantopiaTemplateHelper.*;

@SuppressWarnings("unused")
public class PlantopiaAdvancementMeta extends PlantopiaObjectMeta<PlantopiaAdvancement> {
	private final MetaType type;
	private final FrameType frameType;
	private final ItemStack icon;
	private final PlantopiaAdvancementTab group;
	private final ResourceLocation background;
	private final PlantopiaAdvancement parent;
	private final boolean showToast;
	private final boolean announceToChat;
	private final boolean isHidden;
	private final MutableComponent title;
	private final MutableComponent description;

	public PlantopiaAdvancementMeta(String name, PlantopiaAdvancement object, @NotNull MetaProperties metaProperties) {
		super(name, object);
		type = PlantopiaMetaAccessor.getMetaType(metaProperties);
		frameType = metaProperties.frameType;
		showToast = metaProperties.showToast;
		announceToChat = metaProperties.announceToChat;
		isHidden = metaProperties.isHidden;

		icon = metaProperties.icon;
		if(icon == null) throw new PlantopiaMetaException.Required("icon", type);

		parent = metaProperties.parent;
		if(type != MetaType.ROOT && parent == null) throw new PlantopiaMetaException.Required("parent", type);

		if(type == MetaType.ROOT) {
			group = metaProperties.group;
		} else {
			group = parent.getGroup();
		}
		if(group == null) throw new PlantopiaMetaException.Required("group", type);

		background = metaProperties.background;
		if(type == MetaType.ROOT && background == null) throw new PlantopiaMetaException.Required("background", type);

		title = Component.translatable(advancementTitle(nameOf(group), name));
		description = Component.translatable(advancementDescription(nameOf(group), name));
	}

	public PlantopiaAdvancement getAdvancement() {
		return getObject();
	}

	public MetaType getType() {
		return type;
	}

	public String getPath() {
		return group + "/" + getName();
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

	public MutableComponent getTitle() {
		return title;
	}

	public MutableComponent getDescription() {
		return description;
	}

	public PlantopiaAdvancementTab getGroup() {
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

	public static final class MetaType extends PlantopiaObjectMetaType<MetaType, MetaProperties> {
		public static final MetaType ROOT = new MetaProperties().doNotShowToast().doNotAnnounceToChat().makeType("root");
		public static final MetaType CHILD = new MetaProperties().makeType("child");
		public static final MetaType TASK = MetaProperties.of(CHILD).taskFrame().makeType("task");
		public static final MetaType GOAL = MetaProperties.of(CHILD).goalFrame().makeType("goal");
		public static final MetaType CHALLENGE = MetaProperties.of(CHILD).challengeFrame().makeType("challenge");

		private MetaType(String name, MetaProperties properties) {
			super("advancement", name, properties);
		}
	}

	public static final class MetaProperties extends PlantopiaObjectMetaProperties<MetaType> implements Cloneable {
		private FrameType frameType = FrameType.TASK;
		private ItemStack icon = null;
		private ResourceLocation background = null;
		private PlantopiaAdvancementTab group = null;
		private PlantopiaAdvancement parent = null;
		private boolean showToast = true;
		private boolean announceToChat = true;
		private boolean isHidden = false;

		private MetaProperties() {}

		public static @NotNull MetaProperties of(@NotNull MetaType metaType) {
			return PlantopiaMetaAccessor.getMetaProperties(metaType).clone();
		}

		private @NotNull MetaType makeType(String name) {
			MetaType metaType = new MetaType(name, this);
			PlantopiaMetaAccessor.setRecursiveMetaType(metaType);
			type = metaType;
			return metaType;
		}

		public MetaProperties group(PlantopiaAdvancementTab group) {
			if(type != null && type != MetaType.ROOT) throw new PlantopiaMetaException.UnableToSet("group", type);
			this.group = group;
			return this;
		}

		public MetaProperties parent(PlantopiaAdvancement parent) {
			if(type != null && type == MetaType.ROOT) throw new PlantopiaMetaException.UnableToSet("parent", type);
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

		public MetaProperties background(String name) {
			return background(plantopia(advancementBackground(name)));
		}

		public MetaProperties background(ResourceLocation background) {
			if(type != null && type != MetaType.ROOT) throw new PlantopiaMetaException.UnableToSet("background", type);
			this.background = background;
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

		@Override
		public MetaProperties clone() {
			try {
				return (MetaProperties)super.clone();
			} catch(CloneNotSupportedException e) {
				throw new AssertionError();
			}
		}
	}
}