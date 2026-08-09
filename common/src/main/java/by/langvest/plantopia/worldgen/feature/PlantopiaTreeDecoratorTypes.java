package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaAlterBaseLogDecorator;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaBranchDecorator;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaFruitDecorator;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

public class PlantopiaTreeDecoratorTypes {
    public static final RegistryObject<TreeDecoratorType<PlantopiaAlterBaseLogDecorator>> ALTER_BASE_LOG = registerTreeDecoratorType("alter_base_log", () -> new TreeDecoratorType<>(PlantopiaAlterBaseLogDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<PlantopiaFruitDecorator>> FRUIT = registerTreeDecoratorType("fruit", () -> new TreeDecoratorType<>(PlantopiaFruitDecorator.CODEC));
    public static final RegistryObject<TreeDecoratorType<PlantopiaBranchDecorator>> BRANCH = registerTreeDecoratorType("branch", () -> new TreeDecoratorType<>(PlantopiaBranchDecorator.CODEC));

    private static <T extends TreeDecorator> RegistryObject<TreeDecoratorType<T>> registerTreeDecoratorType(String name, Supplier<TreeDecoratorType<T>> supplier) {
        return registerTreeDecoratorType(plantopia(name), supplier);
    }

    private static <T extends TreeDecorator> RegistryObject<TreeDecoratorType<T>> registerTreeDecoratorType(ResourceLocation identifier, Supplier<TreeDecoratorType<T>> supplier) {
        return PlantopiaRegistries.TREE_DECORATOR_TYPE.register(identifier, supplier);
    }

    public static void setup(@NotNull RegisterEvent event) {
        event.registerAll(Registries.TREE_DECORATOR_TYPE, PlantopiaRegistries.TREE_DECORATOR_TYPE);
    }
}
