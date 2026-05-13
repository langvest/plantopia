package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.meta.property.PlantopiaTagType;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.toolkit.meta.MetaAccessor;
import by.langvest.plantopia.meta.property.PlantopiaDisplayNameType;
import by.langvest.plantopia.meta.property.PlantopiaModelType;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.toolkit.meta.SimpleMetaObject;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaItemMeta extends SimpleMetaObject<Item> {
	private final MetaType type;
	private final List<ResourceKey<CreativeModeTab>> groups;
	private final Supplier<Item.Properties> behaviourProperties;
	private final PlantopiaModelType modelType;
	private final PlantopiaDisplayNameType displayNameType;
	private final PlantopiaOrderType orderType;
	private final @Nullable Supplier<? extends ItemLike> goesAfter;
	private final PlantopiaTagType tagType;
	private final boolean hasCustomRenderer;
	private final int burnTime;

	public PlantopiaItemMeta(ResourceLocation identifier, @NotNull MetaProperties properties) {
		super(identifier, PlantopiaRegistries.ITEM.supposeValue(identifier));
		type = MetaAccessor.getMetaTypeFrom(properties);
		groups = properties.groups;
		modelType = properties.modelType;
		displayNameType = properties.displayNameType;
		orderType = properties.orderType;
		goesAfter = properties.goesAfter;
		burnTime = properties.burnTime;
		behaviourProperties = properties.behaviourProperties;
		hasCustomRenderer = properties.hasCustomRenderer;
		tagType = properties.tagType;
	}

	public Item.Properties createBehaviourProperties() {
		return behaviourProperties.get();
	}

	public MetaType getType() {
		return type;
	}

	public boolean hasCustomRenderer() {
		return hasCustomRenderer;
	}

	public List<ResourceKey<CreativeModeTab>> getGroups() {
		return groups;
	}

	public PlantopiaModelType getModelType() {
		return modelType;
	}

	public boolean shouldGenerateModel() {
		return !type.instanceOfExcept(MetaType.BLOCK, Set.of(MetaType.SIGN)) && modelType != PlantopiaModelType.NONE && modelType != PlantopiaModelType.CUSTOM;
	}

	public boolean shouldGenerateTranslation() {
		return !type.instanceOf(MetaType.BLOCK) && displayNameType != PlantopiaDisplayNameType.NONE && displayNameType != PlantopiaDisplayNameType.CUSTOM;
	}

	public boolean shouldGenerateTag() {
		return tagType != PlantopiaTagType.NONE && tagType != PlantopiaTagType.CUSTOM;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public boolean isBurnable() {
		return this.burnTime > 0;
	}

	public PlantopiaOrderType getOrderType() {
		return orderType;
	}

	public @Nullable ItemLike getGoesAfter() {
		return goesAfter == null ? null : goesAfter.get();
	}

	public static class MetaType extends SimpleMetaObject.MetaType<MetaType, MetaProperties> {
		public static final MetaType ITEM = MetaProperties.create()
			.noTag()
			.makeType("item");

		public static final MetaType BOAT = MetaProperties.create()
			.stacksTo(1)
			.makeType("boat");

		public static final MetaType CHEST_BOAT = MetaProperties.of(BOAT)
			.makeType("chest_boat");

		public static final MetaType FOOD = MetaProperties.create()
			.order(PlantopiaOrderType.FOOD)
			.makeType("food");

		public static final MetaType BLOCK = MetaProperties.create()
			.order(PlantopiaOrderType.BLOCK)
			.noTag()
			.makeType("block");

		public static final MetaType SIGN = MetaProperties.of(BLOCK)
			.stacksTo(16)
			.makeType("sign");

		public static final MetaType COBBLESTONE_SHARD_BLOCK = MetaProperties.of(BLOCK)
			.order(PlantopiaOrderType.COBBLESTONE_SHARD)
			.makeType("cobblestone_shard_block");

		public static final MetaType SHELL_BLOCK = MetaProperties.of(BLOCK)
			.order(PlantopiaOrderType.SHELL)
			.makeType("shell_block");

		public static final MetaType WATERLILY_BLOCK = MetaProperties.of(BLOCK)
			.order(PlantopiaOrderType.WET_PLANT)
			.makeType("waterlily_block");

		public static final MetaType LUCKY_DAISY_BLOCK = MetaProperties.of(BLOCK)
			.order(PlantopiaOrderType.FLOWER)
			.customModel()
			.makeType("lucky_daisy_block");

		public static final MetaType ICON = MetaProperties.create()
			.noGroup()
			.noDisplayName()
			.stacksTo(1)
			.makeType("icon");

		private MetaType(String name, MetaProperties properties) {
			super(plantopia(name), properties);
		}
	}

	public static class MetaProperties extends SimpleMetaObject.MetaProperties<MetaType, MetaProperties> {
		private List<ResourceKey<CreativeModeTab>> groups = List.of(PlantopiaCreativeModeTabs.MAIN);
		private Supplier<Item.Properties> behaviourProperties = Item.Properties::new;
		private PlantopiaModelType modelType = PlantopiaModelType.GENERATED;
		private PlantopiaDisplayNameType displayNameType = PlantopiaDisplayNameType.GENERATED;
		private PlantopiaTagType tagType = PlantopiaTagType.GENERATED;
		private boolean hasCustomRenderer = false;
		private PlantopiaOrderType orderType = PlantopiaOrderType.ITEM;
		private @Nullable Supplier<? extends ItemLike> goesAfter = null;
		private int burnTime = -1;

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

		public MetaProperties modifyBehaviour(Supplier<Item.Properties> properties) {
			this.behaviourProperties = properties;
			return this;
		}

		public MetaProperties resetBehaviour() {
			return modifyBehaviour(Item.Properties::new);
		}

		public MetaProperties modifyBehaviour(Function<Item.Properties, Item.Properties> properties) {
			var prevBehaviourProperties = this.behaviourProperties;
			this.behaviourProperties = () -> properties.apply(prevBehaviourProperties.get());
			return this;
		}

		public MetaProperties stacksTo(int maxStackSize) {
			return modifyBehaviour(properties -> properties.stacksTo(maxStackSize));
		}

		public MetaProperties food(FoodProperties foodProperties) {
			return modifyBehaviour(properties -> properties.food(foodProperties));
		}

		public MetaProperties hasCustomRenderer() {
			this.hasCustomRenderer = true;
			return this;
		}

		public MetaProperties noCustomRenderer() {
			this.hasCustomRenderer = false;
			return this;
		}

		public MetaProperties noBurnTime() {
			this.burnTime = -1;
			return this;
		}

		public MetaProperties burnTime(int ticks) {
			this.burnTime = ticks;
			return this;
		}

		public MetaProperties order(PlantopiaOrderType orderType) {
			this.orderType = orderType;
			return this;
		}

		public MetaProperties goesAfter(Supplier<? extends ItemLike> itemLike) {
			this.goesAfter = itemLike;
			return this;
		}

		public MetaProperties group(@NotNull Collection<ResourceKey<CreativeModeTab>> groups) {
			this.groups = groups.stream().toList();
			return this;
		}

		@SafeVarargs
		public final MetaProperties group(ResourceKey<CreativeModeTab>... groups) {
			this.groups = Arrays.stream(groups).toList();
			return this;
		}

		public MetaProperties noGroup() {
			this.groups = Collections.emptyList();
			return this;
		}

		public MetaProperties noTag() {
			this.tagType = PlantopiaTagType.NONE;
			return this;
		}

		public MetaProperties customTag() {
			this.tagType = PlantopiaTagType.CUSTOM;
			return this;
		}

		public MetaProperties generatedTag() {
			this.tagType = PlantopiaTagType.GENERATED;
			return this;
		}

		public MetaProperties noModel() {
			this.modelType = PlantopiaModelType.NONE;
			return this;
		}

		public MetaProperties customModel() {
			this.modelType = PlantopiaModelType.CUSTOM;
			return this;
		}

		public MetaProperties generatedModel() {
			this.modelType = PlantopiaModelType.GENERATED;
			return this;
		}

		public MetaProperties noDisplayName() {
			this.displayNameType = PlantopiaDisplayNameType.NONE;
			return this;
		}

		public MetaProperties customDisplayName() {
			this.displayNameType = PlantopiaDisplayNameType.CUSTOM;
			return this;
		}

		public MetaProperties generatedDisplayName() {
			this.displayNameType = PlantopiaDisplayNameType.GENERATED;
			return this;
		}
	}
}