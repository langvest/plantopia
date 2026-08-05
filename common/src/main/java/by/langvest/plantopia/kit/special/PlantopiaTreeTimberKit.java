package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaBalkBlock;
import by.langvest.plantopia.block.special.PlantopiaBalkStubBlock;
import by.langvest.plantopia.event.PlantopiaDatagenBridgeEvent;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
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

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class PlantopiaTreeTimberKit extends PlantopiaKit {
    protected final String baseName;
    protected final Supplier<Block> log;
    protected final Supplier<Block> strippedLog;
    protected final PlantopiaTreeKitConfiguration config;

    public final RegistryObject<Block> balk;
    public final RegistryObject<Block> stub;
    public final RegistryObject<Block> strippedBalk;
    public final RegistryObject<Block> strippedStub;

    public final TagKey<Block> balksBlockTag;
    public final TagKey<Item> balksItemTag;

    public PlantopiaTreeTimberKit(
        String baseName,
        Supplier<Block> log,
        Supplier<Block> strippedLog,
        Supplier<Block> balkGoesAfter,
        Supplier<Block> strippedBalkGoesAfter,
        @NotNull PlantopiaTreeKitConfiguration config
    ) {
        this.baseName = baseName;
        this.log = log;
        this.strippedLog = strippedLog;
        this.config = config;

        var supposedStub = PlantopiaBlocks.supposeBlock(baseName + "_stub");
        var supposedStrippedBalk = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_balk");
        var supposedStrippedStub = PlantopiaBlocks.supposeBlock("stripped_" + baseName + "_stub");

        this.balk = PlantopiaBlocks.registerBlock(baseName + "_balk", properties -> new PlantopiaBalkBlock(properties, supposedStub), config.applyBlockMeta(MetaProperties.of(MetaType.BALK).goesAfter(balkGoesAfter).strippable(supposedStrippedBalk)));
        this.stub = PlantopiaBlocks.registerBlock(baseName + "_stub", properties -> new PlantopiaBalkStubBlock(properties, balk), config.applyBlockMeta(MetaProperties.of(MetaType.BALK_STUB).parent(balk).strippable(supposedStrippedStub)));
        this.strippedBalk = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_balk", properties -> new PlantopiaBalkBlock(properties, supposedStrippedStub), config.applyBlockMeta(MetaProperties.of(MetaType.BALK).goesAfter(strippedBalkGoesAfter)));
        this.strippedStub = PlantopiaBlocks.registerBlock("stripped_" + baseName + "_stub", properties -> new PlantopiaBalkStubBlock(properties, strippedBalk), config.applyBlockMeta(MetaProperties.of(MetaType.BALK_STUB).parent(strippedBalk)));

        this.balksBlockTag = PlantopiaBlockTags.createBlockTag(baseName + "_balks");
        this.balksItemTag = PlantopiaItemTags.createItemTag(baseName + "_balks");
    }

    @Override
    protected void addBlockTags(PlantopiaDatagenBridgeEvent.BlockTagEvent.Bridge bridge) {
        super.addBlockTags(bridge);

        var balks = bridge.getOrCreateTagSet(balksBlockTag);
        var overworldNaturalBlocks = bridge.getOrCreateTagSet(BlockTags.OVERWORLD_NATURAL_LOGS);

        balks.add(balk.get(), stub.get(), strippedBalk.get(), strippedStub.get());

        if (config.dimensionType() == Level.OVERWORLD) {
            overworldNaturalBlocks.add(balk.get(), stub.get());
        }
    }

    @Override
    protected void addItemTags(PlantopiaDatagenBridgeEvent.ItemTagEvent.Bridge bridge) {
        super.addItemTags(bridge);

        var balks = bridge.getOrCreateTagSet(balksItemTag);

        balks.add(balk.get().asItem(), stub.get().asItem(), strippedBalk.get().asItem(), strippedStub.get().asItem());
    }

    @Override
    protected void addRecipes(PlantopiaDatagenBridgeEvent.RecipeEvent.Bridge bridge) {
        super.addRecipes(bridge);

        bridge.balksFromLogs(balk.get(), log.get());
        bridge.balksFromLogs(strippedBalk.get(), strippedLog.get());
    }
}
