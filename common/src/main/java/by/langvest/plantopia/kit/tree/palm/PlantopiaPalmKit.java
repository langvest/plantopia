package by.langvest.plantopia.kit.tree.palm;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeTrunkKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeStuffKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaPalmKit extends PlantopiaAbstractTreeKit {
    public final PlantopiaTreeStuffKit stuff;
    public final PlantopiaTreeTrunkKit trunk;

    public final PlantopiaPalmWorldgenKit worldgen;

    public final RegistryObject<Block> sapling;
    public final RegistryObject<Block> leaves;

    public PlantopiaPalmKit(
        String baseName,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, config);

        this.worldgen = createWorldgenKit(baseName);
        this.sapling = config.registerBlock(baseName + "_sapling", properties -> new SaplingBlock(worldgen.treeGrower, properties), MetaProperties.of(MetaType.SAPLING).mapColor(MapColor.WOOD));
        this.leaves = config.registerBlock(baseName + "_leaves", LeavesBlock::new, MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.WOOD).foliageTint());

        this.trunk = new PlantopiaTreeTrunkKit(baseName, config);
        this.stuff = new PlantopiaTreeStuffKit(baseName, woodType, config);
    }

    protected static @NotNull PlantopiaPalmWorldgenKit createWorldgenKit(String baseName) {
        var supposedLog = PlantopiaBlocks.supposeBlock(baseName + "_log");
        var supposedLeaves = PlantopiaBlocks.supposeBlock(baseName + "_leaves");
        var supposedSapling = PlantopiaBlocks.supposeBlock(baseName + "_sapling");

        return new PlantopiaPalmWorldgenKit(
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
        bridge.hangingSign(stuff.hangingSign.get(), trunk.strippedLog.get());
    }
}
