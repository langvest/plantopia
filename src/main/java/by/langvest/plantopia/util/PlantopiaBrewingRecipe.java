package by.langvest.plantopia.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public class PlantopiaBrewingRecipe implements IBrewingRecipe {
	private final Potion output;
	private final Item ingredient;
	private final Potion input;

	public PlantopiaBrewingRecipe(@NotNull Potion result, @NotNull ItemLike ingredient, @NotNull Potion precursor) {
		this.output = result;
		this.ingredient = ingredient.asItem();
		this.input = precursor;
	}

	@Override
	public boolean isInput(@NotNull ItemStack input) {
		return PotionUtils.getPotion(input) == this.input;
	}

	@Override
	public boolean isIngredient(@NotNull ItemStack ingredient) {
		return ingredient.getItem() == this.ingredient;
	}

	@Override
	public @NotNull ItemStack getOutput(@NotNull ItemStack input, @NotNull ItemStack ingredient) {
		if(!isInput(input)) return ItemStack.EMPTY;
		if(!isIngredient(ingredient)) return ItemStack.EMPTY;

		ItemStack result = input.getItem().getDefaultInstance();
		result.setTag(new CompoundTag());
		PotionUtils.setPotion(result, output);
		return result;
	}
}