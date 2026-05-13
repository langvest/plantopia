package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.special.PlantopiaMapleLeavesBlock;
import by.langvest.plantopia.datagen.recipe.PlantopiaRecipeProvider;
import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.particle.PlantopiaParticleTypes;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class PlantopiaMapleKit extends PlantopiaAbstractTreeKit {
    protected final PlantopiaTreeStuffKit stuff;
    protected final PlantopiaTreeTrunkKit trunk;

    protected final RegistryObject<Block> yellowLeaves;
    protected final RegistryObject<Block> orangeLeaves;
    protected final RegistryObject<Block> redLeaves;

    protected PlantopiaMapleKit(String baseName, PlantopiaTreeKitConfiguration config) {
        super(baseName, config);

        this.yellowLeaves = PlantopiaBlocks.registerBlock("yellow_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.YELLOW_MAPLE_LEAVES, properties), config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_YELLOW)));
        this.orangeLeaves = PlantopiaBlocks.registerBlock("orange_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.ORANGE_MAPLE_LEAVES, properties), config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_ORANGE)));
        this.redLeaves = PlantopiaBlocks.registerBlock("red_" + baseName + "_leaves", properties -> new PlantopiaMapleLeavesBlock(PlantopiaParticleTypes.RED_MAPLE_LEAVES, properties), config.applyBlockMeta(MetaProperties.of(MetaType.LEAVES).mapColor(MapColor.COLOR_RED)));
        this.trunk = PlantopiaTreeTrunkKit.registerTreeTrunkKit(baseName, config);
        this.stuff = PlantopiaTreeStuffKit.registerTreeStuffKit(baseName, woodType, config);
    }

    public static @NotNull PlantopiaMapleKit registerMapleKit(String baseName, PlantopiaTreeKitConfiguration config) {
        return new PlantopiaMapleKit(baseName, config);
    }

    public PlantopiaTreeStuffKit stuff() {
        return stuff;
    }

    public PlantopiaTreeTrunkKit trunk() {
        return trunk;
    }

    public RegistryObject<Block> yellowLeaves() {
        return yellowLeaves;
    }

    public RegistryObject<Block> orangeLeaves() {
        return orangeLeaves;
    }

    public RegistryObject<Block> redLeaves() {
        return redLeaves;
    }

    @Override
    protected void addRecipes() {
        super.addRecipes();

        PlantopiaRecipeProvider.planksFromLogs(stuff.planks().get(), trunk.logsItemTag(), 4);
        PlantopiaRecipeProvider.hangingSign(stuff.hangingSign().get(), trunk.strippedLog().get());
    }
}
