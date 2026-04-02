package by.langvest.plantopia.block;

import by.langvest.plantopia.worldgen.feature.PlantopiaTreeFeatures;
import by.langvest.toolkit.event.RegisterEvent;
import by.langvest.toolkit.platform.RegistryHelper;
import by.langvest.toolkit.registry.RegistryObject;
import by.langvest.plantopia.registry.PlantopiaRegistries;
import by.langvest.plantopia.compat.PlantopiaCompats.Compostability;
import by.langvest.plantopia.block.special.*;
import by.langvest.plantopia.item.PlantopiaItems;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaProperties;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import by.langvest.plantopia.meta.property.PlantopiaOrderType;
import by.langvest.plantopia.meta.property.PlantopiaTintType;
import by.langvest.toolkit.registry.SupposedRegistryObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.pottedNameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopia;

/**
 * @see net.minecraft.world.level.block.Blocks
 */
public class PlantopiaBlocks {
	public static final RegistryObject<Block> BIRCH_BASE_LOG = registerBlock("birch_base_log", properties -> new PlantopiaBirchBaseBlock(() -> Blocks.STRIPPED_BIRCH_LOG, properties), MetaProperties.of(MetaType.LOG).mapColor(PlantopiaBirchBaseBlock::logMapColor).customModel());
	public static final RegistryObject<Block> BIRCH_BASE_WOOD = registerBlock("birch_base_wood", properties -> new PlantopiaBirchBaseBlock(() -> Blocks.STRIPPED_BIRCH_WOOD, properties), MetaProperties.of(MetaType.WOOD).mapColor(PlantopiaBirchBaseBlock::woodMapColor).customModel());

	public static final RegistryObject<Block> INFESTED_GRASS_BLOCK = registerBlock("infested_grass_block", PlantopiaInfestedGrassBlock::new, MetaProperties.of(MetaType.GRASS_BLOCK).customModel().customDrop());
	public static final RegistryObject<Block> INFESTED_DIRT = registerBlock("infested_dirt", PlantopiaInfestedDirtBlock::new, MetaProperties.of(MetaType.DIRT).randomlyTicking().customModel().customDrop());
	public static final RegistryObject<Block> HOGWEED = registerBlock("hogweed", PlantopiaHogweedBlock::new, MetaProperties.of(MetaType.PLANT).strength(0.4F).order(PlantopiaOrderType.EXOTIC_PLANT).tripleHighPlant().doubleWide().customModel());

