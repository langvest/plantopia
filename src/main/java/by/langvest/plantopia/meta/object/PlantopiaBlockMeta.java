package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.block.PlantopiaCompats.*;
import by.langvest.plantopia.meta.core.*;
import by.langvest.plantopia.meta.property.*;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaBlockMeta extends PlantopiaMetaObject<RegistryObject<? extends Block>> {
	private final MetaType type;
	private final List<ResourceKey<CreativeModeTab>> groups;
	private final PlantopiaBlockHeightType blockHeightType;
	private final PlantopiaBlockWidthType blockWidthType;
	private final PlantopiaBlockItemType itemType;
	private final PlantopiaRenderType renderType;
	private final PlantopiaModelType modelType;
	private final PlantopiaBlockDropType dropType;
	private final PlantopiaRecipeType recipeType;
	private final PlantopiaDisplayNameType displayNameType;
	private final PlantopiaTintType tintType;
	private final PlantopiaOrderType orderType;
	private final int encouragement;
	private final int flammability;
	private final float compostability;
	private final boolean isPottable;
	private final boolean isIgnoredByBees;
	private final boolean isPreferredByBees;
	private final boolean shouldTintParticles;
	private final boolean shouldTintItem;
	private final Item dye;
	private final int burnTime;

	public PlantopiaBlockMeta(RegistryObject<? extends Block> target, @NotNull MetaProperties properties) {
		super(target);
		type = PlantopiaMetaAccessor.getMetaTypeFrom(properties);
		groups = properties.groups;
		blockHeightType = properties.blockHeightType;
		blockWidthType = properties.blockWidthType;
		itemType = properties.itemType;
		renderType = properties.renderType;
		modelType = properties.modelType;
		dropType = properties.dropType;
		recipeType = properties.recipeType;
		displayNameType = properties.displayNameType;
		tintType = properties.tintType;
		orderType = properties.orderType;
		encouragement = properties.encouragement;
		flammability = properties.flammability;
		compostability = properties.compostability;
		isPottable = properties.isPottable;
		isIgnoredByBees = properties.isIgnoredByBees;
		isPreferredByBees = properties.isPreferredByBees;
		shouldTintParticles = properties.shouldTintParticles;
		shouldTintItem = properties.shouldTintItem;
		dye = properties.dye;
		burnTime = properties.burnTime;
	}

	public String getName() {
		return nameOf(target);
	}

	public Block getBlock() {
		return target.get();
	}

	public MetaType getType() {
		return type;
	}

	@Nullable
	public Item getDye() {
		return dye;
	}

	public boolean isIgnoredByBees() {
		return isIgnoredByBees;
	}

	public boolean isPreferredByBees() {
		return isPreferredByBees;
	}

	public List<ResourceKey<CreativeModeTab>> getGroups() {
		return groups;
	}

	public boolean hasItem() {
		return itemType != PlantopiaBlockItemType.NONE;
	}

	public PlantopiaBlockHeightType getBlockHeightType() {
		return blockHeightType;
	}

	public PlantopiaBlockWidthType getBlockWidthType() {
		return blockWidthType;
	}

	public PlantopiaRenderType getRenderType() {
		return renderType;
	}

	public PlantopiaModelType getModelType() {
		return modelType;
	}

	public PlantopiaOrderType getOrderType() {
		return orderType;
	}

	public PlantopiaBlockDropType getDropType() {
		return dropType;
	}

	public PlantopiaRecipeType getRecipeType() {
		return recipeType;
	}

	public PlantopiaDisplayNameType getDisplayNameType() {
		return displayNameType;
	}

	public PlantopiaTintType getTintType() {
		return tintType;
	}

	public boolean isTinted() {
		return tintType != PlantopiaTintType.NONE;
	}

	public boolean hasDrop() {
		return dropType != PlantopiaBlockDropType.NONE;
	}

	public boolean shouldGenerateModel() {
		return modelType != PlantopiaModelType.NONE && modelType != PlantopiaModelType.CUSTOM;
	}

	public boolean shouldGenerateItem() {
		return itemType != PlantopiaBlockItemType.NONE && itemType != PlantopiaBlockItemType.CUSTOM;
	}

	public boolean shouldGenerateLootTable() {
		return dropType != PlantopiaBlockDropType.NONE && dropType != PlantopiaBlockDropType.CUSTOM;
	}

	public boolean shouldGenerateRecipe() {
		return recipeType != PlantopiaRecipeType.NONE && recipeType != PlantopiaRecipeType.CUSTOM;
	}

	public boolean shouldGenerateTranslation() {
		return hasItem() && displayNameType != PlantopiaDisplayNameType.NONE && displayNameType != PlantopiaDisplayNameType.CUSTOM;
	}

	public boolean shouldApplyTint() {
		return tintType != PlantopiaTintType.NONE && tintType != PlantopiaTintType.CUSTOM;
	}

	public boolean shouldApplyTintToParticles() {
		return shouldApplyTint() && shouldTintParticles;
	}

	public boolean shouldApplyTintToItem() {
		return shouldApplyTint() && hasItem() && shouldTintItem;
	}

	public boolean shouldApplyRenderLayer() {
		return renderType != PlantopiaRenderType.NONE;
	}

	public int getEncouragement() {
		return encouragement;
	}

	public int getFlammability() {
		return flammability;
	}

	public boolean isFlammable() {
		return encouragement > 0 || flammability > 0;
	}

	public boolean isPottable() {
		return hasItem() && isPottable && blockHeightType.getBaseHeight() == 1 && blockWidthType.getBaseWidth() == 1;
	}

	public float getCompostability() {
		return compostability;
	}

	public boolean isCompostable() {
		return hasItem() && compostability > 0.0F;
	}

	public int getBurnTime() {
		return this.burnTime;
	}

	public boolean isBurnable() {
		return this.burnTime > 0;
	}

	public static final class MetaType extends PlantopiaMetaType<MetaType, MetaProperties> {
		public static final MetaType PLANT = MetaProperties.of().order(PlantopiaOrderType.PLANT).cutoutRender().flammable(Encouragement.PLANT, Flammability.PLANT).compostable(Compostability.PLANT_1).tintedParticles().makeType("plant");
		public static final MetaType WOODY_PLANT = MetaProperties.copy(PLANT).notCompostable().customBurnTime(100).makeType("woody_plant");
		public static final MetaType FLOWER = MetaProperties.copy(PLANT).order(PlantopiaOrderType.FLOWER).pottable().notTintedParticles().compostable(Compostability.FLOWER).makeType("flower");
		public static final MetaType SAPLING = MetaProperties.copy(PLANT).pottable().notTintedParticles().makeType("sapling");
		public static final MetaType MUSHROOM_PLANT = MetaProperties.copy(PLANT).pottable().notTintedParticles().notFlammable().compostable(Compostability.MUSHROOM_PLANT).makeType("mushroom_plant");
		public static final MetaType MUSHROOM_STEM = MetaProperties.of().compostable(Compostability.MUSHROOM_STEM).makeType("mushroom_stem");
		public static final MetaType MUSHROOM_BLOCK = MetaProperties.of().compostable(Compostability.MUSHROOM_BLOCK).makeType("mushroom_block");
		public static final MetaType POTTED = MetaProperties.of().cutoutRender().noItem().makeType("potted");
		public static final MetaType LEAVES = MetaProperties.of().cutoutMippedRender().tintedParticles().flammable(Encouragement.LEAVES, Flammability.LEAVES).makeType("leaves");
		public static final MetaType STONE = MetaProperties.of().makeType("stone");
		public static final MetaType SAND = MetaProperties.of().makeType("sand");
		public static final MetaType WOOD = MetaProperties.of().flammable(Encouragement.WOOD, Flammability.WOOD).makeType("wood");
		public static final MetaType LOG = MetaProperties.copy(WOOD).makeType("log");
		public static final MetaType PLANKS = MetaProperties.copy(WOOD).flammable(Encouragement.PLANKS, Flammability.PLANKS).makeType("planks");
		public static final MetaType DIRT = MetaProperties.of().makeType("dirt");
		public static final MetaType IRON = MetaProperties.of().makeType("iron");
		public static final MetaType GRASS_BLOCK = MetaProperties.copy(DIRT).cutoutMippedRender().grassTint().notTintedParticles().makeType("grass_block");

		private MetaType(String name, MetaProperties properties) {
			super("block", name, properties);
		}

		public boolean isSimplePlantLike() {
			return equals(PLANT)
				|| equals(WOODY_PLANT);
		}

		public boolean isMushroomLike() {
			return instanceOf(MUSHROOM_PLANT)
				|| instanceOf(MUSHROOM_STEM)
				|| instanceOf(MUSHROOM_BLOCK);
		}

		public boolean isAbleToBePotted() {
			return instanceOf(PLANT);
		}
	}

	public static final class MetaProperties extends PlantopiaMetaProperties<MetaType, MetaProperties> {
		private List<ResourceKey<CreativeModeTab>> groups = List.of(PlantopiaCreativeModeTabs.PLANTOPIA);
		private PlantopiaBlockHeightType blockHeightType = PlantopiaBlockHeightType.SINGLE;
		private PlantopiaBlockWidthType blockWidthType = PlantopiaBlockWidthType.SINGLE;
		private PlantopiaBlockItemType itemType = PlantopiaBlockItemType.GENERATED;
		private PlantopiaRenderType renderType = PlantopiaRenderType.NONE;
		private PlantopiaModelType modelType = PlantopiaModelType.GENERATED;
		private PlantopiaBlockDropType dropType = PlantopiaBlockDropType.GENERATED;
		private PlantopiaRecipeType recipeType = PlantopiaRecipeType.GENERATED;
		private PlantopiaDisplayNameType displayNameType = PlantopiaDisplayNameType.GENERATED;
		private PlantopiaTintType tintType = PlantopiaTintType.NONE;
		private PlantopiaOrderType orderType = PlantopiaOrderType.BLOCK;
		private int encouragement = 0;
		private int flammability = 0;
		private float compostability = 0.0F;
		private boolean isPottable = false;
		private boolean isIgnoredByBees = false;
		private boolean isPreferredByBees = false;
		private boolean shouldTintParticles = true;
		private boolean shouldTintItem = true;
		private Item dye = null;
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

		public MetaProperties noDye() {
			this.dye = null;
			return this;
		}

		public MetaProperties dye(Item dye) {
			if(type != null && !type.instanceOf(MetaType.FLOWER)) throw new PlantopiaMetaException.UnableToSet("dye", type);
			this.dye = dye;
			return this;
		}

		public MetaProperties order(PlantopiaOrderType orderType) {
			this.orderType = orderType;
			return this;
		}

		public MetaProperties tintedParticles() {
			this.shouldTintParticles = true;
			return this;
		}

		public MetaProperties notTintedParticles() {
			this.shouldTintParticles = false;
			return this;
		}

		public MetaProperties tintedItem() {
			this.shouldTintItem = true;
			return this;
		}

		public MetaProperties notTintedItem() {
			this.shouldTintItem = false;
			return this;
		}

		public MetaProperties noRecipe() {
			this.recipeType = PlantopiaRecipeType.NONE;
			return this;
		}

		public MetaProperties customRecipe() {
			this.recipeType = PlantopiaRecipeType.CUSTOM;
			return this;
		}

		public MetaProperties generatedRecipe() {
			this.recipeType = PlantopiaRecipeType.GENERATED;
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

		public MetaProperties ignoredByBees() {
			this.isIgnoredByBees = true;
			return this;
		}

		public MetaProperties notIgnoredByBees() {
			this.isIgnoredByBees = false;
			return this;
		}

		public MetaProperties preferredByBees() {
			this.isPreferredByBees = true;
			return this;
		}

		public MetaProperties notPreferredByBees() {
			this.isPreferredByBees = false;
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

		public MetaProperties generatedItem() {
			this.itemType = PlantopiaBlockItemType.GENERATED;
			return this;
		}

		public MetaProperties customItem() {
			this.itemType = PlantopiaBlockItemType.CUSTOM;
			return this;
		}

		public MetaProperties noItem() {
			this.itemType = PlantopiaBlockItemType.NONE;
			return this;
		}

		public MetaProperties cutoutRender() {
			this.renderType = PlantopiaRenderType.CUTOUT;
			return this;
		}

		public MetaProperties cutoutMippedRender() {
			this.renderType = PlantopiaRenderType.CUTOUT_MIPPED;
			return this;
		}

		public MetaProperties translucentRender() {
			this.renderType = PlantopiaRenderType.TRANSLUCENT;
			return this;
		}

		public MetaProperties noRender() {
			this.renderType = PlantopiaRenderType.NONE;
			return this;
		}

		public MetaProperties flammable(int encouragement, int flammability) {
			this.encouragement = Math.max(encouragement, 0);
			this.flammability = Math.max(flammability, 0);
			return this;
		}

		public MetaProperties notFlammable() {
			this.encouragement = 0;
			this.flammability = 0;
			return this;
		}

		public MetaProperties pottable() {
			if(type != null && !type.isAbleToBePotted()) throw new PlantopiaMetaException.UnableToSet("pottable", type);
			this.isPottable = true;
			return this;
		}

		public MetaProperties notPottable() {
			this.isPottable = false;
			return this;
		}

		public MetaProperties compostable(float compostability) {
			this.compostability = Mth.clamp(compostability, 0.0F, 1.0F);
			return this;
		}

		public MetaProperties notCompostable() {
			this.compostability = 0.0F;
			return this;
		}

		public MetaProperties singleHigh() {
			this.blockHeightType = PlantopiaBlockHeightType.SINGLE;
			return this;
		}

		public MetaProperties doubleHigh() {
			this.blockHeightType = PlantopiaBlockHeightType.DOUBLE;
			return this;
		}

		public MetaProperties tripleHigh() {
			this.blockHeightType = PlantopiaBlockHeightType.TRIPLE;
			return this;
		}

		public MetaProperties singleWide() {
			this.blockWidthType = PlantopiaBlockWidthType.SINGLE;
			return this;
		}

		public MetaProperties doubleWide() {
			this.blockWidthType = PlantopiaBlockWidthType.DOUBLE;
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

		public MetaProperties noDrop() {
			this.dropType = PlantopiaBlockDropType.NONE;
			return this;
		}

		public MetaProperties customDrop() {
			this.dropType = PlantopiaBlockDropType.CUSTOM;
			return this;
		}

		public MetaProperties generatedDrop() {
			this.dropType = PlantopiaBlockDropType.GENERATED;
			return this;
		}

		public MetaProperties dropSelf() {
			this.dropType = PlantopiaBlockDropType.SELF;
			return this;
		}

		public MetaProperties dropSelfByShears() {
			this.dropType = PlantopiaBlockDropType.SELF_BY_SHEARS;
			return this;
		}

		public MetaProperties noTint() {
			this.tintType = PlantopiaTintType.NONE;
			return this;
		}

		public MetaProperties customTint() {
			this.tintType = PlantopiaTintType.CUSTOM;
			return this;
		}

		public MetaProperties pottedTint(PlantopiaTintType tintType) {
			if(type != null && !type.instanceOf(MetaType.POTTED)) throw new PlantopiaMetaException.UnableToSet("pottedTint", type);
			this.tintType = tintType;
			return this;
		}

		public MetaProperties grassTint() {
			this.tintType = PlantopiaTintType.GRASS;
			return this;
		}

		public MetaProperties foliageTint() {
			this.tintType = PlantopiaTintType.FOLIAGE;
			return this;
		}

		public MetaProperties rainbowTint() {
			this.tintType = PlantopiaTintType.RAINBOW;
			return this;
		}
	}
}