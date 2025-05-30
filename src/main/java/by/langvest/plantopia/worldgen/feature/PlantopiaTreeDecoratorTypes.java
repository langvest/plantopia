package by.langvest.plantopia.worldgen.feature;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.worldgen.feature.treedecorator.PlantopiaBirchBaseBlockDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PlantopiaTreeDecoratorTypes {
	private static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATOR_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Plantopia.MOD_ID);

	public static final RegistryObject<TreeDecoratorType<PlantopiaBirchBaseBlockDecorator>> BIRCH_BASE_BLOCK = registerTreeDecoratorType("birch_base_block", () -> new TreeDecoratorType<>(PlantopiaBirchBaseBlockDecorator.CODEC));

	private static <T extends TreeDecorator> RegistryObject<TreeDecoratorType<T>> registerTreeDecoratorType(String name, Supplier<TreeDecoratorType<T>> supplier) {
		return TREE_DECORATOR_TYPE_REGISTER.register(name, supplier);
	}

	public static void setup(IEventBus bus) {
		TREE_DECORATOR_TYPE_REGISTER.register(bus);
	}
}