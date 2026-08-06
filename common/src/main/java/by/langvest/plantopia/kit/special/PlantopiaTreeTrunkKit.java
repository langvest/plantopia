package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.metaOf;

@ParametersAreNonnullByDefault
public class PlantopiaTreeTrunkKit extends PlantopiaKit {
    protected final String baseName;
    protected final PlantopiaTreeKitConfiguration config;

    public final RegistryObject<Block> log;
    public final RegistryObject<Block> wood;
    public final RegistryObject<Block> strippedLog;
    public final RegistryObject<Block> strippedWood;

    public final TagKey<Block> logsBlockTag;
    public final TagKey<Item> logsItemTag;

    public PlantopiaTreeTrunkKit(
        String baseName,
        @NotNull PlantopiaTreeKitConfiguration config
    ) {
        this.baseName = baseName;
        this.config = config;

        var supposedStrippedLog = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_log");
        var supposedStrippedWood = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_wood");

        this.log = config.registerBlock(baseName + "_log", RotatedPillarBlock::new, MetaProperties.of(MetaType.LOG).mapColor(config.logMapColor()).strippable(supposedStrippedLog));
        this.wood = config.registerBlock(baseName + "_wood", RotatedPillarBlock::new, MetaProperties.of(MetaType.WOOD).mapColor(config.trunkMapColor()).parent(log).strippable(supposedStrippedWood));
        this.strippedLog = config.registerBlock("stripped_" + baseName + "_log", RotatedPillarBlock::new, MetaProperties.of(MetaType.LOG).mapColor(config.strippedLogMapColor()));
        this.strippedWood = config.registerBlock("stripped_" + baseName + "_wood", RotatedPillarBlock::new, MetaProperties.of(MetaType.WOOD).mapColor(config.strippedTrunkMapColor()).parent(strippedLog));

        this.logsBlockTag = PlantopiaBlockTags.createBlockTag(baseName + "_logs");
        this.logsItemTag = PlantopiaItemTags.createItemTag(baseName + "_logs");
    }

    @Override
    protected void addBlockTags(PlantopiaDatagenBridgeEvent.BlockTagEvent.Bridge bridge) {
        super.addBlockTags(bridge);

        var currentLogs = bridge.getOrCreateTagSet(logsBlockTag);
        var logsThatBurn = bridge.getOrCreateTagSet(BlockTags.LOGS_THAT_BURN);
        var overworldNaturalBlocks = bridge.getOrCreateTagSet(BlockTags.OVERWORLD_NATURAL_LOGS);
        boolean isFlameable = metaOf(log.get()).map(PlantopiaBlockMeta::isFlammable).orElse(false);

        currentLogs.add(log.get(), wood.get(), strippedLog.get(), strippedWood.get());

        if (isFlameable) {
            logsThatBurn.addTag(logsBlockTag);
        }

        if (config.dimensionType() == Level.OVERWORLD) {
            overworldNaturalBlocks.add(log.get());
        }
    }

    @Override
    protected void addItemTags(PlantopiaDatagenBridgeEvent.ItemTagEvent.Bridge bridge) {
        super.addItemTags(bridge);

        var currentLogs = bridge.getOrCreateTagSet(logsItemTag);
        var logsThatBurn = bridge.getOrCreateTagSet(ItemTags.LOGS_THAT_BURN);
        boolean isFlameable = metaOf(log.get()).map(PlantopiaBlockMeta::isFlammable).orElse(false);

        currentLogs.add(log.get().asItem(), wood.get().asItem(), strippedLog.get().asItem(), strippedWood.get().asItem());

        if (isFlameable) {
            logsThatBurn.addTag(logsItemTag);
        }
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.woodFromLogs(wood.get(), log.get());
        bridge.woodFromLogs(strippedWood.get(), strippedLog.get());
    }
}
