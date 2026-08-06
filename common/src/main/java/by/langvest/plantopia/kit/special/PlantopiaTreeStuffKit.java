package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.special.PlantopiaCeilingHangingSignBlock;
import by.langvest.plantopia.block.special.PlantopiaStandingSignBlock;
import by.langvest.plantopia.block.special.PlantopiaWallHangingSignBlock;
import by.langvest.plantopia.block.special.PlantopiaWallSignBlock;
import by.langvest.plantopia.client.render.PlantopiaEntityLayerDefinitions;
import by.langvest.plantopia.entity.PlantopiaBoatType;
import by.langvest.plantopia.entity.PlantopiaBoatTypes;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.item.special.PlantopiaBoatItem;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.data.BlockFamily;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

@ParametersAreNonnullByDefault
public class PlantopiaTreeStuffKit extends PlantopiaKit {
    protected final String baseName;
    protected final PlantopiaTreeKitConfiguration config;

    public final RegistryObject<Block> planks;
    public final RegistryObject<Block> stairs;
    public final RegistryObject<Block> slab;
    public final RegistryObject<Block> fence;
    public final RegistryObject<Block> fenceGate;
    public final RegistryObject<Block> door;
    public final RegistryObject<Block> trapdoor;
    public final RegistryObject<Block> pressurePlate;
    public final RegistryObject<Block> button;

    public final RegistryObject<PlantopiaBoatType> boatType;
    public final RegistryObject<Item> boatItem;
    public final RegistryObject<Item> chestBoatItem;

    public final RegistryObject<Block> sign;
    public final RegistryObject<Block> wallSign;
    public final RegistryObject<Block> hangingSign;
    public final RegistryObject<Block> wallHangingSign;
    public final RegistryObject<Item> signItem;
    public final RegistryObject<Item> hangingSignItem;

