package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaDirectionalPillarBlock;
import by.langvest.plantopia.block.special.PlantopiaStraightBalkBlock;
import by.langvest.plantopia.block.special.PlantopiaStraightBalkStubBlock;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaExtraVanillaBirchTreeKit extends PlantopiaExtraVanillaTreeKit {
    public final RegistryObject<Block> baseLog;
    public final RegistryObject<Block> baseWood;
    public final RegistryObject<Block> baseBalk;
    public final RegistryObject<Block> baseStub;

    public PlantopiaExtraVanillaBirchTreeKit(
        String baseName,
        Supplier<Block> planks,
        Supplier<Block> log,
        Supplier<Block> wood,
        Supplier<Block> strippedLog,
        Supplier<Block> strippedWood,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, planks, log, wood, strippedLog, strippedWood, config);

        var supposedBaseStub = PlantopiaBlocks.supposeBlock(baseName + "_base_stub");

        this.baseLog = PlantopiaBlocks.registerBlock(baseName + "_base_log", PlantopiaDirectionalPillarBlock::new, MetaProperties.of(MetaType.LOG).mapColor(baseLogMapColor(config)).customModel().customDrop().strippable(strippedLog));
        this.baseWood = PlantopiaBlocks.registerBlock(baseName + "_base_wood", PlantopiaDirectionalPillarBlock::new, MetaProperties.of(MetaType.WOOD).mapColor(baseWoodMapColor(config)).customModel().customDrop().strippable(strippedWood));
        this.baseBalk = PlantopiaBlocks.registerBlock(baseName + "_base_balk", properties -> new PlantopiaStraightBalkBlock(properties, supposedBaseStub), MetaProperties.of(MetaType.BALK).mapColor(config.trunkMapColor()).customDrop().strippable(plant.strippedBalk));
        this.baseStub = PlantopiaBlocks.registerBlock(baseName + "_base_stub", properties -> new PlantopiaStraightBalkStubBlock(properties, baseBalk), MetaProperties.of(MetaType.BALK_STUB).mapColor(config.trunkMapColor()).customDrop().parent(baseBalk).strippable(plant.strippedStub));
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.woodFromLogs(baseWood.get(), baseLog.get());
        bridge.balksFromLogs(baseBalk.get(), baseLog.get());
    }

    @Override
    protected void addBlockTags(PlantopiaDatagenBridgeEvent.BlockTagEvent.Bridge bridge) {
        super.addBlockTags(bridge);

        var currentBalks = bridge.getOrCreateTagSet(plant.balksBlockTag);
        currentBalks.add(baseBalk.get(), baseStub.get());
    }

    @Override
    protected void addItemTags(PlantopiaDatagenBridgeEvent.ItemTagEvent.Bridge bridge) {
        super.addItemTags(bridge);

        var currentBalks = bridge.getOrCreateTagSet(plant.balksItemTag);
        currentBalks.add(baseBalk.get().asItem());
    }

    @Contract(pure = true)
    protected static @NotNull Function<BlockState, MapColor> baseLogMapColor(PlantopiaTreeKitConfiguration config) {
        return state -> {
            var facing = state.getValue(BlockStateProperties.FACING);
            return facing.getAxis().isVertical() ? config.plankMapColor() : config.trunkMapColor();
        };
    }

    @Contract(pure = true)
    protected static @NotNull Function<BlockState, MapColor> baseWoodMapColor(PlantopiaTreeKitConfiguration config) {
        return state -> {
            var facing = state.getValue(BlockStateProperties.FACING);
            return facing == Direction.DOWN ? MapColor.COLOR_BLACK : config.trunkMapColor();
        };
    }
}
