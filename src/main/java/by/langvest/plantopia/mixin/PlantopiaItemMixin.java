package by.langvest.plantopia.mixin;

import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import by.langvest.plantopia.meta.store.PlantopiaMetaStore;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public abstract class PlantopiaItemMixin implements IForgeItem {
	@Override
	public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		@Nullable PlantopiaItemMeta itemMeta = PlantopiaMetaStore.getItem(itemStack.getItem());

		if(itemMeta == null) return -1;

		return itemMeta.getBurnTime();
	}
}
