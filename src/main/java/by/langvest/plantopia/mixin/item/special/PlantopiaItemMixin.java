package by.langvest.plantopia.mixin.item.special;

import by.langvest.plantopia.meta.object.PlantopiaItemMeta;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import static by.langvest.plantopia.util.helper.PlantopiaResourceHelper.metaOf;

@Mixin(Item.class)
public abstract class PlantopiaItemMixin implements IForgeItem {
	@Override
	public int getBurnTime(@NotNull ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return metaOf(itemStack.getItem()).map(PlantopiaItemMeta::getBurnTime).orElse(-1);
	}
}
