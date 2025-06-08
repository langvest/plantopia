package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.meta.core.PlantopiaMetaAccessor;
import by.langvest.plantopia.meta.core.PlantopiaMetaObject;
import by.langvest.plantopia.meta.core.PlantopiaMetaProperties;
import by.langvest.plantopia.meta.core.PlantopiaMetaType;
import by.langvest.plantopia.meta.property.PlantopiaDisplayNameType;
import by.langvest.plantopia.meta.property.PlantopiaModelType;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaItemMeta extends PlantopiaMetaObject<RegistryObject<? extends Item>> {
	private final MetaType type;
	private final List<ResourceKey<CreativeModeTab>> groups;
	private final PlantopiaModelType modelType;
	private final PlantopiaDisplayNameType displayNameType;
	private final int burnTime;

	public PlantopiaItemMeta(RegistryObject<? extends Item> target, @NotNull MetaProperties properties) {
		super(target);
		type = PlantopiaMetaAccessor.getMetaTypeFrom(properties);
		groups = properties.groups;
		modelType = properties.modelType;
		displayNameType = properties.displayNameType;
		burnTime = properties.burnTime;
	}

	public String getName() {
		return nameOf(target);
	}

	public Item getItem() {
		return target.get();
	}

	public MetaType getType() {
		return type;
	}

	public List<ResourceKey<CreativeModeTab>> getGroups() {
		return groups;
	}

	public PlantopiaModelType getModelType() {
		return modelType;
	}

	public boolean shouldGenerateModel() {
		return type != MetaType.BLOCK && modelType != PlantopiaModelType.NONE && modelType != PlantopiaModelType.CUSTOM;
	}

	public boolean shouldGenerateTranslation() {
		return type.instanceOf(MetaType.ITEM) && displayNameType != PlantopiaDisplayNameType.NONE && displayNameType != PlantopiaDisplayNameType.CUSTOM;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public boolean isBurnable() {
		return this.burnTime > 0;
	}

	public static final class MetaType extends PlantopiaMetaType<MetaType, MetaProperties> {
		public static final MetaType ITEM = MetaProperties.of().makeType("item");
		public static final MetaType BLOCK = MetaProperties.of().makeType("block");
		public static final MetaType ICON = MetaProperties.of().noGroup().makeType("icon");

		private MetaType(String name, MetaProperties properties) {
			super("item", name, properties);
		}
	}

	public static final class MetaProperties extends PlantopiaMetaProperties<MetaType, MetaProperties> {
		private List<ResourceKey<CreativeModeTab>> groups = List.of(PlantopiaCreativeModeTabs.PLANTOPIA);
		private PlantopiaModelType modelType = PlantopiaModelType.GENERATED;
		private PlantopiaDisplayNameType displayNameType = PlantopiaDisplayNameType.GENERATED;
		private int burnTime = -1;

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

		public MetaProperties generatedBurnTime() {
			this.burnTime = -1;
			return this;
		}

		public MetaProperties noBurnTime() {
			this.burnTime = 0;
			return this;
		}

		public MetaProperties customBurnTime(int ticks) {
			this.burnTime = ticks;
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