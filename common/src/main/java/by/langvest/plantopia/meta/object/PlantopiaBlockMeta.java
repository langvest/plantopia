package by.langvest.plantopia.meta.object;

import by.langvest.plantopia.compat.PlantopiaCompats.*;
import by.langvest.plantopia.meta.property.*;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.tab.PlantopiaCreativeModeTabs;
import by.langvest.toolkit.meta.*;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaBlockMeta extends SimpleMetaObject<Block> {
    private final MetaType type;
    private final List<ResourceKey<CreativeModeTab>> groups;
    private final Supplier<BlockBehaviour.Properties> behaviourProperties;
    private final PlantopiaBlockHeightType blockHeightType;
    private final PlantopiaBlockWidthType blockWidthType;
    private final PlantopiaBlockItemType itemType;
    private final PlantopiaRenderType renderType;
    private final PlantopiaModelType modelType;
    private final PlantopiaBlockDropType dropType;
    private final PlantopiaRecipeType recipeType;
    private final PlantopiaDisplayNameType displayNameType;
    private final PlantopiaTintType tintType;
    private final PlantopiaTagType tagType;
    private final PlantopiaOrderType orderType;
    private final PlantopiaBeePreferenceType beePreferenceType;
    private final @Nullable Supplier<? extends ItemLike> goesAfter;
    private final @Nullable DyeColor color;
    private final @Nullable Supplier<Block> parent;
    private final @Nullable Supplier<Block> stripped;
    private final boolean isPottable;
    private final boolean shouldTintParticles;
    private final boolean shouldTintItem;
    private final float compostability;
    private final int encouragement;
    private final int flammability;
    private final int burnTime;

    public PlantopiaBlockMeta(ResourceLocation identifier, @NotNull MetaProperties properties) {
        super(identifier, PlantopiaRegistries.BLOCK.supposeValue(identifier));
        type = MetaAccessor.getMetaTypeFrom(properties);
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
        tagType = properties.tagType;
        orderType = properties.orderType;
        goesAfter = properties.goesAfter;
        encouragement = properties.encouragement;
        flammability = properties.flammability;
        compostability = properties.compostability;
        isPottable = properties.isPottable;
        beePreferenceType = properties.beePreferenceType;
        shouldTintParticles = properties.shouldTintParticles;
        shouldTintItem = properties.shouldTintItem;
        color = properties.color;
        parent = properties.parent;
        stripped = properties.stripped;
        burnTime = properties.burnTime;
        behaviourProperties = properties.behaviourProperties;
    }

    public BlockBehaviour.Properties createBehaviourProperties() {
        return behaviourProperties.get();
    }

    public MetaType getType() {
        return type;
    }

    @Nullable
    public DyeColor getColor() {
        return color;
    }

    @Nullable
    public Block getParent() {
        return parent == null ? null : parent.get();
    }

    @Nullable
    public Block getStripped() {
        return stripped == null ? null : stripped.get();
    }

    public boolean isIgnoredByBees() {
        return beePreferenceType == PlantopiaBeePreferenceType.IGNORE;
    }

    public boolean isPreferredByBees() {
        return beePreferenceType == PlantopiaBeePreferenceType.PREFER;
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

    public @Nullable ItemLike getGoesAfter() {
        return goesAfter == null ? null : goesAfter.get();
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

    public boolean shouldGenerateTag() {
        return tagType != PlantopiaTagType.NONE && tagType != PlantopiaTagType.CUSTOM;
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

    public boolean isStrippable() {
        return stripped != null;
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public boolean isBurnable() {
        return this.burnTime > 0;
    }

    public static class MetaType extends SimpleMetaObject.MetaType<MetaType, MetaProperties> {
        public static final MetaType PLANT = MetaProperties.create()
            .mapColor(MapColor.PLANT)
            .noCollision()
            .instabreak()
            .sound(SoundType.GRASS)
            .ignitedByLava()
            .pushReaction(PushReaction.DESTROY)
            .order(PlantopiaOrderType.PLANT)
            .cutoutRender()
            .flammable(Encouragement.PLANT, Flammability.PLANT)
            .compostable(Compostability.PLANT_1)
            .makeType("plant");

        public static final MetaType WATER_PLANT = MetaProperties.of(PLANT)
            .order(PlantopiaOrderType.WET_PLANT)
            .makeType("water_plant");

        public static final MetaType WATER_GRASS = MetaProperties.of(WATER_PLANT)
            .replaceable()
            .doubleHigh()
            .compostable(Compostability.PLANT_2)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .makeType("water_grass");

        public static final MetaType UNDERWATER_PLANT = MetaProperties.of(WATER_PLANT)
            .mapColor(MapColor.WATER)
            .sound(SoundType.WET_GRASS)
            .notFlammable()
            .makeType("underwater_plant");

        public static final MetaType WOODY_PLANT = MetaProperties.of(PLANT)
            .mapColor(MapColor.WOOD)
            .notCompostable()
            .burnTime(BurnTime.WOODY_PLANT)
            .makeType("woody_plant");

        public static final MetaType SHRUB = MetaProperties.of(WOODY_PLANT)
            .makeType("shrub");

        public static final MetaType TINY_CACTUS = MetaProperties.of(PLANT)
            .sound(SoundType.WOOL)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pottable()
            .makeType("tiny_cactus");

        public static final MetaType WATERLILY = MetaProperties.of(WATER_PLANT)
            .notFlammable()
            .sound(SoundType.LILY_PAD)
            .hasCollision()
            .makeType("waterlily");

        public static final MetaType FLOWERING_WATERLILY = MetaProperties.of(WATERLILY)
            .noItem()
            .preferredByBees()
            .makeType("flowering_waterlily");

        public static final MetaType GRASS = MetaProperties.of(PLANT)
            .replaceable()
            .makeType("grass");

        public static final MetaType SMALL_GRASS = MetaProperties.of(GRASS)
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .dropSelfByShears()
            .pottable()
            .makeType("small_grass");

        public static final MetaType TINY_GRASS = MetaProperties.of(SMALL_GRASS)
            .notPottable()
            .makeType("tiny_grass");

        public static final MetaType TALL_GRASS = MetaProperties.of(GRASS)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .doubleHigh()
            .compostable(Compostability.PLANT_2)
            .makeType("tall_grass");

        public static final MetaType HERB = MetaProperties.of(TALL_GRASS)
            .dropSelfByShears()
            .preferredByBees()
            .compostable(Compostability.PLANT_2 + Compostability.HAS_FLOWERS)
            .makeType("herb");

        public static final MetaType FLOWER = MetaProperties.of(PLANT)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .order(PlantopiaOrderType.FLOWER)
            .notTintedParticles()
            .compostable(Compostability.FLOWER)
            .makeType("flower");

        public static final MetaType SMALL_FLOWER = MetaProperties.of(FLOWER)
            .pottable()
            .makeType("small_flower");

        public static final MetaType TALL_FLOWER = MetaProperties.of(FLOWER)
            .doubleHigh()
            .makeType("tall_flower");

        public static final MetaType LUCKY_DAISY = MetaProperties.of(SMALL_FLOWER)
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .sound(SoundType.CHERRY_SAPLING)
            .customModel()
            .customDrop()
            .customItem()
            .makeType("lucky_daisy");

        public static final MetaType WILDFLOWERS = MetaProperties.of(SMALL_FLOWER)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .makeType("wildflowers");

        public static final MetaType CLOVER = MetaProperties.of(PLANT)
            .sound(SoundType.AZALEA)
            .order(PlantopiaOrderType.CLOVER)
            .grassTint()
            .makeType("clover");

        public static final MetaType CLOVER_FLOWER = MetaProperties.of(SMALL_FLOWER)
            .sound(SoundType.AZALEA)
            .order(PlantopiaOrderType.CLOVER)
            .grassTint()
            .offsetType(BlockBehaviour.OffsetType.XYZ)
            .makeType("clover_flower");

        public static final MetaType WATERLILY_FLOWER = MetaProperties.of(SMALL_FLOWER)
            .order(PlantopiaOrderType.WET_PLANT)
            .sound(SoundType.CHERRY_LEAVES)
            .notPottable()
            .waterlilyTint()
            .customItem()
            .dropSelf()
            .makeType("waterlily_flower");

        public static final MetaType SAPLING = MetaProperties.of(PLANT)
            .copyBehaviour(Blocks.OAK_SAPLING)
            .pottable()
            .notTintedParticles()
            .makeType("sapling");

        public static final MetaType MUSHROOM = MetaProperties.create()
            .copyBehaviour(Blocks.BROWN_MUSHROOM)
            .postProcessable()
            .notLuminous()
            .cutoutRender()
            .pottable()
            .notTintedParticles()
            .notFlammable()
            .compostable(Compostability.MUSHROOM)
            .order(PlantopiaOrderType.MUSHROOM)
            .makeType("mushroom");

        public static final MetaType MUSHROOM_STEM = MetaProperties.create()
            .copyBehaviour(Blocks.MUSHROOM_STEM)
            .compostable(Compostability.MUSHROOM_STEM)
            .order(PlantopiaOrderType.MUSHROOM)
            .makeType("mushroom_stem");

        public static final MetaType MUSHROOM_BLOCK = MetaProperties.create()
            .copyBehaviour(Blocks.BROWN_MUSHROOM_BLOCK)
            .compostable(Compostability.MUSHROOM_BLOCK)
            .order(PlantopiaOrderType.MUSHROOM)
            .makeType("mushroom_block");

        public static final MetaType POTTED = MetaProperties.create()
            .copyBehaviour(Blocks.FLOWER_POT)
            .cutoutRender()
            .noItem()
            .notTintedParticles()
            .makeType("potted");

        public static final MetaType LEAVES = MetaProperties.create()
            .copyBehaviour(Blocks.OAK_LEAVES)
            .cutoutMippedRender()
            .flammable(Encouragement.LEAVES, Flammability.LEAVES)
            .compostable(Compostability.LEAVES)
            .makeType("leaves");

        public static final MetaType LEAF_LITTER = MetaProperties.create()
            .replaceable()
            .noCollision()
            .sound(SoundType.CHERRY_LEAVES)
            .pushReaction(PushReaction.DESTROY)
            .cutoutRender()
            .flammable(Encouragement.PLANT, Flammability.PLANT)
            .compostable(Compostability.PARTIAL_PLANT)
            .burnTime(BurnTime.LEAF_LITTER)
            .makeType("leaf_litter");

        public static final MetaType TREE_FRUIT = MetaProperties.of(PLANT)
            .order(PlantopiaOrderType.TREE_FRUIT)
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .compostable(Compostability.PARTIAL_PLANT)
            .makeType("tree_fruit");

        public static final MetaType SEA_MOSS = MetaProperties.create()
            .copyBehaviour(Blocks.MOSS_BLOCK)
            .sound(SoundType.WET_GRASS)
            .flammable(Encouragement.PLANT_2, Flammability.PLANT_2)
            .order(PlantopiaOrderType.SEA_MOSS)
            .makeType("sea_moss");

        public static final MetaType SEA_HANGING_MOSS = MetaProperties.of(UNDERWATER_PLANT)
            .flammable(Encouragement.PLANT_2, Flammability.PLANT_2)
            .order(PlantopiaOrderType.SEA_MOSS)
            .makeType("sea_hanging_moss");

        public static final MetaType STONE = MetaProperties.create()
            .copyBehaviour(Blocks.STONE)
            .makeType("stone");

        public static final MetaType COBBLESTONE_SHARD = MetaProperties.of(STONE)
            .resetBehaviour()
            .sound(SoundType.DRIPSTONE_BLOCK)
            .strength(0.2F)
            .pushReaction(PushReaction.DESTROY)
            .noOcclusion()
            .makeType("cobblestone_shard");

        public static final MetaType SAND = MetaProperties.create()
            .copyBehaviour(Blocks.SAND)
            .makeType("sand");

        public static final MetaType WOOD = MetaProperties.create()
            .copyBehaviour(Blocks.OAK_WOOD)
            .flammable(Encouragement.WOOD, Flammability.WOOD)
            .makeType("wood");

        public static final MetaType LOG = MetaProperties.create()
            .copyBehaviour(Blocks.OAK_LOG)
            .flammable(Encouragement.WOOD, Flammability.WOOD)
            .makeType("log");

        public static final MetaType BALK = MetaProperties.create()
            .copyBehaviour(Blocks.OAK_LOG)
            .mapColor(MapColor.PODZOL)
            .flammable(Encouragement.WOOD, Flammability.WOOD)
            .burnTime(BurnTime.BALK)
            .strength(1.5F)
            .makeType("balk");

        public static final MetaType BALK_STUB = MetaProperties.of(BALK)
            .noItem()
            .makeType("balk_stub");

        public static final MetaType PLANKS = MetaProperties.create()
            .copyBehaviour(Blocks.OAK_PLANKS)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("planks");

        public static final MetaType STAIRS = MetaProperties.create()
            .makeType("stairs");

        public static final MetaType WOODEN_STAIRS = MetaProperties.of(STAIRS)
            .copyBehaviour(Blocks.OAK_STAIRS)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("wooden_planks");

        public static final MetaType SLAB = MetaProperties.create()
            .makeType("slab");

        public static final MetaType WOODEN_SLAB = MetaProperties.of(SLAB)
            .copyBehaviour(Blocks.OAK_SLAB)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("wooden_slab");

        public static final MetaType FENCE = MetaProperties.create()
            .makeType("fence");

        public static final MetaType WOODEN_FENCE = MetaProperties.of(FENCE)
            .copyBehaviour(Blocks.OAK_FENCE)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("wooden_fence");

        public static final MetaType FENCE_GATE = MetaProperties.create()
            .makeType("fence_gate");

        public static final MetaType WOODEN_FENCE_GATE = MetaProperties.of(FENCE_GATE)
            .copyBehaviour(Blocks.OAK_FENCE_GATE)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("wooden_fence_gate");

        public static final MetaType DOOR = MetaProperties.create()
            .cutoutRender()
            .makeType("door");

        public static final MetaType WOODEN_DOOR = MetaProperties.of(DOOR)
            .copyBehaviour(Blocks.OAK_DOOR)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("wooden_door");

        public static final MetaType TRAPDOOR = MetaProperties.create()
            .cutoutRender()
            .makeType("trapdoor");

        public static final MetaType WOODEN_TRAPDOOR = MetaProperties.of(TRAPDOOR)
            .copyBehaviour(Blocks.OAK_TRAPDOOR)
            .flammable(Encouragement.PLANKS, Flammability.PLANKS)
            .makeType("wooden_trapdoor");

        public static final MetaType PRESSURE_PLATE = MetaProperties.create()
            .makeType("pressure_plate");

        public static final MetaType WOODEN_PRESSURE_PLATE = MetaProperties.of(PRESSURE_PLATE)
            .copyBehaviour(Blocks.OAK_PRESSURE_PLATE)
            .makeType("wooden_pressure_plate");

        public static final MetaType BUTTON = MetaProperties.create()
            .makeType("button");

        public static final MetaType WOODEN_BUTTON = MetaProperties.of(BUTTON)
            .copyBehaviour(Blocks.OAK_BUTTON)
            .makeType("wooden_button");

        public static final MetaType SIGN = MetaProperties.create()
            .makeType("sign");

        public static final MetaType WOODEN_SIGN = MetaProperties.of(SIGN)
            .copyBehaviour(Blocks.OAK_SIGN)
            .makeType("wooden_sign");

        public static final MetaType HANGING_SIGN = MetaProperties.create()
            .makeType("hanging_sign");

        public static final MetaType WOODEN_HANGING_SIGN = MetaProperties.of(HANGING_SIGN)
            .copyBehaviour(Blocks.OAK_HANGING_SIGN)
            .makeType("wooden_hanging_sign");

        public static final MetaType DIRT = MetaProperties.create()
            .copyBehaviour(Blocks.DIRT)
            .makeType("dirt");

        public static final MetaType GRASS_BLOCK = MetaProperties.of(DIRT)
            .copyBehaviour(Blocks.GRASS_BLOCK)
            .cutoutMippedRender()
            .grassTint()
            .notTintedParticles()
            .makeType("grass_block");

        public static final MetaType IRON = MetaProperties.create()
            .copyBehaviour(Blocks.IRON_BLOCK)
            .makeType("iron");

        public static final MetaType CAULDRON = MetaProperties.of(IRON)
            .copyBehaviour(Blocks.CAULDRON)
            .makeType("cauldron");

        public static final MetaType SEA_SHELL = MetaProperties.create()
            .mapColor(MapColor.SAND)
            .strength(0.2F)
            .sound(SoundType.BONE_BLOCK)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .cutoutRender()
            .customTint()
            .customItem()
            .makeType("sea_shell");

        public static final MetaType SNOW = MetaProperties.create()
            .copyBehaviour(Blocks.SNOW)
            .makeType("snow");

        public static final MetaType ICE = MetaProperties.create()
            .copyBehaviour(Blocks.ICE)
            .order(PlantopiaOrderType.ICE)
            .dropSelfBySilkTouch()
            .makeType("ice");

        public static final MetaType ICICLE = MetaProperties.of(ICE)
            .notValidSpawn()
            .cutoutRender()
            .offsetType(BlockBehaviour.OffsetType.XZ)
            .pushReaction(PushReaction.DESTROY)
            .makeType("icicle");

        public static final MetaType ICE_CRUST = MetaProperties.of(ICE)
            .strength(0.2F)
            .translucentRender()
            .isValidSpawn((state, level, pos, entityType) -> {
                var posBelow = pos.below();
                var stateBelow = level.getBlockState(posBelow);
                return stateBelow.isValidSpawn(level, posBelow, entityType);
            })
            .pushReaction(PushReaction.DESTROY)
            .makeType("ice_crust");

        private MetaType(String name, MetaProperties properties) {
            super(plantopia(name), properties);
        }

        public boolean isSimplePlantLike() {
            return instanceOfExcept(PLANT, Set.of(FLOWER, SAPLING, WATERLILY, UNDERWATER_PLANT, MUSHROOM));
        }

        public boolean isMushroomLike() {
            return instanceOf(MUSHROOM) || instanceOf(MUSHROOM_STEM) || instanceOf(MUSHROOM_BLOCK);
        }

        public boolean isSignLike() {
            return instanceOf(SIGN) || instanceOf(HANGING_SIGN);
        }

        public boolean isAbleToBePotted() {
            return instanceOf(PLANT);
        }

        public boolean isWoodFamilyLike() {
            return instanceOf(PLANKS)
                || instanceOf(LOG)
                || instanceOf(WOOD)
                || instanceOf(WOODEN_SLAB)
                || instanceOf(WOODEN_STAIRS)
                || instanceOf(WOODEN_FENCE)
                || instanceOf(WOODEN_FENCE_GATE)
                || instanceOf(WOODEN_SIGN)
                || instanceOf(WOODEN_HANGING_SIGN)
                || instanceOf(WOODEN_DOOR)
                || instanceOf(WOODEN_TRAPDOOR)
                || instanceOf(WOODEN_PRESSURE_PLATE)
                || instanceOf(WOODEN_BUTTON);
        }
    }

    public static class MetaProperties extends SimpleMetaObject.MetaProperties<MetaType, MetaProperties> {
        private List<ResourceKey<CreativeModeTab>> groups = List.of(PlantopiaCreativeModeTabs.MAIN);
        private Supplier<BlockBehaviour.Properties> behaviourProperties = BlockBehaviour.Properties::of;
        private PlantopiaBlockHeightType blockHeightType = PlantopiaBlockHeightType.SINGLE;
        private PlantopiaBlockWidthType blockWidthType = PlantopiaBlockWidthType.SINGLE;
        private PlantopiaBlockItemType itemType = PlantopiaBlockItemType.GENERATED;
        private PlantopiaRenderType renderType = PlantopiaRenderType.NONE;
        private PlantopiaModelType modelType = PlantopiaModelType.GENERATED;
        private PlantopiaBlockDropType dropType = PlantopiaBlockDropType.GENERATED;
        private PlantopiaRecipeType recipeType = PlantopiaRecipeType.GENERATED;
        private PlantopiaDisplayNameType displayNameType = PlantopiaDisplayNameType.GENERATED;
        private PlantopiaTintType tintType = PlantopiaTintType.NONE;
        private PlantopiaTagType tagType = PlantopiaTagType.GENERATED;
        private PlantopiaOrderType orderType = PlantopiaOrderType.BLOCK;
        private @Nullable Supplier<? extends ItemLike> goesAfter = null;
        private PlantopiaBeePreferenceType beePreferenceType = PlantopiaBeePreferenceType.DEFAULT;
        private @Nullable DyeColor color = null;
        private @Nullable Supplier<Block> parent = null;
        private @Nullable Supplier<Block> stripped = null;
        private boolean isPottable = false;
        private boolean shouldTintParticles = true;
        private boolean shouldTintItem = true;
        private float compostability = 0.0F;
        private int encouragement = 0;
        private int flammability = 0;
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

        private static boolean always(BlockState state, BlockGetter level, BlockPos pos) {
            return true;
        }

        private static boolean never(BlockState state, BlockGetter level, BlockPos pos) {
            return false;
        }

        private static boolean never(BlockState state, BlockGetter level, BlockPos pos, EntityType<?> entity) {
            return false;
        }

        public MetaProperties modifyBehaviour(Supplier<BlockBehaviour.Properties> properties) {
            this.behaviourProperties = properties;
            return this;
        }

        public MetaProperties modifyBehaviour(Function<BlockBehaviour.Properties, BlockBehaviour.Properties> properties) {
            var prevBehaviourProperties = this.behaviourProperties;
            this.behaviourProperties = () -> properties.apply(prevBehaviourProperties.get());
            return this;
        }

        public MetaProperties copyBehaviour(Block block) {
            return modifyBehaviour(() -> BlockBehaviour.Properties.copy(block));
        }

        public MetaProperties resetBehaviour() {
            return modifyBehaviour(BlockBehaviour.Properties::of);
        }

        public MetaProperties randomlyTicking() {
            return modifyBehaviour(BlockBehaviour.Properties::randomTicks);
        }

        public MetaProperties notRandomlyTicking() {
            return modifyBehaviour(properties -> {
                properties.isRandomlyTicking = false;
                return properties;
            });
        }

        public MetaProperties strength(float strength) {
            return modifyBehaviour(properties -> properties.strength(strength));
        }

        public MetaProperties friction(float friction) {
            return modifyBehaviour(properties -> properties.friction(friction));
        }

        public MetaProperties instabreak() {
            return modifyBehaviour(BlockBehaviour.Properties::instabreak);
        }

        public MetaProperties mapColor(DyeColor mapColor) {
            return modifyBehaviour(properties -> properties.mapColor(mapColor));
        }

        public MetaProperties lightLevel(ToIntFunction<BlockState> lightEmission) {
            return modifyBehaviour(properties -> properties.lightLevel(lightEmission));
        }

        public MetaProperties lightLevel(int lightEmission) {
            return modifyBehaviour(properties -> properties.lightLevel(state -> lightEmission));
        }

        public MetaProperties notLuminous() {
            return modifyBehaviour(properties -> properties.lightLevel(state -> 0));
        }

        public MetaProperties mapColor(MapColor mapColor) {
            return modifyBehaviour(properties -> properties.mapColor(mapColor));
        }

        public MetaProperties mapColor(Function<BlockState, MapColor> mapColor) {
            return modifyBehaviour(properties -> properties.mapColor(mapColor));
        }

        public MetaProperties postProcessable(BlockBehaviour.StatePredicate predicate) {
            return modifyBehaviour(properties -> properties.hasPostProcess(predicate));
        }

        public MetaProperties postProcessable() {
            return postProcessable(MetaProperties::always);
        }

        public MetaProperties notPostProcessable() {
            return postProcessable(MetaProperties::never);
        }

        public MetaProperties redstoneConductable(BlockBehaviour.StatePredicate predicate) {
            return modifyBehaviour(properties -> properties.isRedstoneConductor(predicate));
        }

        public MetaProperties redstoneConductable() {
            return redstoneConductable(MetaProperties::always);
        }

        public MetaProperties notRedstoneConductable() {
            return redstoneConductable(MetaProperties::never);
        }

        public MetaProperties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
            return modifyBehaviour(properties -> properties.isValidSpawn(predicate));
        }

        public MetaProperties notValidSpawn() {
            return modifyBehaviour(properties -> properties.isValidSpawn(MetaProperties::never));
        }

        public MetaProperties sound(SoundType soundType) {
            return modifyBehaviour(properties -> properties.sound(soundType));
        }

        public MetaProperties offsetType(BlockBehaviour.OffsetType offsetType) {
            return modifyBehaviour(properties -> properties.offsetType(offsetType));
        }

        public MetaProperties pushReaction(PushReaction pushReaction) {
            return modifyBehaviour(properties -> properties.pushReaction(pushReaction));
        }

        public MetaProperties replaceable() {
            return modifyBehaviour(BlockBehaviour.Properties::replaceable);
        }

        public MetaProperties instrument(NoteBlockInstrument instrument) {
            return modifyBehaviour(properties -> properties.instrument(instrument));
        }

        public MetaProperties notReplaceable() {
            return modifyBehaviour(properties -> {
                properties.replaceable = false;
                return properties;
            });
        }

        public MetaProperties ignitedByLava() {
            return modifyBehaviour(BlockBehaviour.Properties::ignitedByLava);
        }

        public MetaProperties notIgnitedByLava() {
            return modifyBehaviour(properties -> {
                properties.ignitedByLava = false;
                return properties;
            });
        }

        public MetaProperties hasOcclusion() {
            return modifyBehaviour(properties -> {
                properties.canOcclude = true;
                return properties;
            });
        }

        public MetaProperties noOcclusion() {
            return modifyBehaviour(BlockBehaviour.Properties::noOcclusion);
        }

        public MetaProperties hasCollision() {
            return modifyBehaviour(properties -> {
                properties.hasCollision = true;
                return properties;
            });
        }

        public MetaProperties noCollision() {
            return modifyBehaviour(BlockBehaviour.Properties::noCollission);
        }

        public MetaProperties hasDynamicShape() {
            return modifyBehaviour(BlockBehaviour.Properties::dynamicShape);
        }

        public MetaProperties noDynamicShape() {
            return modifyBehaviour(properties -> {
                properties.dynamicShape = false;
                return properties;
            });
        }

        public MetaProperties noBurnTime() {
            this.burnTime = -1;
            return this;
        }

        public MetaProperties burnTime(int ticks) {
            this.burnTime = ticks;
            return this;
        }

        public MetaProperties noColor() {
            this.color = null;
            return this;
        }

        public MetaProperties color(DyeColor color) {
            this.color = color;
            return this;
        }

        public MetaProperties noParent() {
            this.parent = null;
            return this;
        }

        public MetaProperties parent(Supplier<Block> parent) {
            this.parent = parent;
            return this;
        }

        public MetaProperties notStrippable() {
            this.stripped = null;
            return this;
        }

        public MetaProperties strippable(Supplier<Block> stripped) {
            this.stripped = stripped;
            return this;
        }

        public MetaProperties order(PlantopiaOrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public MetaProperties goesAfter(@Nullable RegistryObject<? extends ItemLike> itemLike) {
            this.goesAfter = itemLike;
            return this;
        }

        public MetaProperties goesAfter(@Nullable Supplier<? extends ItemLike> itemLike) {
            this.goesAfter = itemLike;
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
            this.beePreferenceType = PlantopiaBeePreferenceType.IGNORE;
            return this;
        }

        public MetaProperties preferredByBees() {
            this.beePreferenceType = PlantopiaBeePreferenceType.PREFER;
            return this;
        }

        public MetaProperties defaultBeePreference() {
            this.beePreferenceType = PlantopiaBeePreferenceType.DEFAULT;
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
            if (type != null && !type.isAbleToBePotted()) throw new MetaException.UnableToSet("pottable", type);
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

        public MetaProperties tripleHighPlant() {
            return tripleHigh().compostable(Compostability.PLANT_3);
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

        public MetaProperties dropSelfBySilkTouch() {
            this.dropType = PlantopiaBlockDropType.SELF_BY_SILK_TOUCH;
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
            if (type != null && !type.instanceOf(MetaType.POTTED))
                throw new MetaException.UnableToSet("pottedTint", type);
            this.tintType = tintType;
            return this;
        }

        public MetaProperties grassTint() {
            this.tintType = PlantopiaTintType.GRASS;
            return this;
        }

        public MetaProperties dryTint() {
            this.tintType = PlantopiaTintType.DRY;
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

        public MetaProperties lilyPadTint() {
            this.tintType = PlantopiaTintType.LILY_PAD;
            return this;
        }

        public MetaProperties waterlilyTint() {
            this.tintType = PlantopiaTintType.WATERLILY;
            return this;
        }
    }
}