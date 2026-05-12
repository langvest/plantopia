package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.options.PlantopiaTreeOptions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public abstract class PlantopiaAbstractTreeKit {
    protected final String baseName;
    protected final BlockSetType blockSetType;
    protected final WoodType woodType;

    protected PlantopiaAbstractTreeKit(String baseName, @NotNull PlantopiaTreeOptions config) {
        super();
        this.baseName = baseName;
        this.blockSetType = config.blockSetTypeFactory().apply(plantopia(baseName));
        this.woodType = config.woodTypeFactory().apply(Pair.of(plantopia(baseName), blockSetType));
    }

    public BlockSetType blockSetType() {
        return blockSetType;
    }

    public WoodType woodType() {
        return woodType;
    }
}
