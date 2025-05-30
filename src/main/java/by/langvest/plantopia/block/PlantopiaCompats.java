package by.langvest.plantopia.block;

import by.langvest.plantopia.meta.PlantopiaMetaRegistries;
import by.langvest.plantopia.util.PlantopiaBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.jetbrains.annotations.NotNull;

import static by.langvest.plantopia.util.helper.PlantopiaContentHelper.*;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.locationOf;

public class PlantopiaCompats {
	public static void setup() {
		registerAll();

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

		registerBrewable(Potions.LUCK, PlantopiaBlocks.BIG_CLOVER.get(), Potions.AWKWARD);
	}

	private static void registerAll() {
		PlantopiaMetaRegistries.BLOCKS.forEach(blockMeta -> {
			var block = blockMeta.getBlock();

			if(blockMeta.isFlammable()) registerFlammable(block, blockMeta.getEncouragement(), blockMeta.getFlammability());
			if(blockMeta.isCompostable()) registerCompostable(block, blockMeta.getCompostability());
			if(block instanceof FlowerPotBlock flowerPotBlock) registerPotted(flowerPotBlock);
		});
	}

	private static void registerFlammable(Block block, int encouragement, int flammability) {
		FIRE_BLOCK.setFlammable(block, encouragement, flammability);
	}

	public static void registerCompostable(@NotNull ItemLike item, float compostability) {
		COMPOSTABLES.put(item.asItem(), compostability);
	}

	public static void registerPotted(@NotNull FlowerPotBlock pottedBlock) {
		ResourceLocation plantLocation = locationOf(pottedBlock.getContent());
		FLOWER_POT_BLOCK.addPlant(plantLocation, () -> pottedBlock);
	}

	public static void registerBrewable(Potion result, ItemLike ingredient, Potion precursor) {
		BrewingRecipeRegistry.addRecipe(new PlantopiaBrewingRecipe(result, ingredient, precursor));
	}

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
		public static final float FLOWER = CHANCE_65;
		public static final float MUSHROOM_PLANT = CHANCE_65;
		public static final float MUSHROOM_STEM = CHANCE_65;
		public static final float MUSHROOM_BLOCK = CHANCE_85;
		public static final float HAS_FLOWERS = 0.05F;
	}

	public static final class Encouragement {
		public static final int PLANT = 60;
		public static final int WOOD = 5;
		public static final int PLANKS = WOOD;
		public static final int LEAVES = 30;
	}

	public static final class Flammability {
		public static final int PLANT = 100;
		public static final int WOOD = 5;
		public static final int PLANKS = 20;
		public static final int LEAVES = 60;
	}
}
