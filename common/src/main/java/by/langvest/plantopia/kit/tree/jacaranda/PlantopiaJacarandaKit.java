package by.langvest.plantopia.kit.tree.jacaranda;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaJacarandaLeavesBlock;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeTrunkKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeStuffKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaJacarandaKit extends PlantopiaAbstractTreeKit {
    public final PlantopiaTreeStuffKit stuff;
    public final PlantopiaTreeTrunkKit trunk;

    public final PlantopiaJacarandaWorldgenKit worldgen;

    public final RegistryObject<Block> sapling;
    public final RegistryObject<Block> leaves;

    public PlantopiaJacarandaKit(
        String baseName,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, config);

        this.worldgen = createWorldgenKit(baseName);
        this.sapling = PlantopiaBlocks.registerBlock(baseName + "_sapling", properties -> new SaplingBlock(worldgen.treeGrower, properties), config.applyBlockMeta(MetaProperties.of(MetaType.SAPLING).mapColor(MapColor.COLOR_PURPLE)));
        this.leaves = PlantopiaBlocks.registerBlock(baseName + "_leaves", PlantopiaJacarandaLeavesBlock::new, config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_PURPLE)));

        this.trunk = new PlantopiaTreeTrunkKit(baseName, config);
        this.stuff = new PlantopiaTreeStuffKit(baseName, woodType, config);
    }

    protected static @NotNull PlantopiaJacarandaWorldgenKit createWorldgenKit(String baseName) {
        var supposedLog = PlantopiaBlocks.supposeBlock(baseName + "_log");
        var supposedLeaves = PlantopiaBlocks.supposeBlock(baseName + "_leaves");
        var supposedSapling = PlantopiaBlocks.supposeBlock(baseName + "_sapling");

        return new PlantopiaJacarandaWorldgenKit(
            baseName,
            supposedLog,
            supposedLeaves,
            supposedSapling
        );
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.planksFromLogs(stuff.planks.get(), trunk.logsItemTag, 4);
        bridge.hangingSign(stuff.hangingSign.get(), trunk.strippedLog.get());
    }
}
