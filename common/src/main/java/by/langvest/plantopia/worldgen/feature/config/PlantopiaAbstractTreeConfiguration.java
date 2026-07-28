package by.langvest.plantopia.worldgen.feature.config;

import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.List;

public abstract class PlantopiaAbstractTreeConfiguration {
    public interface WithDecorators {
        List<TreeDecorator> decorators();
    }
}
