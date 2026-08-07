package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaBalkBlock;
import by.langvest.plantopia.block.special.PlantopiaBalkStubBlock;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.tag.PlantopiaBlockTags;
import by.langvest.plantopia.tag.PlantopiaItemTags;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaTreePlantKit extends PlantopiaKit {
    protected final String baseName;
    protected final PlantopiaTreeKitConfiguration config;

    public final RegistryObject<Block> balk;
    public final RegistryObject<Block> stub;
    public final RegistryObject<Block> strippedBalk;
    public final RegistryObject<Block> strippedStub;

    public final TagKey<Block> balksBlockTag;
    public final TagKey<Item> balksItemTag;

    public PlantopiaTreePlantKit(
        String baseName,
        @Nullable Supplier<Block> balkGoesAfter,
        @Nullable Supplier<Block> strippedBalkGoesAfter,
        @NotNull PlantopiaTreeKitConfiguration config
    ) {
        this.baseName = baseName;
        this.config = config;

        var supposedStub = PlantopiaBlocks.supposeBlock(baseName + "_stub");
        var supposedStrippedBalk = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_balk");
        var supposedStrippedStub = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_stub");

        var trunkColor = config.trunkMapColor();
        var strippedTrunkColor = config.strippedTrunkMapColor();

        this.balk = PlantopiaBlocks.registerBlock(baseName + "_balk", properties -> new PlantopiaBalkBlock(properties, supposedStub), config.applyMeta(MetaType.BALK).mapColor(trunkColor).goesAfter(balkGoesAfter).strippable(supposedStrippedBalk));
        this.stub = PlantopiaBlocks.registerBlock(baseName + "_stub", properties -> new PlantopiaBalkStubBlock(properties, balk), config.applyMeta(MetaType.BALK_STUB).mapColor(trunkColor).parent(balk).strippable(supposedStrippedStub));
        this.strippedBalk = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_balk", properties -> new PlantopiaBalkBlock(properties, supposedStrippedStub), config.applyMeta(MetaType.BALK).mapColor(strippedTrunkColor).goesAfter(strippedBalkGoesAfter));
        this.strippedStub = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_stub", properties -> new PlantopiaBalkStubBlock(properties, strippedBalk), config.applyMeta(MetaType.BALK_STUB).mapColor(strippedTrunkColor).parent(strippedBalk));

        this.balksBlockTag = PlantopiaBlockTags.createBlockTag(baseName + "_balks");
        this.balksItemTag = PlantopiaItemTags.createItemTag(baseName + "_balks");
    }

    @Override
    protected void addBlockTags(PlantopiaDatagenBridgeEvent.BlockTagEvent.Bridge bridge) {
        super.addBlockTags(bridge);

        var currentBalks = bridge.getOrCreateTagSet(balksBlockTag);
        var allBalks = bridge.getOrCreateTagSet(PlantopiaBlockTags.BALKS);
        var overworldNaturalBlocks = bridge.getOrCreateTagSet(BlockTags.OVERWORLD_NATURAL_LOGS);

        currentBalks.add(balk.get(), stub.get(), strippedBalk.get(), strippedStub.get());
        allBalks.addTag(balksBlockTag);

        if (config.dimensionType() == Level.OVERWORLD) {
            overworldNaturalBlocks.add(balk.get(), stub.get());
        }
    }

    @Override
    protected void addItemTags(PlantopiaDatagenBridgeEvent.ItemTagEvent.Bridge bridge) {
        super.addItemTags(bridge);

        var currentBalks = bridge.getOrCreateTagSet(balksItemTag);
        var allBalks = bridge.getOrCreateTagSet(PlantopiaItemTags.BALKS);

        currentBalks.add(balk.get().asItem(), strippedBalk.get().asItem());
        allBalks.addTag(balksItemTag);
    }
}
