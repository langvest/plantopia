package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaCeilingHangingSignBlock;
import by.langvest.plantopia.block.special.PlantopiaStandingSignBlock;
import by.langvest.plantopia.block.special.PlantopiaWallHangingSignBlock;
import by.langvest.plantopia.block.special.PlantopiaWallSignBlock;
import by.langvest.plantopia.client.render.PlantopiaEntityLayerDefinitions;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.entity.PlantopiaBoatTypes;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.item.special.PlantopiaBoatItem;
import by.langvest.plantopia.kit.options.PlantopiaTreeOptions;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaTreeStuffKit {
    protected final String baseName;

    protected final RegistryObject<Block> planks;
    protected final RegistryObject<Block> stairs;
    protected final RegistryObject<Block> slab;
    protected final RegistryObject<Block> fence;
    protected final RegistryObject<Block> fenceGate;
    protected final RegistryObject<Block> door;
    protected final RegistryObject<Block> trapdoor;
    protected final RegistryObject<Block> pressurePlate;
    protected final RegistryObject<Block> button;

    protected final RegistryObject<PlantopiaBoatType> boatType;
    protected final RegistryObject<Item> boatItem;
    protected final RegistryObject<Item> chestBoatItem;

    protected final RegistryObject<Block> sign;
    protected final RegistryObject<Block> wallSign;
    protected final RegistryObject<Block> hangingSign;
    protected final RegistryObject<Block> wallHangingSign;
    protected final RegistryObject<Item> signItem;
    protected final RegistryObject<Item> hangingSignItem;

    protected PlantopiaTreeStuffKit(String baseName, WoodType woodType, @NotNull PlantopiaTreeOptions options) {
        this.baseName = baseName;

        /* BUILDING BLOCKS ********************************************************************************************/

        this.planks = PlantopiaBlocks.registerBlock(baseName + "_planks", Block::new, options.applyBlockMeta(MetaProperties.of(MetaType.PLANKS)));
        this.stairs = PlantopiaBlocks.registerBlock(baseName + "_stairs", properties -> new StairBlock(() -> planks.get().defaultBlockState(), properties), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_STAIRS).parent(planks)));
        this.slab = PlantopiaBlocks.registerBlock(baseName + "_slab", SlabBlock::new, options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_SLAB).parent(planks)));
        this.fence = PlantopiaBlocks.registerBlock(baseName + "_fence", FenceBlock::new, options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_FENCE).parent(planks)));
        this.fenceGate = PlantopiaBlocks.registerBlock(baseName + "_fence_gate", properties -> new FenceGateBlock(properties, woodType), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_FENCE_GATE).parent(planks)));
        this.door = PlantopiaBlocks.registerBlock(baseName + "_door", properties -> new DoorBlock(properties, woodType.setType()), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_DOOR).parent(planks)));
        this.trapdoor = PlantopiaBlocks.registerBlock(baseName + "_trapdoor", properties -> new TrapDoorBlock(properties, woodType.setType()), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_TRAPDOOR).parent(planks)));
        this.pressurePlate = PlantopiaBlocks.registerBlock(baseName + "_pressure_plate", properties -> new PressurePlateBlock(options.pressurePlateSensitivity(), properties, woodType.setType()), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_PRESSURE_PLATE).parent(planks)));
        this.button = PlantopiaBlocks.registerBlock(baseName + "_button", properties -> new ButtonBlock(properties, woodType.setType(), options.buttonTicksToStayPressed(), options.canArrowsPressButton()), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_BUTTON).parent(planks)));

        /* SIGNS ******************************************************************************************************/

        this.sign = PlantopiaBlocks.registerBlock(baseName + "_sign", properties -> new PlantopiaStandingSignBlock(properties, woodType), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_SIGN).parent(planks).customItem().noDisplayName()));
        this.wallSign = PlantopiaBlocks.registerBlock(baseName + "_wall_sign", properties -> new PlantopiaWallSignBlock(properties, woodType), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_SIGN).parent(planks).noItem()));
        this.hangingSign = PlantopiaBlocks.registerBlock(baseName + "_hanging_sign", properties -> new PlantopiaCeilingHangingSignBlock(properties, woodType), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_HANGING_SIGN).parent(planks).customItem().noDisplayName()));
        this.wallHangingSign = PlantopiaBlocks.registerBlock(baseName + "_wall_hanging_sign", properties -> new PlantopiaWallHangingSignBlock(properties, woodType), options.applyBlockMeta(MetaProperties.of(MetaType.WOODEN_HANGING_SIGN).parent(planks).noItem()));

        this.signItem = PlantopiaItems.registerItem(baseName + "_sign", properties -> new SignItem(properties, sign.get(), wallSign.get()), options.applyItemMeta(PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.SIGN)));
        this.hangingSignItem = PlantopiaItems.registerItem(baseName + "_hanging_sign", properties -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), properties), options.applyItemMeta(PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.SIGN)));

        /* BOATS ******************************************************************************************************/

        var supposedBoatItem = PlantopiaItems.supposeItem(baseName + "_boat");
        var supposedChestBoatItem = PlantopiaItems.supposeItem(baseName + "_chest_boat");

        this.boatType = PlantopiaBoatTypes.registerBoatType(baseName, () -> new PlantopiaBoatType(boat -> planks.get(), boat -> boat instanceof ChestBoat ? supposedChestBoatItem.get() : supposedBoatItem.get()));
        this.boatItem = PlantopiaItems.registerItem(baseName + "_boat", properties -> new PlantopiaBoatItem(false, boatType, properties), options.applyItemMeta(PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.BOAT)));
        this.chestBoatItem = PlantopiaItems.registerItem(baseName + "_chest_boat", properties -> new PlantopiaBoatItem(true, boatType, properties), options.applyItemMeta(PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.BOAT)));

        if (Plantopia.getPlatform().isClient()) {
            var boatModelLayerLocation = new ModelLayerLocation(plantopia("boat", baseName), "main");
            var chestBoatModelLayerLocation = new ModelLayerLocation(plantopia("chest_boat", baseName), "main");

            PlantopiaEntityLayerDefinitions.registerLayerDefinition(boatModelLayerLocation, BoatModel::createBodyModel);
            PlantopiaEntityLayerDefinitions.registerLayerDefinition(chestBoatModelLayerLocation, ChestBoatModel::createBodyModel);
        }
    }

    public static @NotNull PlantopiaTreeStuffKit registerTreeStuffKit(String baseName, WoodType woodType, PlantopiaTreeOptions options) {
        return new PlantopiaTreeStuffKit(baseName, woodType, options);
    }

    public RegistryObject<Block> planks() {
        return planks;
    }

    public RegistryObject<Block> stairs() {
        return stairs;
    }

    public RegistryObject<Block> slab() {
        return slab;
    }

    public RegistryObject<Block> fence() {
        return fence;
    }

    public RegistryObject<Block> fenceGate() {
        return fenceGate;
    }

    public RegistryObject<Block> door() {
        return door;
    }

    public RegistryObject<Block> trapdoor() {
        return trapdoor;
    }

    public RegistryObject<Block> pressurePlate() {
        return pressurePlate;
    }

    public RegistryObject<Block> button() {
        return button;
    }

    public RegistryObject<PlantopiaBoatType> boatType() {
        return boatType;
    }

    public RegistryObject<Item> boatItem() {
        return boatItem;
    }

    public RegistryObject<Item> chestBoatItem() {
        return chestBoatItem;
    }

    public RegistryObject<Block> sign() {
        return sign;
    }

    public RegistryObject<Block> wallSign() {
        return wallSign;
    }

    public RegistryObject<Block> hangingSign() {
        return hangingSign;
    }

    public RegistryObject<Block> wallHangingSign() {
        return wallHangingSign;
    }

    public RegistryObject<Item> signItem() {
        return signItem;
    }

    public RegistryObject<Item> hangingSignItem() {
        return hangingSignItem;
    }
}
