package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.adv.PlantopiaAdvancement;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.util.helper.PlantopiaTemplateHelper;
import by.langvest.toolkit.meta.*;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaAdvancementMeta extends SimpleMetaObject<PlantopiaAdvancement> {
	private final MetaType type;
	private final FrameType frameType;
	private final ItemStack icon;
	private final ResourceLocation group;
	private final ResourceLocation background;
	private final RegistryObject<PlantopiaAdvancement> parent;
	private final boolean showToast;
	private final boolean announceToChat;
	private final boolean isHidden;

	public PlantopiaAdvancementMeta(ResourceLocation identifier, @NotNull MetaProperties properties) {
		super(identifier, PlantopiaRegistries.ADVANCEMENT.supposeValue(identifier));
		type = MetaAccessor.getMetaTypeFrom(properties);
		frameType = properties.frameType;
		showToast = properties.showToast;
		announceToChat = properties.announceToChat;
		isHidden = properties.isHidden;
		icon = properties.icon;
		parent = properties.parent;
		background = properties.background;

		if(icon == null) throw new MetaException.Required("icon", type);
		if(type != MetaType.ROOT && parent == null) throw new MetaException.Required("parent", type);

		if(type == MetaType.ROOT) {
			group = properties.group;
		} else {
			var parentMeta = PlantopiaMetaBuckets.ADVANCEMENT.getValueOrThrow(parent.getIdentifier());
			group = parentMeta.getGroup();
		}

		if(group == null) throw new MetaException.Required("group", type);
		if(type == MetaType.ROOT && background == null) throw new MetaException.Required("background", type);
	}

	public MetaType getType() {
		return type;
	}

	@Nullable
	public PlantopiaAdvancement getParent() {
		if(parent == null) return null;
		return parent.get();
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

	public static class MetaType extends SimpleMetaObject.MetaType<MetaType, MetaProperties> {
		public static final MetaType ROOT = MetaProperties.create()
			.doNotShowToast()
			.doNotAnnounceToChat()
			.makeType("root");

		public static final MetaType CHILD = MetaProperties.create()
			.makeType("child");

		public static final MetaType TASK = MetaProperties.of(CHILD)
			.taskFrame()
			.makeType("task");

		public static final MetaType GOAL = MetaProperties.of(CHILD)
			.goalFrame()
			.makeType("goal");

		public static final MetaType CHALLENGE = MetaProperties.of(CHILD)
			.challengeFrame()
			.makeType("challenge");

		private MetaType(String name, MetaProperties properties) {
			super(plantopia(name), properties);
		}
	}

	public static class MetaProperties extends SimpleMetaObject.MetaProperties<MetaType, MetaProperties> {
		private FrameType frameType = FrameType.TASK;
		private ItemStack icon = null;
		private ResourceLocation background = null;
		private ResourceLocation group = null;
		private RegistryObject<PlantopiaAdvancement> parent = null;
		private boolean showToast = true;
		private boolean announceToChat = true;
		private boolean isHidden = false;

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

		public MetaProperties group(String groupName) {
			if(type != null && !type.equals(MetaType.ROOT)) throw new MetaException.UnableToSet("group", type);
			return group(plantopia(groupName));
		}

		public MetaProperties group(ResourceLocation groupLocation) {
			if(type != null && !type.equals(MetaType.ROOT)) throw new MetaException.UnableToSet("group", type);
			this.group = groupLocation;
			return this;
		}

		public MetaProperties parent(RegistryObject<PlantopiaAdvancement> parent) {
			if(type != null && type.equals(MetaType.ROOT)) throw new MetaException.UnableToSet("parent", type);
			this.parent = parent;
			return this;
		}

		public <T extends ItemLike> MetaProperties icon(@NotNull Supplier<T> icon) {
			return icon(icon.get());
		}

		public MetaProperties icon(@NotNull ItemLike icon) {
			return icon(icon.asItem().getDefaultInstance());
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

			return background(plantopia(backgroundPath));
		}

		public MetaProperties background(ResourceLocation backgroundLocation) {
			if(type != null && !type.equals(MetaType.ROOT)) throw new MetaException.UnableToSet("background", type);
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