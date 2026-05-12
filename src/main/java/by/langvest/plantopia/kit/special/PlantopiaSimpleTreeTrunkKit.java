package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.kit.options.PlantopiaTreeOptions;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

public class PlantopiaSimpleTreeTrunkKit {
    protected final String baseName;

    protected final RegistryObject<Block> log;
    protected final RegistryObject<Block> wood;
    protected final RegistryObject<Block> strippedLog;
    protected final RegistryObject<Block> strippedWood;

    protected PlantopiaSimpleTreeTrunkKit(String baseName, @NotNull PlantopiaTreeOptions options) {
        this.baseName = baseName;

        var supposedStrippedLog = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_log");
        var supposedStrippedWood = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_wood");

        this.log = PlantopiaBlocks.registerBlock(baseName + "_log", RotatedPillarBlock::new, options.applyBlockMeta(MetaProperties.of(MetaType.LOG).mapColor(options.logMapColor()).strippable(supposedStrippedLog)));
        this.wood = PlantopiaBlocks.registerBlock(baseName + "_wood", RotatedPillarBlock::new, options.applyBlockMeta(MetaProperties.of(MetaType.WOOD).mapColor(options.trunkMapColor()).parent(log)));
        this.strippedLog = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_log", RotatedPillarBlock::new, options.applyBlockMeta(MetaProperties.of(MetaType.LOG).mapColor(options.woodMapColor()).strippable(supposedStrippedWood)));
        this.strippedWood = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_wood", RotatedPillarBlock::new, options.applyBlockMeta(MetaProperties.of(MetaType.WOOD).mapColor(options.woodMapColor()).parent(strippedLog)));
    }

    public static @NotNull PlantopiaSimpleTreeTrunkKit registerSimpleTreeTrunkKit(String baseName, PlantopiaTreeOptions options) {
        return new PlantopiaSimpleTreeTrunkKit(baseName, options);
    }

    public RegistryObject<Block> log() {
        return log;
    }

    public RegistryObject<Block> wood() {
        return wood;
    }

    public RegistryObject<Block> strippedLog() {
        return strippedLog;
    }

    public RegistryObject<Block> strippedWood() {
        return strippedWood;
    }
}
