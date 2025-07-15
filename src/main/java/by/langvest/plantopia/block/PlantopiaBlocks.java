package by.langvest.plantopia.block;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaCompats.Compostability;
import by.langvest.plantopia.block.special.*;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
	public static final RegistryObject<Block> HOGWEED = registerBlock("hogweed", () -> new PlantopiaHogweedBlock(Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.4F)), MetaProperties.copy(MetaType.PLANT).order(PlantopiaOrderType.EXOTIC_PLANT).tripleHigh().doubleWide().customModel().compostable(Compostability.PLANT_3 * 1.5F));
	public static final RegistryObject<Block> CLOVER = registerBlock("clover", () -> new PlantopiaCloverBlock(Properties.copy(Blocks.GRASS).offsetType(OffsetType.NONE).sound(SoundType.AZALEA)), MetaProperties.copy(MetaType.PLANT).order(PlantopiaOrderType.CLOVER).customModel().grassTint().customDrop().compostable(Compostability.PLANT_1 * 0.75F));
	public static final RegistryObject<Block> BIG_CLOVER = registerBlock("big_clover", () -> new PlantopiaBigCloverBlock(Properties.of().mapColor(MapColor.PLANT).instabreak().sound(SoundType.AZALEA).ignitedByLava().pushReaction(PushReaction.DESTROY)), MetaProperties.copy(MetaType.PLANT).order(PlantopiaOrderType.CLOVER).customModel().grassTint().pottable());
	public static final RegistryObject<Block> WHITE_CLOVER_BLOSSOM = registerBlock("white_clover_blossom", () -> new PlantopiaCloverBlossomBlock(() -> MobEffects.LUCK, 14, Properties.copy(Blocks.DANDELION).sound(SoundType.AZALEA)), MetaProperties.copy(MetaType.FLOWER).order(PlantopiaOrderType.CLOVER).customModel().grassTint().dye(Items.LIGHT_GRAY_DYE));
	public static final RegistryObject<Block> PINK_CLOVER_BLOSSOM = registerBlock("pink_clover_blossom", () -> new PlantopiaCloverBlossomBlock(() -> MobEffects.LUCK, 14, Properties.copy(Blocks.DANDELION).sound(SoundType.AZALEA)), MetaProperties.copy(MetaType.FLOWER).order(PlantopiaOrderType.CLOVER).customModel().grassTint().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> COBBLESTONE_SHARD = registerBlock("cobblestone_shard", () -> new PlantopiaCobblestoneShardBlock(Properties.of().strength(0.2F).sound(SoundType.DRIPSTONE_BLOCK)), MetaProperties.copy(MetaType.STONE).customModel().customDrop().customItem());
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_SHARD = registerBlock("mossy_cobblestone_shard", () -> new PlantopiaCobblestoneShardBlock(Properties.of().strength(0.2F).sound(SoundType.DRIPSTONE_BLOCK)), MetaProperties.copy(MetaType.STONE).customModel().customDrop().customItem());
	public static final RegistryObject<Block> COBBLESTONE_SHARD_PET = registerBlock("cobblestone_shard_pet", () -> new PlantopiaCobblestoneShardPetBlock(COBBLESTONE_SHARD, Properties.of().strength(0.2F).sound(SoundType.DRIPSTONE_BLOCK)), MetaProperties.copy(MetaType.STONE).customModel().customDrop().cutoutMippedRender().noItem());
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_SHARD_PET = registerBlock("mossy_cobblestone_shard_pet", () -> new PlantopiaCobblestoneShardPetBlock(MOSSY_COBBLESTONE_SHARD, Properties.of().strength(0.2F).sound(SoundType.DRIPSTONE_BLOCK)), MetaProperties.copy(MetaType.STONE).customModel().customDrop().cutoutMippedRender().noItem());
	public static final RegistryObject<Block> GIANT_GRASS = registerBlock("giant_grass", () -> new PlantopiaTriplePlantBlock(Properties.copy(Blocks.TALL_GRASS)), MetaProperties.copy(MetaType.PLANT).tripleHigh().grassTint().customDrop().compostable(Compostability.PLANT_3));
	public static final RegistryObject<Block> GIANT_FERN = registerBlock("giant_fern", () -> new PlantopiaTriplePlantBlock(Properties.copy(Blocks.LARGE_FERN)), MetaProperties.copy(MetaType.PLANT).tripleHigh().grassTint().customDrop().compostable(Compostability.PLANT_3).customModel());
	public static final RegistryObject<Block> BRANCHING_SHRUB = registerBlock("branching_shrub", () -> new PlantopiaBranchingShrubBlock(Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.8F).dynamicShape().sound(SoundType.MANGROVE_ROOTS)), MetaProperties.copy(MetaType.WOODY_PLANT).customDrop().pottable());
	public static final RegistryObject<Block> BRANCHING_SHRUB_PLANT = registerBlock("branching_shrub_plant", () -> new PlantopiaBranchingShrubPlantBlock(Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.8F).dynamicShape().sound(SoundType.MANGROVE_ROOTS)), MetaProperties.copy(MetaType.WOODY_PLANT).customDrop().noItem());
	public static final RegistryObject<Block> THORNY_SHRUB = registerBlock("thorny_shrub", () -> new PlantopiaThornyShrubBlock(Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().ignitedByLava().strength(0.2F).sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)), MetaProperties.copy(MetaType.WOODY_PLANT).dropSelfByShears().customModel());
	public static final RegistryObject<Block> FIREWEED = registerBlock("fireweed", () -> new PlantopiaFireweedBlock(Properties.copy(Blocks.TALL_GRASS)), MetaProperties.copy(MetaType.PLANT).doubleHigh().customModel().customTint().dropSelfByShears().preferredByBees().compostable(Compostability.PLANT_2 + Compostability.HAS_FLOWERS));
	public static final RegistryObject<Block> POLLINATED_DANDELION = registerBlock("pollinated_dandelion", () -> new PlantopiaPollinatedDandelionBlock(Properties.copy(Blocks.DANDELION)), MetaProperties.copy(MetaType.FLOWER).customModel().customDrop().noItem());
	public static final RegistryObject<Block> FLUFFY_DANDELION = registerBlock("fluffy_dandelion", () -> new PlantopiaFluffyDandelionBlock(() -> MobEffects.SLOW_FALLING, 7, Properties.copy(Blocks.DANDELION)), MetaProperties.copy(MetaType.FLOWER).ignoredByBees().noDye());
	public static final RegistryObject<Block> TINY_CACTUS = registerBlock("tiny_cactus", () -> new PlantopiaTinyCactusBlock(Properties.of().mapColor(MapColor.PLANT).instabreak().noCollission().sound(SoundType.WOOL).offsetType(OffsetType.XZ).pushReaction(PushReaction.DESTROY)), MetaProperties.copy(MetaType.PLANT).noTint().pottable());
	public static final RegistryObject<Block> FLOWERING_TINY_CACTUS = registerBlock("flowering_tiny_cactus", () -> new PlantopiaTinyCactusBlock(Properties.of().mapColor(MapColor.PLANT).instabreak().noCollission().sound(SoundType.WOOL).offsetType(OffsetType.XZ).pushReaction(PushReaction.DESTROY)), MetaProperties.copy(MetaType.PLANT).noTint().pottable());
	public static final RegistryObject<Block> QUICKSAND = registerBlock("quicksand", () -> new PlantopiaQuicksandBlock(Properties.copy(Blocks.SAND).dynamicShape().forceSolidOn().isRedstoneConductor(PlantopiaBlocks::never)), MetaProperties.copy(MetaType.SAND).noItem().noDrop().customModel());
	public static final RegistryObject<Block> QUICKSAND_CAULDRON = registerBlock("quicksand_cauldron", () -> new PlantopiaQuicksandCauldronBlock(PlantopiaBlocks.QUICKSAND, Properties.copy(Blocks.CAULDRON)), MetaProperties.copy(MetaType.IRON).noItem().customModel());
	public static final RegistryObject<Block> RED_FOXGLOVE = registerBlock("red_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.RED_DYE));
	public static final RegistryObject<Block> ORANGE_FOXGLOVE = registerBlock("orange_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.ORANGE_DYE));
	public static final RegistryObject<Block> YELLOW_FOXGLOVE = registerBlock("yellow_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.YELLOW_DYE));
	public static final RegistryObject<Block> WHITE_FOXGLOVE = registerBlock("white_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.WHITE_DYE));
	public static final RegistryObject<Block> PINK_FOXGLOVE = registerBlock("pink_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> MAGENTA_FOXGLOVE = registerBlock("magenta_foxglove", () -> new PlantopiaFoxgloveBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.MAGENTA_DYE));
	public static final RegistryObject<Block> RED_HOLLYHOCK = registerBlock("red_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.RED_DYE));
	public static final RegistryObject<Block> YELLOW_HOLLYHOCK = registerBlock("yellow_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.YELLOW_DYE));
	public static final RegistryObject<Block> WHITE_HOLLYHOCK = registerBlock("white_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.WHITE_DYE));
	public static final RegistryObject<Block> PINK_HOLLYHOCK = registerBlock("pink_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> MAGENTA_HOLLYHOCK = registerBlock("magenta_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.MAGENTA_DYE));
	public static final RegistryObject<Block> PURPLE_HOLLYHOCK = registerBlock("purple_hollyhock", () -> new TallFlowerBlock(Properties.copy(Blocks.ROSE_BUSH)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PURPLE_DYE));
	public static final RegistryObject<Block> RED_LUPINE = registerBlock("red_lupine", () -> new TallFlowerBlock(Properties.copy(Blocks.LILAC)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.RED_DYE));
	public static final RegistryObject<Block> YELLOW_LUPINE = registerBlock("yellow_lupine", () -> new TallFlowerBlock(Properties.copy(Blocks.LILAC)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.YELLOW_DYE));
	public static final RegistryObject<Block> WHITE_LUPINE = registerBlock("white_lupine", () -> new TallFlowerBlock(Properties.copy(Blocks.LILAC)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.WHITE_DYE));
	public static final RegistryObject<Block> PINK_LUPINE = registerBlock("pink_lupine", () -> new TallFlowerBlock(Properties.copy(Blocks.LILAC)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PINK_DYE));
	public static final RegistryObject<Block> PURPLE_LUPINE = registerBlock("purple_lupine", () -> new TallFlowerBlock(Properties.copy(Blocks.LILAC)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.PURPLE_DYE));
	public static final RegistryObject<Block> BLUE_LUPINE = registerBlock("blue_lupine", () -> new TallFlowerBlock(Properties.copy(Blocks.LILAC)), MetaProperties.copy(MetaType.FLOWER).doubleHigh().customModel().dye(Items.BLUE_DYE));
	public static final RegistryObject<Block> WATERGRASS = registerBlock("watergrass", () -> new PlantopiaWatergrassBlock(Properties.copy(Blocks.TALL_GRASS).mapColor(MapColor.PLANT)), MetaProperties.copy(MetaType.WATER_PLANT).order(PlantopiaOrderType.EXOTIC_PLANT).doubleHigh().customDrop().customModel().grassTint().notTintedItem());
	public static final RegistryObject<Block> CATTAIL = registerBlock("cattail", () -> new PlantopiaWaterloggedDoublePlantBlock(Properties.copy(Blocks.TALL_GRASS).mapColor(MapColor.PLANT)), MetaProperties.copy(MetaType.WATER_PLANT).order(PlantopiaOrderType.EXOTIC_PLANT).doubleHigh().dropSelfByShears().customModel().grassTint().notTintedItem());
	public static final RegistryObject<Block> REEDS = registerBlock("reeds", () -> new PlantopiaReedsBlock(Properties.copy(Blocks.TALL_GRASS).mapColor(MapColor.WOOD)), MetaProperties.copy(MetaType.WATER_PLANT).order(PlantopiaOrderType.EXOTIC_PLANT).doubleHigh().dropSelfByShears());
	public static final RegistryObject<Block> SEA_OATS = registerBlock("sea_oats", () -> new PlantopiaSeaOatsBlock(Properties.copy(Blocks.TALL_GRASS).mapColor(MapColor.SAND)), MetaProperties.copy(MetaType.PLANT).order(PlantopiaOrderType.EXOTIC_PLANT).doubleHigh().dropSelfByShears());
	public static final RegistryObject<Block> SEA_MOSS = registerBlock("sea_moss", () -> new PlantopiaSeaMossBlock(Properties.of().randomTicks().noCollission().instabreak().sound(SoundType.WET_GRASS)), MetaProperties.copy(MetaType.UNDERWATER_PLANT).order(PlantopiaOrderType.WET_PLANT).customDrop().customModel());
	public static final RegistryObject<Block> SEA_MOSS_PLANT = registerBlock("sea_moss_plant", () -> new PlantopiaSeaMossPlantBlock(Properties.of().noCollission().instabreak().sound(SoundType.WET_GRASS)), MetaProperties.copy(MetaType.UNDERWATER_PLANT).customDrop().customModel().noItem());
	public static final RegistryObject<Block> SMALL_PLATTERLEAF = registerBlock("small_platterleaf", () -> new PlantopiaSmallPlatterleafBlock(Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).dropSelf().customModel().customItem());
	public static final RegistryObject<Block> BIG_PLATTERLEAF = registerBlock("big_platterleaf", () -> new PlantopiaBigPlatterleafBlock(Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).customDrop().customModel().customItem());
	public static final RegistryObject<Block> RED_WATERLILY = registerBlock("red_waterlily", () -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, Properties.copy(Blocks.LILY_PAD).sound(SoundType.CHERRY_LEAVES).noCollission()), MetaProperties.copy(MetaType.WATERLILY_FLOWER).dye(Items.RED_DYE).customItem().dropSelf().lilyPadTint());
	public static final RegistryObject<Block> YELLOW_WATERLILY = registerBlock("yellow_waterlily", () -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, Properties.copy(Blocks.LILY_PAD).sound(SoundType.CHERRY_LEAVES).noCollission()), MetaProperties.copy(MetaType.WATERLILY_FLOWER).dye(Items.YELLOW_DYE).customItem().dropSelf().lilyPadTint());
	public static final RegistryObject<Block> WHITE_WATERLILY = registerBlock("white_waterlily", () -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, Properties.copy(Blocks.LILY_PAD).sound(SoundType.CHERRY_LEAVES).noCollission()), MetaProperties.copy(MetaType.WATERLILY_FLOWER).dye(Items.WHITE_DYE).customItem().dropSelf().lilyPadTint());
	public static final RegistryObject<Block> PINK_WATERLILY = registerBlock("pink_waterlily", () -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, Properties.copy(Blocks.LILY_PAD).sound(SoundType.CHERRY_LEAVES).noCollission()), MetaProperties.copy(MetaType.WATERLILY_FLOWER).dye(Items.PINK_DYE).customItem().dropSelf().lilyPadTint());
	public static final RegistryObject<Block> RED_FLOWERING_LILY_PAD = registerBlock("red_flowering_lily_pad", () -> new PlantopiaFloweringLilyPadBlock(RED_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem().lilyPadTint());
	public static final RegistryObject<Block> YELLOW_FLOWERING_LILY_PAD = registerBlock("yellow_flowering_lily_pad", () -> new PlantopiaFloweringLilyPadBlock(YELLOW_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem().lilyPadTint());
	public static final RegistryObject<Block> WHITE_FLOWERING_LILY_PAD = registerBlock("white_flowering_lily_pad", () -> new PlantopiaFloweringLilyPadBlock(WHITE_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem().lilyPadTint());
	public static final RegistryObject<Block> PINK_FLOWERING_LILY_PAD = registerBlock("pink_flowering_lily_pad", () -> new PlantopiaFloweringLilyPadBlock(PINK_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem().lilyPadTint());
	public static final RegistryObject<Block> RED_FLOWERING_SMALL_PLATTERLEAF = registerBlock("red_flowering_small_platterleaf", () -> new PlantopiaFloweringSmallPlatterleafBlock(RED_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem());
	public static final RegistryObject<Block> YELLOW_FLOWERING_SMALL_PLATTERLEAF = registerBlock("yellow_flowering_small_platterleaf", () -> new PlantopiaFloweringSmallPlatterleafBlock(YELLOW_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem());
	public static final RegistryObject<Block> WHITE_FLOWERING_SMALL_PLATTERLEAF = registerBlock("white_flowering_small_platterleaf", () -> new PlantopiaFloweringSmallPlatterleafBlock(WHITE_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem());
	public static final RegistryObject<Block> PINK_FLOWERING_SMALL_PLATTERLEAF = registerBlock("pink_flowering_small_platterleaf", () -> new PlantopiaFloweringSmallPlatterleafBlock(PINK_WATERLILY, Properties.copy(Blocks.LILY_PAD)), MetaProperties.copy(MetaType.WATERLILY).noItem());
	public static final RegistryObject<Block> AZOLLA = registerBlock("azolla", () -> new PlantopiaAzollaBlock(Properties.copy(Blocks.LILY_PAD).sound(SoundType.WET_GRASS).replaceable().noCollission()), MetaProperties.copy(MetaType.WATER_PLANT).customModel().customDrop().customItem());

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

	/* HELPER METHODS *****************************************************************************************/

	@Contract(pure = true)
	private static @NotNull Boolean never(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
}