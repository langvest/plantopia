package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.datagen.tag.PlantopiaBlockTagProvider;
import by.langvest.plantopia.datagen.tag.PlantopiaItemTagProvider;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.metaOf;

public class PlantopiaTreeTrunkKit {
    protected final String baseName;
    protected final PlantopiaTreeKitConfiguration config;

    protected final RegistryObject<Block> log;
    protected final RegistryObject<Block> wood;
    protected final RegistryObject<Block> strippedLog;
    protected final RegistryObject<Block> strippedWood;

    protected final TagKey<Block> logsBlockTag;
    protected final TagKey<Item> logsItemTag;

    protected PlantopiaTreeTrunkKit(String baseName, @NotNull PlantopiaTreeKitConfiguration config) {
        this.baseName = baseName;
        this.config = config;

        var supposedStrippedLog = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_log");
        var supposedStrippedWood = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_wood");

        this.log = PlantopiaBlocks.registerBlock(baseName + "_log", RotatedPillarBlock::new, config.applyBlockMeta(MetaProperties.of(MetaType.LOG).mapColor(config.logMapColor()).strippable(supposedStrippedLog)));
        this.wood = PlantopiaBlocks.registerBlock(baseName + "_wood", RotatedPillarBlock::new, config.applyBlockMeta(MetaProperties.of(MetaType.WOOD).mapColor(config.trunkMapColor()).parent(log).strippable(supposedStrippedWood)));
        this.strippedLog = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_log", RotatedPillarBlock::new, config.applyBlockMeta(MetaProperties.of(MetaType.LOG).mapColor(config.woodMapColor())));
        this.strippedWood = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_wood", RotatedPillarBlock::new, config.applyBlockMeta(MetaProperties.of(MetaType.WOOD).mapColor(config.woodMapColor()).parent(strippedLog)));

        this.logsBlockTag = PlantopiaBlockTags.createBlockTag(baseName + "_logs");
        this.logsItemTag = PlantopiaItemTags.createItemTag(baseName + "_logs");

        if (Plantopia.getPlatform().isDatagen()) {
            var workScheduler = Plantopia.getPlatform().getWorkScheduler();

            workScheduler.enqueueWork("block_tag_datagen", this::addBlockTags);
            workScheduler.enqueueWork("item_tag_datagen", this::addItemTags);
        }
    }

    public static @NotNull PlantopiaTreeTrunkKit registerTreeTrunkKit(String baseName, PlantopiaTreeKitConfiguration config) {
        return new PlantopiaTreeTrunkKit(baseName, config);
    }

    protected void addBlockTags() {
        var logsTagSet = PlantopiaBlockTagProvider.getOrCreateTagSet(logsBlockTag);
        boolean isFlameable = metaOf(log.get()).map(PlantopiaBlockMeta::isFlammable).orElse(false);

        logsTagSet.add(log.get(), wood.get(), strippedLog.get(), strippedWood.get());

        if (isFlameable) {
            PlantopiaBlockTagProvider.LOGS_THAT_BURN.addTag(logsBlockTag);
        }

        if (config.dimensionType() == Level.OVERWORLD) {
            PlantopiaBlockTagProvider.OVERWORLD_NATURAL_LOGS.add(log.get());
        }
    }

    protected void addItemTags() {
        var logsTagSet = PlantopiaItemTagProvider.getOrCreateTagSet(logsItemTag);
        boolean isFlameable = metaOf(log.get()).map(PlantopiaBlockMeta::isFlammable).orElse(false);

        logsTagSet.add(log.get().asItem(), wood.get().asItem(), strippedLog.get().asItem(), strippedWood.get().asItem());

        if (isFlameable) {
            PlantopiaItemTagProvider.LOGS_THAT_BURN.addTag(logsItemTag);
        }
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

    public TagKey<Block> logsBlockTag() {
        return logsBlockTag;
    }

    public TagKey<Item> logsItemTag() {
        return logsItemTag;
    }
}
