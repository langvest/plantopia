package by.langvest.plantopia.item;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.item.special.*;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PlantopiaItems {
	private static final DeferredRegister<Item> ITEM_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, Plantopia.MOD_ID);

	public static final RegistryObject<Item> FLOWERS_ICON = registerItem("flowers_icon", () -> new PlantopiaRenderedIconItem(new Properties()), MetaProperties.copy(MetaType.ICON));
	public static final RegistryObject<Item> COBBLESTONE_SHARD = registerItem("cobblestone_shard", () -> new PlantopiaCobblestoneShardBlockItem(PlantopiaBlocks.COBBLESTONE_SHARD.get(), PlantopiaBlocks.COBBLESTONE_SHARD_PET.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.COBBLESTONE_SHARD));
	public static final RegistryObject<Item> MOSSY_COBBLESTONE_SHARD = registerItem("mossy_cobblestone_shard", () -> new PlantopiaCobblestoneShardBlockItem(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get(), PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.COBBLESTONE_SHARD));
	public static final RegistryObject<Item> QUICKSAND_BUCKET = registerItem("quicksand_bucket", () -> new SolidBucketItem(PlantopiaBlocks.QUICKSAND.get(), SoundEvents.BUCKET_EMPTY_POWDER_SNOW, new Properties().stacksTo(1)), MetaProperties.copy(MetaType.ITEM));
	public static final RegistryObject<Item> SMALL_PLATTERLEAF = registerItem("small_platterleaf", () -> new PlantopiaPlatterleafBlockItem(PlantopiaBlocks.SMALL_PLATTERLEAF.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> BIG_PLATTERLEAF = registerItem("big_platterleaf", () -> new PlantopiaPlatterleafBlockItem(PlantopiaBlocks.BIG_PLATTERLEAF.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> RED_WATERLILY = registerItem("red_waterlily", () -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.RED_WATERLILY.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> YELLOW_WATERLILY = registerItem("yellow_waterlily", () -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.YELLOW_WATERLILY.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> WHITE_WATERLILY = registerItem("white_waterlily", () -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.WHITE_WATERLILY.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> PINK_WATERLILY = registerItem("pink_waterlily", () -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.PINK_WATERLILY.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> AZOLLA = registerItem("azolla", () -> new PlantopiaAzollaBlockItem(PlantopiaBlocks.AZOLLA.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> ROUND_SEA_SHELL = registerItem("round_sea_shell", () -> new PlantopiaSeaShellBlockItem(PlantopiaBlocks.ROUND_SEA_SHELL.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.SEA_SHELL));
	public static final RegistryObject<Item> TWISTY_SEA_SHELL = registerItem("twisty_sea_shell", () -> new PlantopiaSeaShellBlockItem(PlantopiaBlocks.TWISTY_SEA_SHELL.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.SEA_SHELL));
	public static final RegistryObject<Item> TUBE_SEA_SHELL = registerItem("tube_sea_shell", () -> new PlantopiaSeaShellBlockItem(PlantopiaBlocks.TUBE_SEA_SHELL.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.SEA_SHELL));
	public static final RegistryObject<Item> WHITE_LUCKY_DAISY = registerItem("white_lucky_daisy", () -> new PlantopiaLuckyDaisyBlockItem(PlantopiaBlocks.WHITE_LUCKY_DAISY.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.FLOWER).customModel());
	public static final RegistryObject<Item> PINK_LUCKY_DAISY = registerItem("pink_lucky_daisy", () -> new PlantopiaLuckyDaisyBlockItem(PlantopiaBlocks.PINK_LUCKY_DAISY.get(), new Properties()), MetaProperties.copy(MetaType.BLOCK).order(PlantopiaOrderType.FLOWER).customModel());

	public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier, MetaProperties metaProperties) {
		var registryObject = ITEM_REGISTER.register(name, supplier);
		PlantopiaMetaRegistries.ITEMS.associate(name, new PlantopiaItemMeta(registryObject, metaProperties));
		return registryObject;
	}

	public static void registerBlockItem(@NotNull PlantopiaBlockMeta blockMeta) {
		if(!blockMeta.shouldGenerateItem()) return;

		var groups = blockMeta.getGroups();
		var burnTime = blockMeta.getBurnTime();
		var orderType = blockMeta.getOrderType();

		var properties = new Properties();
		var supplier = PlantopiaItemHelper.getBlockItemSupplier(blockMeta, properties);
		var metaProperties = MetaProperties.copy(MetaType.BLOCK).group(groups).customBurnTime(burnTime).order(orderType);

		registerItem(blockMeta.getName(), supplier, metaProperties);
	}

	public static void setup(IEventBus bus) {
		ITEM_REGISTER.register(bus);
	}
}