package by.langvest.plantopia.kit.special;

import by.langvest.plantopia.kit.config.PlantopiaTreeKitConfiguration;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

@ParametersAreNonnullByDefault
public abstract class PlantopiaAbstractTreeKit extends PlantopiaKit {
    protected final String baseName;
    protected final PlantopiaTreeKitConfiguration config;

    public final BlockSetType blockSetType;
    public final WoodType woodType;

    protected PlantopiaAbstractTreeKit(String baseName, @NotNull PlantopiaTreeKitConfiguration config) {
        super();
        this.baseName = baseName;
        this.config = config;
        this.blockSetType = config.blockSetTypeFactory().apply(plantopia(baseName));
        this.woodType = config.woodTypeFactory().apply(Pair.of(plantopia(baseName), blockSetType));
    }
}