	public static final RegistryObject<Block> CLOVER = registerBlock("clover", PlantopiaCloverBlock::new, MetaProperties.of(MetaType.CLOVER).replaceable().customModel().customDrop().compostable(Compostability.PLANT_1 * 0.75F));
	public static final RegistryObject<Block> BIG_CLOVER = registerBlock("big_clover", PlantopiaBigCloverBlock::new, MetaProperties.of(MetaType.CLOVER).hasCollision().customModel().pottable());
	public static final RegistryObject<Block> WHITE_CLOVER_BLOSSOM = registerBlock("white_clover_blossom", properties -> new PlantopiaCloverBlossomBlock(() -> MobEffects.LUCK, 14, properties), MetaProperties.of(MetaType.CLOVER_FLOWER).customModel().color(DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Block> PINK_CLOVER_BLOSSOM = registerBlock("pink_clover_blossom", properties -> new PlantopiaCloverBlossomBlock(() -> MobEffects.LUCK, 14, properties), MetaProperties.of(MetaType.CLOVER_FLOWER).customModel().color(DyeColor.PINK));

	public static final RegistryObject<Block> COBBLESTONE_SHARD = registerBlock("cobblestone_shard", PlantopiaCobblestoneShardBlock::new, MetaProperties.of(MetaType.COBBLESTONE_SHARD).customModel().customDrop().customItem());
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_SHARD = registerBlock("mossy_cobblestone_shard", PlantopiaCobblestoneShardBlock::new, MetaProperties.of(MetaType.COBBLESTONE_SHARD).customModel().customDrop().customItem());
	public static final RegistryObject<Block> COBBLESTONE_SHARD_PET = registerBlock("cobblestone_shard_pet", properties -> new PlantopiaCobblestoneShardPetBlock(COBBLESTONE_SHARD, properties), MetaProperties.of(MetaType.COBBLESTONE_SHARD).customModel().customDrop().cutoutMippedRender().noItem());
	public static final RegistryObject<Block> MOSSY_COBBLESTONE_SHARD_PET = registerBlock("mossy_cobblestone_shard_pet", properties -> new PlantopiaCobblestoneShardPetBlock(MOSSY_COBBLESTONE_SHARD, properties), MetaProperties.of(MetaType.COBBLESTONE_SHARD).customModel().customDrop().cutoutMippedRender().noItem());

	public static final RegistryObject<Block> GIANT_GRASS = registerBlock("giant_grass", PlantopiaTriplePlantBlock::new, MetaProperties.of(MetaType.TALL_GRASS).tripleHighPlant().grassTint().customDrop());
	public static final RegistryObject<Block> GIANT_FERN = registerBlock("giant_fern", PlantopiaTriplePlantBlock::new, MetaProperties.of(MetaType.TALL_GRASS).tripleHighPlant().grassTint().customDrop().customModel());

	public static final RegistryObject<Block> BRANCHING_SHRUB = registerBlock("branching_shrub", PlantopiaBranchingShrubBlock::new, MetaProperties.of(MetaType.SHRUB).strength(0.8F).sound(SoundType.MANGROVE_ROOTS).hasDynamicShape().customModel().customDrop().pottable());
	public static final RegistryObject<Block> THORNY_SHRUB = registerBlock("thorny_shrub", PlantopiaThornyShrubBlock::new, MetaProperties.of(MetaType.SHRUB).strength(0.2F).sound(SoundType.SWEET_BERRY_BUSH).dropSelfByShears().customModel().mapColor(MapColor.COLOR_GRAY));

	public static final RegistryObject<Block> FIREWEED = registerBlock("fireweed", PlantopiaHerbBlock::new, MetaProperties.of(MetaType.HERB).customTint());
	public static final RegistryObject<Block> CHICORY = registerBlock("chicory", PlantopiaHerbBlock::new, MetaProperties.of(MetaType.HERB).customTint());
	public static final RegistryObject<Block> CARROTWEED = registerBlock("carrotweed", PlantopiaHerbBlock::new, MetaProperties.of(MetaType.HERB).customTint().customDrop());
	public static final RegistryObject<Block> TANSY = registerBlock("tansy", PlantopiaHerbBlock::new, MetaProperties.of(MetaType.HERB).customTint());

	public static final RegistryObject<Block> POLLINATED_DANDELION = registerBlock("pollinated_dandelion", PlantopiaPollinatedDandelionBlock::new, MetaProperties.of(MetaType.SMALL_FLOWER).customModel().customDrop().noItem());
	public static final RegistryObject<Block> FLUFFY_DANDELION = registerBlock("fluffy_dandelion", properties -> new PlantopiaFluffyDandelionBlock(() -> MobEffects.SLOW_FALLING, 7, properties), MetaProperties.of(MetaType.SMALL_FLOWER).ignoredByBees().noColor());

	public static final RegistryObject<Block> SNOWDROP = registerBlock("snowdrop", properties -> new PlantopiaSnowdropBlock(() -> MobEffects.WEAKNESS, 12, properties), MetaProperties.of(MetaType.SMALL_FLOWER).customModel().color(DyeColor.WHITE));
	public static final RegistryObject<Block> COVERED_SNOWDROP = registerBlock("covered_snowdrop", PlantopiaCoveredSnowdropBlock::new, MetaProperties.of(MetaType.SNOW).noItem().customModel().customDrop().preferredByBees());

	public static final RegistryObject<Block> TINY_CACTUS = registerBlock("tiny_cactus", PlantopiaTinyCactusBlock::new, MetaProperties.of(MetaType.TINY_CACTUS).customModel());
	public static final RegistryObject<Block> FLOWERING_TINY_CACTUS = registerBlock("flowering_tiny_cactus", PlantopiaTinyCactusBlock::new, MetaProperties.of(MetaType.TINY_CACTUS).customModel());

	public static final RegistryObject<Block> QUICKSAND = registerBlock("quicksand", PlantopiaQuicksandBlock::new, MetaProperties.of(MetaType.SAND).modifyBehaviour(properties -> properties.forceSolidOn().isRedstoneConductor(PlantopiaBlocks::never)).hasDynamicShape().noItem().noDrop().customModel());
	public static final RegistryObject<Block> QUICKSAND_CAULDRON = registerBlock("quicksand_cauldron", properties -> new PlantopiaQuicksandCauldronBlock(PlantopiaBlocks.QUICKSAND, properties), MetaProperties.of(MetaType.CAULDRON).noItem().customModel());

	public static final RegistryObject<Block> RED_WILDFLOWERS = registerBlock("red_wildflowers", properties -> new PlantopiaWildflowersBlock(() -> MobEffects.WEAKNESS, 6, properties), MetaProperties.of(MetaType.WILDFLOWERS).color(DyeColor.RED));
	public static final RegistryObject<Block> ORANGE_WILDFLOWERS = registerBlock("orange_wildflowers", properties -> new PlantopiaWildflowersBlock(() -> MobEffects.WEAKNESS, 6, properties), MetaProperties.of(MetaType.WILDFLOWERS).color(DyeColor.ORANGE));
	public static final RegistryObject<Block> YELLOW_WILDFLOWERS = registerBlock("yellow_wildflowers", properties -> new PlantopiaWildflowersBlock(() -> MobEffects.WEAKNESS, 6, properties), MetaProperties.of(MetaType.WILDFLOWERS).color(DyeColor.YELLOW));
	public static final RegistryObject<Block> WHITE_WILDFLOWERS = registerBlock("white_wildflowers", properties -> new PlantopiaWildflowersBlock(() -> MobEffects.WEAKNESS, 6, properties), MetaProperties.of(MetaType.WILDFLOWERS).color(DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Block> PINK_WILDFLOWERS = registerBlock("pink_wildflowers", properties -> new PlantopiaWildflowersBlock(() -> MobEffects.WEAKNESS, 6, properties), MetaProperties.of(MetaType.WILDFLOWERS).color(DyeColor.PINK));
	public static final RegistryObject<Block> PURPLE_WILDFLOWERS = registerBlock("purple_wildflowers", properties -> new PlantopiaWildflowersBlock(() -> MobEffects.WEAKNESS, 6, properties), MetaProperties.of(MetaType.WILDFLOWERS).color(DyeColor.PURPLE));

	public static final RegistryObject<Block> RED_FOXGLOVE = registerBlock("red_foxglove", PlantopiaFoxgloveBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.RED));
	public static final RegistryObject<Block> ORANGE_FOXGLOVE = registerBlock("orange_foxglove", PlantopiaFoxgloveBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.ORANGE));
	public static final RegistryObject<Block> YELLOW_FOXGLOVE = registerBlock("yellow_foxglove", PlantopiaFoxgloveBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.YELLOW));
	public static final RegistryObject<Block> WHITE_FOXGLOVE = registerBlock("white_foxglove", PlantopiaFoxgloveBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.WHITE));
	public static final RegistryObject<Block> PINK_FOXGLOVE = registerBlock("pink_foxglove", PlantopiaFoxgloveBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.PINK));
	public static final RegistryObject<Block> MAGENTA_FOXGLOVE = registerBlock("magenta_foxglove", PlantopiaFoxgloveBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.MAGENTA));

	public static final RegistryObject<Block> RED_MALLOW = registerBlock("red_mallow", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.RED));
	public static final RegistryObject<Block> YELLOW_MALLOW = registerBlock("yellow_mallow", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.YELLOW));
	public static final RegistryObject<Block> WHITE_MALLOW = registerBlock("white_mallow", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.WHITE));
	public static final RegistryObject<Block> PINK_MALLOW = registerBlock("pink_mallow", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.PINK));
	public static final RegistryObject<Block> MAGENTA_MALLOW = registerBlock("magenta_mallow", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.MAGENTA));
	public static final RegistryObject<Block> PURPLE_MALLOW = registerBlock("purple_mallow", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.PURPLE));

	public static final RegistryObject<Block> RED_LUPINE = registerBlock("red_lupine", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.RED));
	public static final RegistryObject<Block> YELLOW_LUPINE = registerBlock("yellow_lupine", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.YELLOW));
	public static final RegistryObject<Block> WHITE_LUPINE = registerBlock("white_lupine", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.WHITE));
	public static final RegistryObject<Block> PINK_LUPINE = registerBlock("pink_lupine", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.PINK));
	public static final RegistryObject<Block> PURPLE_LUPINE = registerBlock("purple_lupine", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.PURPLE));
	public static final RegistryObject<Block> BLUE_LUPINE = registerBlock("blue_lupine", TallFlowerBlock::new, MetaProperties.of(MetaType.TALL_FLOWER).customModel().color(DyeColor.BLUE));

	public static final RegistryObject<Block> SWEET_FLAG = registerBlock("sweet_flag", PlantopiaWaterloggedDoublePlantBlock::new, MetaProperties.of(MetaType.WATER_GRASS).dropSelfByShears().grassTint());
	public static final RegistryObject<Block> CATTAIL = registerBlock("cattail", PlantopiaWaterloggedDoublePlantBlock::new, MetaProperties.of(MetaType.WATER_GRASS).dropSelfByShears().grassTint().customModel());
	public static final RegistryObject<Block> REEDS = registerBlock("reeds", PlantopiaReedsBlock::new, MetaProperties.of(MetaType.WATER_GRASS).mapColor(MapColor.WOOD).dropSelfByShears());
	public static final RegistryObject<Block> ICY_REEDS = registerBlock("icy_reeds", PlantopiaIcyReedsBlock::new, MetaProperties.of(MetaType.ICE).randomlyTicking().translucentRender().noItem().customModel().noDrop());
	public static final RegistryObject<Block> TALL_REEDS = registerBlock("tall_reeds", PlantopiaTallReedsBlock::new, MetaProperties.of(MetaType.WATER_GRASS).mapColor(MapColor.WOOD).tripleHighPlant().customModel().customTint().dropSelfByShears());

	public static final RegistryObject<Block> DUNE_GRASS = registerBlock("dune_grass", PlantopiaDuneGrassBlock::new, MetaProperties.of(MetaType.SMALL_GRASS).mapColor(MapColor.SAND));
	public static final RegistryObject<Block> TALL_DUNE_GRASS = registerBlock("tall_dune_grass", PlantopiaTallDuneGrassBlock::new, MetaProperties.of(MetaType.TALL_GRASS).mapColor(MapColor.SAND).customDrop());

	public static final RegistryObject<Block> SEA_MOSS_BLOCK = registerBlock("sea_moss_block", PlantopiaSeaMossBlock::new, MetaProperties.of(MetaType.SEA_MOSS));
	public static final RegistryObject<Block> SEA_MOSS_CARPET = registerBlock("sea_moss_carpet", PlantopiaSeaMossCarpetBlock::new, MetaProperties.of(MetaType.SEA_MOSS).customModel());
	public static final RegistryObject<Block> SEA_HANGING_MOSS = registerBlock("sea_hanging_moss", PlantopiaWaterloggedHangingMossBlock::new, MetaProperties.of(MetaType.SEA_HANGING_MOSS));

	public static final RegistryObject<Block> SMALL_PLATTERLEAF = registerBlock("small_platterleaf", PlantopiaSmallPlatterleafBlock::new, MetaProperties.of(MetaType.WATERLILY).dropSelf().customModel().customItem());
	public static final RegistryObject<Block> BIG_PLATTERLEAF = registerBlock("big_platterleaf", PlantopiaBigPlatterleafBlock::new, MetaProperties.of(MetaType.WATERLILY).customDrop().customModel().customItem().compostable(Compostability.PLANT_1 * 2));

	public static final RegistryObject<Block> RED_WATERLILY = registerBlock("red_waterlily", properties -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, properties), MetaProperties.of(MetaType.WATERLILY_FLOWER).color(DyeColor.RED));
	public static final RegistryObject<Block> YELLOW_WATERLILY = registerBlock("yellow_waterlily", properties -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, properties), MetaProperties.of(MetaType.WATERLILY_FLOWER).color(DyeColor.YELLOW));
	public static final RegistryObject<Block> WHITE_WATERLILY = registerBlock("white_waterlily", properties -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, properties), MetaProperties.of(MetaType.WATERLILY_FLOWER).color(DyeColor.WHITE));
	public static final RegistryObject<Block> PINK_WATERLILY = registerBlock("pink_waterlily", properties -> new PlantopiaWaterlilyFlowerBlock(() -> MobEffects.CONFUSION, 9, properties), MetaProperties.of(MetaType.WATERLILY_FLOWER).color(DyeColor.PINK));

	public static final RegistryObject<Block> RED_FLOWERING_LILY_PAD = registerBlock("red_flowering_lily_pad", properties -> new PlantopiaFloweringLilyPadBlock(RED_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY).lilyPadTint());
	public static final RegistryObject<Block> YELLOW_FLOWERING_LILY_PAD = registerBlock("yellow_flowering_lily_pad", properties -> new PlantopiaFloweringLilyPadBlock(YELLOW_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY).lilyPadTint());
	public static final RegistryObject<Block> WHITE_FLOWERING_LILY_PAD = registerBlock("white_flowering_lily_pad", properties -> new PlantopiaFloweringLilyPadBlock(WHITE_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY).lilyPadTint());
	public static final RegistryObject<Block> PINK_FLOWERING_LILY_PAD = registerBlock("pink_flowering_lily_pad", properties -> new PlantopiaFloweringLilyPadBlock(PINK_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY).lilyPadTint());
	public static final RegistryObject<Block> RED_FLOWERING_SMALL_PLATTERLEAF = registerBlock("red_flowering_small_platterleaf", properties -> new PlantopiaFloweringSmallPlatterleafBlock(RED_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY));
	public static final RegistryObject<Block> YELLOW_FLOWERING_SMALL_PLATTERLEAF = registerBlock("yellow_flowering_small_platterleaf", properties -> new PlantopiaFloweringSmallPlatterleafBlock(YELLOW_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY));
	public static final RegistryObject<Block> WHITE_FLOWERING_SMALL_PLATTERLEAF = registerBlock("white_flowering_small_platterleaf", properties -> new PlantopiaFloweringSmallPlatterleafBlock(WHITE_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY));
	public static final RegistryObject<Block> PINK_FLOWERING_SMALL_PLATTERLEAF = registerBlock("pink_flowering_small_platterleaf", properties -> new PlantopiaFloweringSmallPlatterleafBlock(PINK_WATERLILY, properties), MetaProperties.of(MetaType.FLOWERING_WATERLILY));

	public static final RegistryObject<Block> AZOLLA = registerBlock("azolla", PlantopiaAzollaBlock::new, MetaProperties.of(MetaType.WATER_PLANT).sound(SoundType.WET_GRASS).replaceable().customModel().customDrop().customItem().compostable(Compostability.PLANT_1 * 0.75F));
	public static final RegistryObject<Block> SEAWEED = registerBlock("seaweed", PlantopiaSeaweedBlock::new, MetaProperties.of(MetaType.UNDERWATER_PLANT).customModel().dropSelfByShears());

	public static final RegistryObject<Block> ROUND_SEA_SHELL = registerBlock("round_sea_shell", properties -> new PlantopiaSeaShellBlock(PlantopiaSeaShellBlock.ROUND_SHAPE, properties), MetaProperties.of(MetaType.SEA_SHELL));
	public static final RegistryObject<Block> TWISTY_SEA_SHELL = registerBlock("twisty_sea_shell", properties -> new PlantopiaSeaShellBlock(PlantopiaSeaShellBlock.TWISTY_SHAPE, properties), MetaProperties.of(MetaType.SEA_SHELL));
	public static final RegistryObject<Block> TUBE_SEA_SHELL = registerBlock("tube_sea_shell", properties -> new PlantopiaSeaShellBlock(PlantopiaSeaShellBlock.TUBE_SHAPE, properties), MetaProperties.of(MetaType.SEA_SHELL));

	public static final RegistryObject<Block> WHITE_LUCKY_DAISY = registerBlock("white_lucky_daisy", properties -> new PlantopiaLuckyDaisyBlock(() -> MobEffects.REGENERATION, 10, properties), MetaProperties.of(MetaType.LUCKY_DAISY).color(DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Block> PINK_LUCKY_DAISY = registerBlock("pink_lucky_daisy", properties -> new PlantopiaLuckyDaisyBlock(() -> MobEffects.REGENERATION, 10, properties), MetaProperties.of(MetaType.LUCKY_DAISY).color(DyeColor.PINK));

	public static final RegistryObject<Block> WITCHY_TOADSTOOL_BLOCK = registerBlock("witchy_toadstool_block", HugeMushroomBlock::new, MetaProperties.of(MetaType.MUSHROOM_BLOCK).mapColor(MapColor.TERRACOTTA_PURPLE).customDrop());
	public static final RegistryObject<Block> WITCHY_TOADSTOOL = registerBlock("witchy_toadstool", properties -> new PlantopiaWitchyToadstoolBlock(properties, PlantopiaTreeFeatures.HUGE_WITCHY_TOADSTOOL), MetaProperties.of(MetaType.MUSHROOM).mapColor(MapColor.TERRACOTTA_PURPLE).lightLevel(1));

	public static final SupposedRegistryObject<Block> POTTED_BRANCHING_SHRUB = supposeBlock(pottedNameOf(BRANCHING_SHRUB));

	static {
		registerPottedBlocks();
	}

	public static SupposedRegistryObject<Block> supposeBlock(String name) {
		return PlantopiaRegistries.BLOCK.supposeValue(plantopia(name));
	}

	public static <T extends Block> RegistryObject<T> registerBlock(String name, Function<Properties, T> factory, MetaProperties metaProperties) {
		return registerBlock(plantopia(name), factory, metaProperties);
	}

	public static <T extends Block> RegistryObject<T> registerBlock(ResourceLocation identifier, Function<Properties, T> factory, MetaProperties metaProperties) {
		var blockMeta = PlantopiaMetaBuckets.BLOCK.associate(identifier, new PlantopiaBlockMeta(identifier, metaProperties));

		PlantopiaItems.registerBlockItem(blockMeta);

		return PlantopiaRegistries.BLOCK.register(identifier, () -> factory.apply(blockMeta.createBehaviourProperties()));
	}

	@SuppressWarnings("UnusedReturnValue")
	public static RegistryObject<FlowerPotBlock> registerPottedBlock(String plantName, Supplier<? extends Block> plantSupplier, PlantopiaTintType plantTintType) {
		return registerBlock(pottedNameOf(plantName), properties -> new FlowerPotBlock(RegistryHelper::getEmptyFlowerPotBlock, plantSupplier, properties), MetaProperties.of(MetaType.POTTED).pottedTint(plantTintType));
	}

	private static void registerPottedBlocks() {
		registerPottedBlock(nameOf(Blocks.GRASS), () -> Blocks.GRASS, PlantopiaTintType.GRASS);

		PlantopiaMetaBuckets.BLOCK.findAll(PlantopiaBlockMeta::isPottable).forEach(blockMeta ->
			registerPottedBlock(blockMeta.getName(), blockMeta, blockMeta.getTintType())
		);
	}

	public static void setup(@NotNull RegisterEvent event) {
		event.registerAll(Registries.BLOCK, PlantopiaRegistries.BLOCK);
	}

	/* HELPER METHODS *****************************************************************************************/

	@Contract(pure = true)
	private static @NotNull Boolean never(BlockState state, BlockGetter level, BlockPos pos) {
		return false;
	}
}