    public PlantopiaTreeStuffKit(
        String baseName,
        WoodType woodType,
        @NotNull PlantopiaTreeKitConfiguration config
    ) {
        this.baseName = baseName;
        this.config = config;

        /* BUILDING BLOCKS ********************************************************************************************/

        var plankColor = config.plankMapColor();

        this.planks = config.registerBlock(baseName + "_planks", Block::new, MetaProperties.of(MetaType.PLANKS).mapColor(plankColor));
        this.stairs = config.registerBlock(baseName + "_stairs", properties -> new StairBlock(planks.get().defaultBlockState(), properties), MetaProperties.of(MetaType.WOODEN_STAIRS).mapColor(plankColor).parent(planks));
        this.slab = config.registerBlock(baseName + "_slab", SlabBlock::new, MetaProperties.of(MetaType.WOODEN_SLAB).mapColor(plankColor).parent(planks));
        this.fence = config.registerBlock(baseName + "_fence", FenceBlock::new, MetaProperties.of(MetaType.WOODEN_FENCE).mapColor(plankColor).parent(planks));
        this.fenceGate = config.registerBlock(baseName + "_fence_gate", properties -> new FenceGateBlock(properties, woodType), MetaProperties.of(MetaType.WOODEN_FENCE_GATE).mapColor(plankColor).parent(planks));
        this.door = config.registerBlock(baseName + "_door", properties -> new DoorBlock(properties, woodType.setType()), MetaProperties.of(MetaType.WOODEN_DOOR).mapColor(plankColor).parent(planks));
        this.trapdoor = config.registerBlock(baseName + "_trapdoor", properties -> new TrapDoorBlock(properties, woodType.setType()), MetaProperties.of(MetaType.WOODEN_TRAPDOOR).mapColor(plankColor).parent(planks));
        this.pressurePlate = config.registerBlock(baseName + "_pressure_plate", properties -> new PressurePlateBlock(config.pressurePlateSensitivity(), properties, woodType.setType()), MetaProperties.of(MetaType.WOODEN_PRESSURE_PLATE).mapColor(plankColor).parent(planks));
        this.button = config.registerBlock(baseName + "_button", properties -> new ButtonBlock(properties, woodType.setType(), config.buttonTicksToStayPressed(), config.canArrowsPressButton()), MetaProperties.of(MetaType.WOODEN_BUTTON).mapColor(plankColor).parent(planks));

        /* SIGNS ******************************************************************************************************/

        this.sign = config.registerBlock(baseName + "_sign", properties -> new PlantopiaStandingSignBlock(properties, woodType), MetaProperties.of(MetaType.WOODEN_SIGN).mapColor(plankColor).parent(planks).customItem());
        this.wallSign = config.registerBlock(baseName + "_wall_sign", properties -> new PlantopiaWallSignBlock(properties, woodType), MetaProperties.of(MetaType.WOODEN_SIGN).mapColor(plankColor).parent(planks).noItem());
        this.hangingSign = config.registerBlock(baseName + "_hanging_sign", properties -> new PlantopiaCeilingHangingSignBlock(properties, woodType), MetaProperties.of(MetaType.WOODEN_HANGING_SIGN).mapColor(plankColor).parent(planks).customItem());
        this.wallHangingSign = config.registerBlock(baseName + "_wall_hanging_sign", properties -> new PlantopiaWallHangingSignBlock(properties, woodType), MetaProperties.of(MetaType.WOODEN_HANGING_SIGN).mapColor(plankColor).parent(planks).noItem());

        this.signItem = config.registerItem(baseName + "_sign", properties -> new SignItem(properties, sign.get(), wallSign.get()), PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.SIGN));
        this.hangingSignItem = config.registerItem(baseName + "_hanging_sign", properties -> new HangingSignItem(hangingSign.get(), wallHangingSign.get(), properties), PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.SIGN));

        /* BOATS ******************************************************************************************************/

        var supposedBoatItem = PlantopiaItems.supposeItem(baseName + "_boat");
        var supposedChestBoatItem = PlantopiaItems.supposeItem(baseName + "_chest_boat");

        this.boatType = PlantopiaBoatTypes.registerBoatType(baseName, () -> new PlantopiaBoatType(boat -> planks.get(), boat -> boat instanceof ChestBoat ? supposedChestBoatItem.get() : supposedBoatItem.get()));
        this.boatItem = config.registerItem(baseName + "_boat", properties -> new PlantopiaBoatItem(false, boatType, properties), PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.BOAT));
        this.chestBoatItem = config.registerItem(baseName + "_chest_boat", properties -> new PlantopiaBoatItem(true, boatType, properties), PlantopiaItemMeta.MetaProperties.of(PlantopiaItemMeta.MetaType.CHEST_BOAT));

        if (Plantopia.getPlatform().isClient()) {
            var boatModelLayerLocation = new ModelLayerLocation(plantopia("boat", baseName), "main");
            var chestBoatModelLayerLocation = new ModelLayerLocation(plantopia("chest_boat", baseName), "main");

            PlantopiaEntityLayerDefinitions.registerLayerDefinition(boatModelLayerLocation, BoatModel::createBodyModel);
            PlantopiaEntityLayerDefinitions.registerLayerDefinition(chestBoatModelLayerLocation, ChestBoatModel::createBodyModel);
        }
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        var blockFamily = createBlockFamily();

        bridge.blockFamily(blockFamily);
        bridge.woodenBoat(boatItem.get(), planks.get());
        bridge.chestBoat(chestBoatItem.get(), boatItem.get());
    }

    protected BlockFamily.Builder blockFamilyBuilder() {
        return new BlockFamily.Builder(planks.get());
    }

    protected BlockFamily createBlockFamily() {
        return blockFamilyBuilder()
            .button(button.get())
            .fence(fence.get())
            .fenceGate(fenceGate.get())
            .pressurePlate(pressurePlate.get())
            .sign(sign.get(), wallSign.get())
            .slab(slab.get())
            .stairs(stairs.get())
            .door(door.get())
            .trapdoor(trapdoor.get())
            .recipeGroupPrefix("wooden")
            .recipeUnlockedBy("has_planks")
            .getFamily();
    }
}
