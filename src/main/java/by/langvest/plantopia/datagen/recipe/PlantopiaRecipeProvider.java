package by.langvest.plantopia.datagen.recipe;

import by.langvest.plantopia.block.PlantopiaBlocks;
import by.langvest.plantopia.meta.PlantopiaMetaBuckets;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta;
import by.langvest.plantopia.meta.object.PlantopiaBlockMeta.MetaType;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.nameOf;
import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.plantopiaLocationFrom;

public class PlantopiaRecipeProvider extends RecipeProvider implements IConditionBuilder {
	private Consumer<FinishedRecipe> consumer;

	public PlantopiaRecipeProvider(PackOutput output) {
		super(output);
	}

	@Override
	protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
		setConsumer(consumer);
		generateAll();

		fullBlockRecipe(RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE, PlantopiaBlocks.COBBLESTONE_SHARD.get());
		fullBlockRecipe(RecipeCategory.BUILDING_BLOCKS, Blocks.MOSSY_COBBLESTONE, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get());

		stonecutterRecipe(RecipeCategory.MISC, PlantopiaBlocks.COBBLESTONE_SHARD.get(), Blocks.COBBLESTONE, 9);
		stonecutterRecipe(RecipeCategory.MISC, PlantopiaBlocks.MOSSY_COBBLESTONE_SHARD.get(), Blocks.MOSSY_COBBLESTONE, 9);

		smeltingRecipe(RecipeCategory.MISC, Items.GREEN_DYE, PlantopiaBlocks.TINY_CACTUS.get(), 1.0F, 200);
		smeltingRecipe(RecipeCategory.MISC, Items.PINK_DYE, PlantopiaBlocks.FLOWERING_TINY_CACTUS.get(), 1.0F, 200);

		woodFromLogs(consumer, PlantopiaBlocks.BIRCH_BASE_WOOD.get(), PlantopiaBlocks.BIRCH_BASE_LOG.get());
	}

	private void setConsumer(@NotNull Consumer<FinishedRecipe> consumer) {
		this.consumer = consumer;
	}

	private void generateAll() {
		PlantopiaMetaBuckets.BLOCK.forEach(blockMeta -> {
			if(!blockMeta.shouldGenerateRecipe()) return;

			var type = blockMeta.getType();

			if(type.instanceOf(MetaType.FLOWER)) {
				dyeFromFlower(blockMeta);
				return;
			}

			if(type.instanceOf(MetaType.SHELL)) {
				boneMealFromSeaShell(blockMeta);
			}
		});
	}

	/* RECIPES GENERATION ******************************************/

	private void boneMealFromSeaShell(@NotNull PlantopiaBlockMeta blockMeta) {
		oneToOneConversionRecipe(RecipeCategory.MISC, Items.BONE_MEAL, blockMeta.get(), nameOf(Items.BONE_MEAL), 1);
	}

	private void dyeFromFlower(@NotNull PlantopiaBlockMeta blockMeta) {
		var color = blockMeta.getColor();
		if(color == null) return;
		var dye = DyeItem.byColor(color);
		oneToOneConversionRecipe(RecipeCategory.MISC, dye, blockMeta.get(), nameOf(dye), blockMeta.getBlockHeightType().getBaseHeight());
	}

	/* RECIPE GENERATION HELPER METHODS ******************************************/

	private void oneToOneConversionRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient, String group, int resultAmount) {
		ShapelessRecipeBuilder.shapeless(category, result, resultAmount)
			.requires(ingredient)
			.group(group)
			.unlockedBy(getHasName(ingredient), has(ingredient))
			.save(consumer, plantopiaLocationFrom(getConversionRecipeName(result, ingredient)));
	}

	private void fullBlockRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient) {
		ShapedRecipeBuilder.shaped(category, result)
			.define('#', ingredient)
			.pattern("###")
			.pattern("###")
			.pattern("###")
			.unlockedBy(getHasName(ingredient), has(ingredient))
			.save(consumer, plantopiaLocationFrom(getSimpleRecipeName(result)));
	}

	private void stonecutterRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient, int resultAmount) {
		SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingredient), category, result, resultAmount)
			.unlockedBy(getHasName(ingredient), has(ingredient))
			.save(consumer, plantopiaLocationFrom(getConversionRecipeName(result, ingredient) + "_stonecutting"));
	}

	private void smeltingRecipe(RecipeCategory category, ItemLike result, ItemLike ingredient, float experience, int cookingTime) {
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), category, result, experience, cookingTime)
			.unlockedBy(getHasName(ingredient), has(ingredient))
			.save(consumer, plantopiaLocationFrom(getConversionRecipeName(result, ingredient) + "_smelting"));
	}
}