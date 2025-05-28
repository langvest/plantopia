package by.langvest.plantopia.item;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.item.special.PlantopiaRenderedIconItem;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta.MetaType;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaItems {
	private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Plantopia.MOD_ID);

	public static final RegistryObject<Item> FLOWERS_ICON = registerItem("flowers_icon", () -> new PlantopiaRenderedIconItem(new Properties()), MetaProperties.copy(MetaType.ICON));

	public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier, MetaProperties metaProperties) {
		var registryObject = ITEM_REGISTER.register(name, supplier);
		PlantopiaMetaRegistries.ITEMS.associate(name, new PlantopiaItemMeta(registryObject, metaProperties));
		return registryObject;
	}

	public static void registerBlockItem(@NotNull PlantopiaBlockMeta blockMeta) {
		if(!blockMeta.hasItem()) return;

		var properties = new Properties();
		var supplier = PlantopiaItemHelper.getBlockItemSupplier(blockMeta, properties);

		registerItem(blockMeta.getName(), supplier, MetaProperties.copy(MetaType.BLOCK).group(blockMeta.getGroups()).customBurnTime(blockMeta.getBurnTime()));
	}

	public static void setup(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}
}