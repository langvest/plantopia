package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.meta.core.PlantopiaMetaAccessor;
import by.langvest.plantopia.meta.core.PlantopiaObjectMeta;
import by.langvest.plantopia.meta.core.PlantopiaObjectMetaProperties;
import by.langvest.plantopia.meta.core.PlantopiaObjectMetaType;
import by.langvest.plantopia.meta.property.PlantopiaModelType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class PlantopiaItemMeta extends PlantopiaObjectMeta<RegistryObject<? extends Item>> {
	private final MetaType type;
	private final PlantopiaModelType modelType;
	private final int burnTime;

	public PlantopiaItemMeta(String name, RegistryObject<? extends Item> object, @NotNull MetaProperties metaProperties) {
		super(name, object);
		type = PlantopiaMetaAccessor.getMetaType(metaProperties);
		modelType = metaProperties.modelType;
		burnTime = metaProperties.burnTime;
	}

	public Item getItem() {
		return getObject().get();
	}

	public MetaType getType() {
		return type;
	}

	@Nullable
	public CreativeModeTab getGroup() {
		return getItem().getItemCategory();
	}

	public PlantopiaModelType getModelType() {
		return modelType;
	}

	public boolean shouldGenerateModel() {
		return type != MetaType.BLOCK && modelType != PlantopiaModelType.NONE && modelType != PlantopiaModelType.CUSTOM;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public boolean isBurnable() {
		return this.burnTime > 0;
	}

	public static final class MetaType extends PlantopiaObjectMetaType<MetaType, MetaProperties> {
		public static final MetaType BLOCK = new MetaProperties().makeType("block");
		public static final MetaType ICON = new MetaProperties().makeType("icon");

		private MetaType(String name, MetaProperties properties) {
			super("item", name, properties);
		}
	}

	public static final class MetaProperties extends PlantopiaObjectMetaProperties<MetaType> implements Cloneable {
		private PlantopiaModelType modelType = PlantopiaModelType.GENERATED;
		private int burnTime = -1;

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