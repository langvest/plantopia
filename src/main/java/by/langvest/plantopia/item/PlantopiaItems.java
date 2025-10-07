package by.langvest.plantopia.item;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.item.special.*;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaItemMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.util.helper.PlantopiaItemHelper;
import by.langvest.toolkit.event.RegistryEvent;
import by.langvest.toolkit.registry.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SolidBucketItem;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaItems {
	public static final RegistryObject<Item> FLOWERS_ICON = registerItem("flowers_icon", PlantopiaRenderedIconItem::new, MetaProperties.of(MetaType.ICON));
	public static final RegistryObject<Item> COBBLESTONE_SHARD = registerItem("cobblestone_shard", properties -> new PlantopiaCobblestoneShardBlockItem(PlantopiaBlocks.COBBLESTONE_SHARD.get(), PlantopiaBlocks.COBBLESTONE_SHARD_PET.get(), properties), MetaProperties.of(MetaType.COBBLESTONE_SHARD_BLOCK));
	public static final RegistryObject<Item> MOSSY_COBBLESTONE_SHARD = registerItem("mossy_cobblestone_shard", properties -> new PlantopiaCobblestoneShardBlockItem(PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get(), PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD_PET.get(), properties), MetaProperties.of(MetaType.COBBLESTONE_SHARD_BLOCK));
	public static final RegistryObject<Item> QUICKSAND_BUCKET = registerItem("quicksand_bucket", properties -> new SolidBucketItem(PlantopiaBlocks.QUICKSAND.get(), SoundEvents.BUCKET_EMPTY_POWDER_SNOW, properties), MetaProperties.of(MetaType.ITEM).stacksTo(1));
	public static final RegistryObject<Item> SMALL_PLATTERLEAF = registerItem("small_platterleaf", properties -> new PlantopiaPlatterleafBlockItem(PlantopiaBlocks.SMALL_PLATTERLEAF.get(), properties), MetaProperties.of(MetaType.WATERLILY_BLOCK));
	public static final RegistryObject<Item> BIG_PLATTERLEAF = registerItem("big_platterleaf", properties -> new PlantopiaPlatterleafBlockItem(PlantopiaBlocks.BIG_PLATTERLEAF.get(), properties), MetaProperties.of(MetaType.WATERLILY_BLOCK));
	public static final RegistryObject<Item> RED_WATERLILY = registerItem("red_waterlily", properties -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.RED_WATERLILY.get(), properties), MetaProperties.of(MetaType.WATERLILY_BLOCK));
	public static final RegistryObject<Item> YELLOW_WATERLILY = registerItem("yellow_waterlily", properties -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.YELLOW_WATERLILY.get(), properties), MetaProperties.of(MetaType.WATERLILY_BLOCK));
	public static final RegistryObject<Item> WHITE_WATERLILY = registerItem("white_waterlily", properties -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.WHITE_WATERLILY.get(), properties), MetaProperties.of(MetaType.WATERLILY_BLOCK));
	public static final RegistryObject<Item> PINK_WATERLILY = registerItem("pink_waterlily", properties -> new PlantopiaWaterlilyFlowerBlockItem(PlantopiaBlocks.PINK_WATERLILY.get(), properties), MetaProperties.of(MetaType.WATERLILY_BLOCK));
	public static final RegistryObject<Item> AZOLLA = registerItem("azolla", properties -> new PlantopiaAzollaBlockItem(PlantopiaBlocks.AZOLLA.get(), properties), MetaProperties.of(MetaType.BLOCK).order(PlantopiaOrderType.WET_PLANT));
	public static final RegistryObject<Item> ROUND_SEA_SHELL = registerItem("round_sea_shell", properties -> new PlantopiaSeaShellBlockItem(PlantopiaBlocks.ROUND_SEA_SHELL.get(), properties), MetaProperties.of(MetaType.SHELL_BLOCK));
	public static final RegistryObject<Item> TWISTY_SEA_SHELL = registerItem("twisty_sea_shell", properties -> new PlantopiaSeaShellBlockItem(PlantopiaBlocks.TWISTY_SEA_SHELL.get(), properties), MetaProperties.of(MetaType.SHELL_BLOCK));
	public static final RegistryObject<Item> TUBE_SEA_SHELL = registerItem("tube_sea_shell", properties -> new PlantopiaSeaShellBlockItem(PlantopiaBlocks.TUBE_SEA_SHELL.get(), properties), MetaProperties.of(MetaType.SHELL_BLOCK));
	public static final RegistryObject<Item> WHITE_LUCKY_DAISY = registerItem("white_lucky_daisy", properties -> new PlantopiaLuckyDaisyBlockItem(PlantopiaBlocks.WHITE_LUCKY_DAISY.get(), properties), MetaProperties.of(MetaType.LUCKY_DAISY_BLOCK));
	public static final RegistryObject<Item> PINK_LUCKY_DAISY = registerItem("pink_lucky_daisy", properties -> new PlantopiaLuckyDaisyBlockItem(PlantopiaBlocks.PINK_LUCKY_DAISY.get(), properties), MetaProperties.of(MetaType.LUCKY_DAISY_BLOCK));

	public static <T extends Item> RegistryObject<T> registerItem(String name, Function<Properties, T> factory, MetaProperties metaProperties) {
		return registerItem(plantopiaLocationFrom(name), factory, metaProperties);
	}

	public static <T extends Item> RegistryObject<T> registerItem(ResourceLocation identifier, Function<Properties, T> factory, MetaProperties metaProperties) {
		var itemMeta = PlantopiaMetaBuckets.ITEM.associate(identifier, new PlantopiaItemMeta(identifier, metaProperties));
		return PlantopiaRegistries.ITEM.register(identifier, () -> factory.apply(itemMeta.createBehaviourProperties()));
	}

	public static void registerBlockItem(@NotNull PlantopiaBlockMeta blockMeta) {
		if(!blockMeta.shouldGenerateItem()) return;

		var groups = blockMeta.getGroups();
		var burnTime = blockMeta.getBurnTime();
		var orderType = blockMeta.getOrderType();

		var factory = PlantopiaItemHelper.getBlockItemFactory(blockMeta);
		var metaProperties = MetaProperties.of(MetaType.BLOCK).group(groups).customBurnTime(burnTime).order(orderType);

		registerItem(blockMeta.getIdentifier(), factory, metaProperties);
	}

	public static void setup(@NotNull RegistryEvent event) {
		event.registerAll(Registries.ITEM, PlantopiaRegistries.ITEM);
	}
}