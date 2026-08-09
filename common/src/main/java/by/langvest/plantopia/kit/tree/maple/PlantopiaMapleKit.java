package by.langvest.plantopia.kit.tree.maple;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaMapleLeavesBlock;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.kit.special.PlantopiaAbstractTreeKit;
import by.langvest.plantopia.kit.special.PlantopiaTreePlantKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeTrunkKit;
import by.langvest.plantopia.kit.special.PlantopiaTreeStuffKit;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlantopiaMapleKit extends PlantopiaAbstractTreeKit {
    public final PlantopiaTreeStuffKit stuff;
    public final PlantopiaTreeTrunkKit trunk;
    public final PlantopiaTreePlantKit plant;

    public final PlantopiaMapleWorldgenKit yellowWorldgen;
    public final PlantopiaMapleWorldgenKit orangeWorldgen;
    public final PlantopiaMapleWorldgenKit redWorldgen;

    public final RegistryObject<Block> yellowSapling;
    public final RegistryObject<Block> orangeSapling;
    public final RegistryObject<Block> redSapling;

    public final RegistryObject<Block> yellowLeaves;
    public final RegistryObject<Block> orangeLeaves;
    public final RegistryObject<Block> redLeaves;

    public PlantopiaMapleKit(
        String baseName,
        PlantopiaTreeKitConfiguration config
    ) {
        super(baseName, config);

        var yellowFoliageColor = MapColor.COLOR_YELLOW;
        var orangeFoliageColor = MapColor.COLOR_ORANGE;
        var redFoliageColor = MapColor.COLOR_RED;

        this.yellowWorldgen = createWorldgenKit("yellow_", baseName);
        this.yellowSapling = PlantopiaBlocks.registerBlock("yellow_" + baseName + "_sapling", properties -> new SaplingBlock(yellowWorldgen.treeGrower, properties), config.applyMeta(MetaType.SAPLING).mapColor(yellowFoliageColor));
        this.yellowLeaves = PlantopiaBlocks.registerBlock("yellow_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.YELLOW_MAPLE_LEAVES, properties), config.applyMeta(MetaType.LEAVES).mapColor(yellowFoliageColor));

        this.orangeWorldgen = createWorldgenKit("orange_", baseName);
        this.orangeSapling = PlantopiaBlocks.registerBlock("orange_" + baseName + "_sapling", properties -> new SaplingBlock(orangeWorldgen.treeGrower, properties), config.applyMeta(MetaType.SAPLING).mapColor(orangeFoliageColor));
        this.orangeLeaves = PlantopiaBlocks.registerBlock("orange_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.ORANGE_MAPLE_LEAVES, properties), config.applyMeta(MetaType.LEAVES).mapColor(orangeFoliageColor));

        this.redWorldgen = createWorldgenKit("red_", baseName);
        this.redSapling = PlantopiaBlocks.registerBlock("red_" + baseName + "_sapling", properties -> new SaplingBlock(redWorldgen.treeGrower, properties), config.applyMeta(MetaType.SAPLING).mapColor(redFoliageColor));
        this.redLeaves = PlantopiaBlocks.registerBlock("red_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.RED_MAPLE_LEAVES, properties), config.applyMeta(MetaType.LEAVES).mapColor(redFoliageColor));

        this.trunk = new PlantopiaTreeTrunkKit(baseName, config);
        this.plant = new PlantopiaTreePlantKit(baseName, trunk.wood, trunk.strippedWood, config);
        this.stuff = new PlantopiaTreeStuffKit(baseName, woodType, config);
    }

    protected static @NotNull PlantopiaMapleWorldgenKit createWorldgenKit(String colorPrefix, String baseName) {
        var supposedLog = PlantopiaBlocks.supposeBlock(baseName + "_log");
        var supposedLeaves = PlantopiaBlocks.supposeBlock(colorPrefix + baseName + "_leaves");
        var supposedSapling = PlantopiaBlocks.supposeBlock(colorPrefix + baseName + "_sapling");
        var supposedBalk = PlantopiaBlocks.supposeBlock(baseName + "_balk");
        var supposedStub = PlantopiaBlocks.supposeBlock(baseName + "_stub");
        var leafLitterPlacement = PlantopiaPlacementUtils.createKey("patch_" + colorPrefix + "leaf_litter_checked");

        return new PlantopiaMapleWorldgenKit(
            colorPrefix + baseName,
            supposedLog,
            supposedLeaves,
            supposedSapling,
            supposedBalk,
            supposedStub,
            leafLitterPlacement
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
