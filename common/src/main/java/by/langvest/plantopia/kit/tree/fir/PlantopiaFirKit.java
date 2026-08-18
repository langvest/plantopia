package by.langvest.plantopia.kit.tree.fir;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeKit;
import by.langvest.plantopia.kit.special.PlantopiaTreePlantKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeTrunkKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeStuffKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaFirKit extends PlantopiaAbstractTreeKit {
    public final PlantopiaTreeStuffKit stuff;
    public final PlantopiaTreeTrunkKit trunk;
    public final PlantopiaTreePlantKit plant;

    public final PlantopiaFirWorldgenKit worldgen;

    public final RegistryObject<Block> sapling;
    public final RegistryObject<Block> leaves;

    public PlantopiaFirKit(
        String baseName,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, config);

        this.worldgen = createWorldgenKit(baseName);
        this.sapling = PlantopiaBlocks.registerBlock(baseName + "_sapling", properties -> new SaplingBlock(worldgen.treeGrower, properties), config.applyMeta(MetaType.SAPLING));
        this.leaves = PlantopiaBlocks.registerBlock(baseName + "_leaves", LeavesBlock::new, config.applyMeta(MetaType.LEAVES));

        this.trunk = new PlantopiaTreeTrunkKit(baseName, config);
        this.plant = new PlantopiaTreePlantKit(baseName, trunk.wood, trunk.strippedWood, config);
        this.stuff = new PlantopiaTreeStuffKit(baseName, woodType, config);
    }

    protected static @NotNull PlantopiaFirWorldgenKit createWorldgenKit(String baseName) {
        var supposedLog = PlantopiaBlocks.supposeBlock(baseName + "_log");
        var supposedLeaves = PlantopiaBlocks.supposeBlock(baseName + "_leaves");
        var supposedSapling = PlantopiaBlocks.supposeBlock(baseName + "_sapling");

        return new PlantopiaFirWorldgenKit(
            baseName,
            supposedLog,
            supposedLeaves,
            supposedSapling
        );
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.planksFromLogs(stuff.planks.get(), trunk.logsItemTag);
        bridge.planksFromBalks(stuff.planks.get(), plant.balksItemTag);
        bridge.hangingSign(stuff.hangingSign.get(), trunk.strippedLog.get());
        bridge.balksFromLogs(plant.balk.get(), trunk.log.get());
        bridge.balksFromLogs(plant.strippedBalk.get(), trunk.strippedLog.get());
    }
}
