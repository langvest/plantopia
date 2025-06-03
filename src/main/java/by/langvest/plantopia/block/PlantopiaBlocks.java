package by.langvest.plantopia.block;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaCompats.Compostability;
import by.langvest.plantopia.block.special.*;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.FLOWER_POT_BLOCK;
import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.pottedNameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;

public class PlantopiaBlocks {
	private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, Plantopia.MOD_ID);

	public static final RegistryObject<Block> INFESTED_GRASS_BLOCK = registerBlock("infested_grass_block", () -> new PlantopiaInfestedGrassBlock(Properties.copy(Blocks.GRASS_BLOCK)), MetaProperties.copy(MetaType.GRASS_BLOCK).customModel().customDrop());
	public static final RegistryObject<Block> INFESTED_DIRT = registerBlock("infested_dirt", () -> new PlantopiaInfestedDirtBlock(Properties.copy(Blocks.DIRT).randomTicks()), MetaProperties.copy(MetaType.DIRT).customModel().customDrop());
	public static final RegistryObject<Block> BIRCH_BASE_LOG = registerBlock("birch_base_log", () -> new PlantopiaBirchBaseBlock(() -> Blocks.STRIPPED_BIRCH_LOG, Properties.copy(Blocks.BIRCH_LOG).mapColor(PlantopiaBirchBaseBlock::logMapColor)), MetaProperties.copy(MetaType.LOG).customModel().dropSelf());
	public static final RegistryObject<Block> BIRCH_BASE_WOOD = registerBlock("birch_base_wood", () -> new PlantopiaBirchBaseBlock(() -> Blocks.STRIPPED_BIRCH_WOOD, Properties.copy(Blocks.BIRCH_WOOD).mapColor(PlantopiaBirchBaseBlock::woodMapColor)), MetaProperties.copy(MetaType.WOOD).customModel().dropSelf());
	public static final RegistryObject<Block> FIREWEED = registerBlock("fireweed", () -> new PlantopiaFireweedBlock(Properties.copy(Blocks.TALL_GRASS)), MetaProperties.copy(MetaType.PLANT).doubleHigh().customModel().customTint().dropSelfByShears().preferredByBees().compostable(Compostability.PLANT_2 + Compostability.HAS_FLOWERS));
	public static final RegistryObject<Block> CLOVER = registerBlock("clover", () -> new PlantopiaCloverBlock(Properties.copy(Blocks.GRASS).offsetType(OffsetType.NONE).sound(SoundType.AZALEA)), MetaProperties.copy(MetaType.PLANT).customModel().grassTint().customDrop().compostable(Compostability.PLANT_1 * 0.75F));
	public static final RegistryObject<Block> BIG_CLOVER = registerBlock("big_clover", () -> new PlantopiaBigCloverBlock(Properties.of().mapColor(MapColor.PLANT).instabreak().sound(SoundType.AZALEA).ignitedByLava().pushReaction(PushReaction.DESTROY)), MetaProperties.copy(MetaType.PLANT).customModel().grassTint().pottable());
	public static final RegistryObject<Block> WHITE_CLOVER_BLOSSOM = registerBlock("white_clover_blossom", () -> new PlantopiaCloverBlossomBlock(() -> MobEffects.LUCK, 14, Properties.copy(Blocks.DANDELION).sound(SoundType.AZALEA)), MetaProperties.copy(MetaType.FLOWER).customModel().grassTint().dye(Items.LIGHT_GRAY_DYE));
	public static final RegistryObject<Block> PINK_CLOVER_BLOSSOM = registerBlock("pink_clover_blossom", () -> new PlantopiaCloverBlossomBlock(() -> MobEffects.LUCK, 14, Properties.copy(Blocks.DANDELION).sound(SoundType.AZALEA)), MetaProperties.copy(MetaType.FLOWER).customModel().grassTint().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> COBBLESTONE_SHARD = registerBlock("cobblestone_shard", () -> new PlantopiaCobblestoneShardBlock(Properties.of().strength(0.2F).sound(SoundType.DRIPSTONE_BLOCK)), MetaProperties.copy(MetaType.STONE).customModel().customDrop());
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_SHARD = registerBlock("mossy_cobblestone_shard", () -> new PlantopiaCobblestoneShardBlock(Properties.of().strength(0.2F).sound(SoundType.DRIPSTONE_BLOCK)), MetaProperties.copy(MetaType.STONE).customModel().customDrop());
	public static final RegistryObject<Block> GIANT_GRASS = registerBlock("giant_grass", () -> new PlantopiaTriplePlantBlock(Properties.copy(Blocks.TALL_GRASS)), MetaProperties.copy(MetaType.PLANT).tripleHigh().grassTint().customDrop().compostable(Compostability.PLANT_3));
	public static final RegistryObject<Block> GIANT_FERN = registerBlock("giant_fern", () -> new PlantopiaTriplePlantBlock(Properties.copy(Blocks.LARGE_FERN)), MetaProperties.copy(MetaType.PLANT).tripleHigh().grassTint().customDrop().compostable(Compostability.PLANT_3).customModel());
	public static final RegistryObject<Block> BRANCHING_SHRUB = registerBlock("branching_shrub", () -> new PlantopiaBranchingShrubBlock(Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.8F).dynamicShape().sound(SoundType.MANGROVE_ROOTS)), MetaProperties.copy(MetaType.WOODY_PLANT).customDrop().pottable());
	public static final RegistryObject<Block> BRANCHING_SHRUB_PLANT = registerBlock("branching_shrub_plant", () -> new PlantopiaBranchingShrubPlantBlock(Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.8F).dynamicShape().sound(SoundType.MANGROVE_ROOTS)), MetaProperties.copy(MetaType.WOODY_PLANT).customDrop().noGroup());
	public static final RegistryObject<Block> THORNY_SHRUB = registerBlock("thorny_shrub", () -> new PlantopiaThornyShrubBlock(Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().ignitedByLava().strength(0.2F).sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)), MetaProperties.copy(MetaType.WOODY_PLANT).dropSelfByShears().customModel());
	public static final RegistryObject<Block> RED_FOXGLOVE = registerBlock("red_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.RED_DYE));
	public static final RegistryObject<Block> ORANGE_FOXGLOVE = registerBlock("orange_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.ORANGE_DYE));
	public static final RegistryObject<Block> YELLOW_FOXGLOVE = registerBlock("yellow_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.YELLOW_DYE));
	public static final RegistryObject<Block> WHITE_FOXGLOVE = registerBlock("white_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.WHITE_DYE));
	public static final RegistryObject<Block> PINK_FOXGLOVE = registerBlock("pink_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> MAGENTA_FOXGLOVE = registerBlock("magenta_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.MAGENTA_DYE));
	public static final RegistryObject<Block> RED_HOLLYHOCK = registerBlock("red_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.RED_DYE));
	public static final RegistryObject<Block> ORANGE_HOLLYHOCK = registerBlock("orange_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.ORANGE_DYE));
	public static final RegistryObject<Block> YELLOW_HOLLYHOCK = registerBlock("yellow_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.YELLOW_DYE));
	public static final RegistryObject<Block> WHITE_HOLLYHOCK = registerBlock("white_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.WHITE_DYE));
	public static final RegistryObject<Block> PINK_HOLLYHOCK = registerBlock("pink_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> MAGENTA_HOLLYHOCK = registerBlock("magenta_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.MAGENTA_DYE));
	public static final RegistryObject<Block> POLLINATED_DANDELION = registerBlock("pollinated_dandelion", () -> new PlantopiaPollinatedDandelionBlock(Properties.copy(Blocks.DANDELION)), MetaProperties.copy(MetaType.FLOWER).customModel().customDrop().noGroup());
	public static final RegistryObject<Block> FLUFFY_DANDELION = registerBlock("fluffy_dandelion", () -> new PlantopiaFluffyDandelionBlock(() -> MobEffects.SLOW_FALLING, 7, Properties.copy(Blocks.DANDELION)), MetaProperties.copy(MetaType.FLOWER).ignoredByBees().noDye());
	public static final RegistryObject<Block> HOGWEED = registerBlock("hogweed", () -> new PlantopiaHogweedBlock(Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.4F)), MetaProperties.copy(MetaType.PLANT).tripleHigh().doubleWide().customModel().compostable(Compostability.PLANT_3 * 1.5F));

	static {
		registerPottedBlocks();
	}

	public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> supplier, MetaProperties metaProperties) {
		var registryObject = BLOCK_REGISTER.register(name, supplier);
		var blockMeta = PlantopiaMetaRegistries.BLOCKS.associate(name, new PlantopiaBlockMeta(registryObject, metaProperties));

		PlantopiaItems.registerBlockItem(blockMeta);

		return registryObject;
	}

	@SuppressWarnings("UnusedReturnValue")
	public static RegistryObject<FlowerPotBlock> registerPottedBlock(String plantName, Supplier<? extends Block> plantSupplier, PlantopiaTintType plantTintType) {
		return registerBlock(pottedNameOf(plantName), () -> new FlowerPotBlock(() -> FLOWER_POT_BLOCK, plantSupplier, Properties.copy(FLOWER_POT_BLOCK)), MetaProperties.copy(MetaType.POTTED).pottedTint(plantTintType));
	}

	private static void registerPottedBlocks() {
		registerPottedBlock(nameOf(Blocks.GRASS), () -> Blocks.GRASS, PlantopiaTintType.GRASS);

		PlantopiaMetaRegistries.BLOCKS.findAll(PlantopiaBlockMeta::isPottable).forEach(blockMeta ->
			registerPottedBlock(blockMeta.getName(), blockMeta.getTarget(), blockMeta.getTintType())
		);
	}

	public static void setup(IEventBus bus) {
		BLOCK_REGISTER.register(bus);
	}
}