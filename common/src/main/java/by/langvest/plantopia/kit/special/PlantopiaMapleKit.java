package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaMapleLeavesBlock;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.plantopia.worldgen.placement.PlantopiaPlacementUtils;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class PlantopiaMapleKit extends PlantopiaAbstractTreeKit {
    public final PlantopiaTreeStuffKit stuff;
    public final PlantopiaTreeTrunkKit trunk;

    public final PlantopiaMapleFeatureKit yellowFeature;
    public final PlantopiaMapleFeatureKit orangeFeature;
    public final PlantopiaMapleFeatureKit redFeature;

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

        this.yellowFeature = createFeatureKit("yellow_", baseName, config);
        this.yellowSapling = PlantopiaBlocks.registerBlock("yellow_" + baseName + "_sapling", properties -> new SaplingBlock(yellowFeature.treeGrower, properties), config.applyBlockMeta(MetaProperties.of(MetaType.SAPLING).mapColor(MapColor.COLOR_YELLOW)));
        this.yellowLeaves = PlantopiaBlocks.registerBlock("yellow_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.YELLOW_MAPLE_LEAVES, properties), config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_YELLOW)));

        this.orangeFeature = createFeatureKit("orange_", baseName, config);
        this.orangeSapling = PlantopiaBlocks.registerBlock("orange_" + baseName + "_sapling", properties -> new SaplingBlock(orangeFeature.treeGrower, properties), config.applyBlockMeta(MetaProperties.of(MetaType.SAPLING).mapColor(MapColor.COLOR_ORANGE)));
        this.orangeLeaves = PlantopiaBlocks.registerBlock("orange_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.ORANGE_MAPLE_LEAVES, properties), config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_ORANGE)));

        this.redFeature = createFeatureKit("red_", baseName, config);
        this.redSapling = PlantopiaBlocks.registerBlock("red_" + baseName + "_sapling", properties -> new SaplingBlock(redFeature.treeGrower, properties), config.applyBlockMeta(MetaProperties.of(MetaType.SAPLING).mapColor(MapColor.COLOR_RED)));
        this.redLeaves = PlantopiaBlocks.registerBlock("red_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.RED_MAPLE_LEAVES, properties), config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_RED)));

        this.trunk = new PlantopiaTreeTrunkKit(baseName, config);
        this.stuff = new PlantopiaTreeStuffKit(baseName, woodType, config);
    }

    protected static @NotNull PlantopiaMapleFeatureKit createFeatureKit(String colorPrefix, String baseName, PlantopiaTreeKitConfiguration config) {
        var supposedLog = PlantopiaBlocks.supposeBlock(baseName + "_log");
        var supposedLeaves = PlantopiaBlocks.supposeBlock(colorPrefix + baseName + "_leaves");
        var supposedSapling = PlantopiaBlocks.supposeBlock(colorPrefix + baseName + "_sapling");
        var leafLitterPlacement = PlantopiaPlacementUtils.createKey("patch_" + colorPrefix + "leaf_litter_checked");

        return new PlantopiaMapleFeatureKit(
            colorPrefix + baseName,
            supposedLog,
            supposedLeaves,
            supposedSapling,
            leafLitterPlacement,
            config
        );
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.planksFromLogs(stuff.planks.get(), trunk.logsItemTag, 4);
        bridge.hangingSign(stuff.hangingSign.get(), trunk.strippedLog.get());
    }
}
