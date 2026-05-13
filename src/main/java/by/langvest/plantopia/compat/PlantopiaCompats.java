package by.langvest.plantopia.compat;

import by.langvest.plantopia.Plantopia;
import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.block.PlantopiaCauldronInteraction;
import by.langvest.plantopia.block.PlantopiaFloweringWaterlilyBlock;
import by.langvest.plantopia.item.special.PlantopiaWaterlilyFlowerBlockItem;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.toolkit.event.LifecycleEvent;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PlantopiaCompats {
	public static void setup(LifecycleEvent.CommonSetupEvent event) {
		generateAll();

		registerFlammable(Blocks.OAK_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.SPRUCE_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.BIRCH_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.JUNGLE_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.ACACIA_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.DARK_OAK_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.BAMBOO_SAPLING, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.SUGAR_CANE, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.SEA_PICKLE, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.MOSS_BLOCK, Encouragement.PLANT, Flammability.PLANT);
		registerFlammable(Blocks.MOSS_CARPET, Encouragement.PLANT, Flammability.PLANT);

		registerBrewable(PlantopiaBlocks.BIG_CLOVER.get(), Potions.LUCK, Potions.AWKWARD);

		PlantopiaCauldronInteraction.setup();
	}

	private static void generateAll() {
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			var block = blockMeta.get();

			if(blockMeta.isFlammable()) registerFlammable(block, blockMeta.getEncouragement(), blockMeta.getFlammability());
			if(blockMeta.isCompostable()) registerCompostable(block, blockMeta.getCompostability());
			if(blockMeta.isStrippable()) registerStrippable(block, blockMeta.getStripped());
			if(block instanceof FlowerPotBlock pottedBlock) registerPottable(pottedBlock);
			if(block instanceof PlantopiaFloweringWaterlilyBlock waterlilyBlock) registerFloweringWaterlily(waterlilyBlock);
		});
	}

	public static void registerFloweringWaterlily(@NotNull PlantopiaFloweringWaterlilyBlock waterlilyBlock) {
		var flowerBlock = waterlilyBlock.getFlowerBlock();
		var originBlock = waterlilyBlock.getOriginBlock();
		var state = ((Block) waterlilyBlock).defaultBlockState();
		PlantopiaWaterlilyFlowerBlockItem.addFloweringWaterlily(Pair.of(flowerBlock, originBlock), state);
	}

	private static void registerFlammable(Block block, int encouragement, int flammability) {
		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		registryHelper.registerFlammable(block, encouragement, flammability);
	}

	public static void registerCompostable(@NotNull ItemLike itemLike, float compostability) {
		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		registryHelper.registerCompostable(itemLike, compostability);
	}

	public static void registerPottable(@NotNull FlowerPotBlock pottedBlock) {
		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		registryHelper.registerPottable(pottedBlock.getContent(), pottedBlock);
	}

	public static void registerBrewable(@NotNull ItemLike ingredient, Potion result, Potion precursor) {
		var registryHelper = Plantopia.getPlatform().getRegistryHelper();
		registryHelper.registerBrewable(precursor, ingredient, result);
	}

	public static void registerStrippable(Block log, Block stripped) {
		AxeItem.STRIPPABLES = Maps.newHashMap(AxeItem.STRIPPABLES);
		AxeItem.STRIPPABLES.put(log, stripped);
	}

	public static void registerFlattenable(Block block, BlockState flattened) {
		ShovelItem.FLATTENABLES = Maps.newHashMap(ShovelItem.FLATTENABLES);
		ShovelItem.FLATTENABLES.put(block, flattened);
	}

	/**
	 * @see net.minecraft.world.level.block.ComposterBlock#bootStrap()
	 */
	public static final class Compostability {
		public static final float CHANCE_30 = 0.3F;
		public static final float CHANCE_50 = 0.5F;
		public static final float CHANCE_60 = 0.6F;
		public static final float CHANCE_65 = 0.65F;
		public static final float CHANCE_85 = 0.85F;
		public static final float CHANCE_100 = 1.0F;
		public static final float PLANT_1 = CHANCE_30;
		public static final float PLANT_2 = CHANCE_50;
		public static final float PLANT_3 = CHANCE_60;
		public static final float LEAVES = CHANCE_30;
		public static final float FLOWER = CHANCE_65;
		public static final float MUSHROOM = CHANCE_65;
		public static final float MUSHROOM_STEM = CHANCE_65;
		public static final float MUSHROOM_BLOCK = CHANCE_85;
		public static final float HAS_FLOWERS = 0.05F;
		public static final float LEAF_LITTER = PLANT_1 * 0.75F;
	}

	/**
	 * @see net.minecraft.world.level.block.FireBlock#bootStrap()
	 */
	public static final class Encouragement {
		public static final int PLANT = 60;
		public static final int PLANT_2 = 30;
		public static final int WOOD = 5;
		public static final int PLANKS = WOOD;
		public static final int LEAVES = 30;
	}

	/**
	 * @see net.minecraft.world.level.block.FireBlock#bootStrap()
	 */
	public static final class Flammability {
		public static final int PLANT = 100;
		public static final int PLANT_2 = 150;
		public static final int WOOD = 5;
		public static final int PLANKS = 20;
		public static final int LEAVES = 60;
	}

	/**
	 * @see net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity#getFuel()
	 */
	public static final class BurnTime {
		public static final int WOODY_PLANT = 100;
		public static final int LEAF_LITTER = 67;
	}
}
